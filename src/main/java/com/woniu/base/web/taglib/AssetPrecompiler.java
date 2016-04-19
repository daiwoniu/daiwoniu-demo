package com.woniu.base.web.taglib;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

//TODO, uglify assets
public class AssetPrecompiler {

    static class Asset {
        final String path;
        final String digest;

        Asset(String path, String digest) {
            this.path = path;
            this.digest = digest;
        }
    }

    private static Pattern ASSET_PATTERN = Pattern.compile("\\.(css|js|ico|jpg|jpeg|png|gif)$", Pattern.CASE_INSENSITIVE);

    private String root = "webapp";
    private String contextPath = "/";
    private String output = "src/main/resources";

    public void setRoot(String root) {
        this.root = root;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void compile() throws IOException {
        final Path rootPath = Paths.get(root).toAbsolutePath();
        final String contextPath = this.contextPath.endsWith("/") ? this.contextPath : this.contextPath + "/";

        final List<Asset> assets = new ArrayList<>();
        final HashFunction sha1 = Hashing.sha1();

        Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (dir.getFileName().toString().equals("WEB-INF")) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String name = file.getFileName().toString();
                if (!ASSET_PATTERN.matcher(name).find()) {
                    return FileVisitResult.CONTINUE;
                }

                Path relativePath = rootPath.relativize(file);
                String path = contextPath + relativePath;
                String digest = sha1.hashBytes(Files.readAllBytes(file)).toString();
                Asset asset = new Asset(path, digest);
                System.out.println(path + "\t" + digest);

                assets.add(asset);

                return FileVisitResult.CONTINUE;
            }

        });

        List<String> lines = new ArrayList<>();
        for (Asset asset : assets) {
            lines.add(asset.path + "\t" + asset.digest);
        }
        Path manifestPath = Paths.get(output, "assets.manifest");
        Files.write(manifestPath, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    private static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("asset_precompiler", options);
        System.exit(1);
    }


    public static void main(String[] args) throws IOException {
        CommandLineParser parser = new GnuParser();
        Options options = new Options();
        options.addOption(OptionBuilder.withArgName("help").withDescription("show this help").create("h"));
        options.addOption(OptionBuilder.withArgName("root").withDescription("webapp root path").hasArg().create("r"));
        options.addOption(OptionBuilder.withArgName("output").withDescription("output directory").hasArg().create("o"));
        options.addOption(OptionBuilder.withArgName("context").withDescription("context path").hasArg().create("c"));

        AssetPrecompiler precompiler = new AssetPrecompiler();

        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("h")) {
                usage(options);
            }
            if (commandLine.hasOption("r")) {
                precompiler.setRoot(commandLine.getOptionValue("r"));
            }
            if (commandLine.hasOption("o")) {
                precompiler.setOutput(commandLine.getOptionValue("o"));
            }
            if (commandLine.hasOption("c")) {
                precompiler.setContextPath(commandLine.getOptionValue("c"));
            }
        } catch (ParseException e) {
            usage(options);
        }

        precompiler.compile();
    }
}
