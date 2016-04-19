package com.woniu.base.code;

import com.google.common.base.CaseFormat;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.apache.commons.cli.*;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 根据数据库中表结构，自动生成java类
 */
public class Generator {
	private final Map<String, TableDescriptor> tables;
	private final TableDescriptor table;

	public Generator(Map<String, TableDescriptor> tables, TableDescriptor table) {
		this.tables = tables;
		this.table = table;
	}

	public void generate(String packageName, String templatePath, File file,
			boolean force) throws IOException {
		if (file.exists() && !force) {
			System.out.println("file " + file + " exists, skipped");
			return;
		}

		String code = generateCode(packageName, templatePath);
		file.getParentFile().mkdirs();
		Files.write(code.getBytes(Charsets.UTF_8), file);
	}

	public String generateCode(String packageName, String templatePath)
			throws IOException {
		VelocityContext context = new VelocityContext();
		context.put("table", table);
		context.put("packageName", packageName);
		StringWriter writer = new StringWriter();

		URL url = Resources.getResource(templatePath);
		String template = Resources.toString(url, Charsets.UTF_8);

		VelocityEngine engine = new VelocityEngine();
		engine.setProperty("runtime.references.strict", false);
		engine.init();
		engine.evaluate(context, writer, "generator", template);
		return writer.toString();

	}

	public static void main(String[] args) throws Exception {
        String configPath = "/db.xml";
		Pattern includePattern = Pattern.compile(".*");
		Pattern excludePattern = null;
		String basePackageName = "com.woniu";
		String outputDir = "src/main/java";
		boolean force = false;
		String baseUri = "/";
		String types[] = { "all" };

		Options options = new Options();
		options.addOption("c", "config", true, "spring datasource config file(classpath)");
		options.addOption("i", "include", true, "include table pattern");
		options.addOption("x", "exclude", true, "exclude table pattern");
		options.addOption("p", "package", true, "base package name");
		options.addOption("o", "output", true, "output directory, default is "
				+ outputDir);
		options.addOption("u", "base-uri", true,
				"base uri prefix, default is /");
		options.addOption("f", "force", false,
				"force generate file even if file exists");
		options.addOption("h", "help", false, "show help message");
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("c")) {
                configPath = commandLine.getOptionValue("c");
            }
			if (commandLine.hasOption("i")) {
				includePattern = Pattern.compile(commandLine
						.getOptionValue("i"), Pattern.CASE_INSENSITIVE);
			}
			if (commandLine.hasOption("x")) {
				excludePattern = Pattern.compile(
						commandLine.getOptionValue("x"),
						Pattern.CASE_INSENSITIVE);
			}
			if (commandLine.hasOption("p")) {
				basePackageName = commandLine.getOptionValue("p");
			}
			if (commandLine.hasOption("o")) {
				outputDir = commandLine.getOptionValue("o");
			}
			if (commandLine.hasOption("u")) {
				baseUri = commandLine.getOptionValue("u");
			}
			force = commandLine.hasOption("f");
			if (commandLine.hasOption("h")) {
				usage(options);
			}
			String[] extraArgs = commandLine.getArgs();
			if (extraArgs.length > 0) {
				types = extraArgs;
			}
		} catch (Exception e) {
			usage(options);
		}

		Map<String, TableDescriptor> tables = loadTables(configPath, basePackageName,
				baseUri);

		for (Map.Entry<String, TableDescriptor> entry : tables.entrySet()) {
			String tableName = entry.getKey();
			if (excludePattern != null) {
				if (excludePattern.matcher(tableName).find()) {
					System.out.println("skip " + tableName);
					continue;
				}
			}
			if (includePattern != null) {
				if (!includePattern.matcher(tableName).find()) {
					System.out.println("skip " + tableName);
					continue;
				}
			}

			TableDescriptor table = entry.getValue();

			System.out.println("generate " + tableName + " ...");
			Generator generator = new Generator(tables, table);

			for (String type : new String[] { "entity", "service",
					"controller", "view" }) {
				if (!isTypeMatch(types, type)) {
					continue;
				}
				if (type.equals("view")) {
					generateViews(force, table, generator);
				} else {
					String packageName = basePackageName + "." + type;
					String templatePath = "com/woniu/base/code/" + type + ".vm";

					String packagePath = packageName.replace('.', '/');
					String className = table.getEntityClassName();
					if (!"entity".equals(type)) {
						className = className
								+ CaseFormat.LOWER_UNDERSCORE.to(
										CaseFormat.UPPER_CAMEL, type);
					}
					File file = new File(outputDir, packagePath + "/"
							+ className + ".java");

					generator.generate(packageName, templatePath, file, force);
				}
			}
		}

		System.out.println("done!");
	}

	private static boolean isTypeMatch(String[] types, String type) {
		for (String t : types) {
			if (t.equalsIgnoreCase(type) || "all".equalsIgnoreCase(t)) {
				return true;
			}
		}
		return false;
	}

	private static void generateViews(boolean force, TableDescriptor table,
									  Generator generator) throws IOException {
		for (String view : new String[] { "index", "new", "edit", "show" }) {
			String templatePath = "com/woniu/base/code/view/" + view + ".jsp.vm";
			File file = new File("webapp/WEB-INF/pages"
					+ table.getUriPrefix() + "/" + view
					+ ".jsp");
			generator.generate(null, templatePath, file, force);
		}
	}

	private static Map<String, TableDescriptor> loadTables(String configPath,
			String basePackageName, String baseUri) throws SQLException {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                configPath);
		DataSource dataSource = ctx.getBean(DataSource.class);

		Connection connection = dataSource.getConnection();

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String database = jdbcTemplate.queryForObject("select database()",
				String.class);
		String sql = "select * from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA = '"
				+ database + "'";

		Map<String, TableDescriptor> tables = Maps.newHashMap();

		List<Map<String, Object>> columns = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> columnInfo : columns) {
			String tableName = (String) columnInfo.get("TABLE_NAME");

			ColumnDescriptor column = new ColumnDescriptor();
			column.columnName = (String) columnInfo.get("COLUMN_NAME");
			column.setDefaultValue(columnInfo.get("COLUMN_DEFAULT"));
			column.dataType = (String) columnInfo.get("DATA_TYPE");
			column.nullable = "YES".equals(columnInfo.get("IS_NULLABLE"));
			column.primary = "PRI".equals(columnInfo.get("COLUMN_KEY"));

			String columnType = (String) columnInfo.get("COLUMN_TYPE");
			column.setColumnType(columnType);
			column.setComment((String) columnInfo.get("COLUMN_COMMENT"));

			TableDescriptor table = tables.get(tableName);
			if (table == null) {
				table = new TableDescriptor(tableName, basePackageName, baseUri);
				tables.put(tableName, table);
			}
			table.addColumn(column);
		}

		sql = "select * from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA = '"
				+ database + "'";
		List<Map<String, Object>> tableInfos = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> tableInfo : tableInfos) {
			String tableName = (String) tableInfo.get("TABLE_NAME");
			String comment = (String) tableInfo.get("TABLE_COMMENT");

			TableDescriptor table = tables.get(tableName);
			if (table != null) {
				table.setComment(comment);
			}
		}

		connection.close();
		ctx.close();
		return tables;
	}

	private static void usage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(
				"Main [options] [all|entity|service|controller|view]", options);
		System.exit(1);
	}

}
