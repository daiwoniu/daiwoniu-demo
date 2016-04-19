package com.woniu.base.db;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.woniu.base.db.EntityClassWrapper.ColumnField;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class SQLExecutor<T> {

    private final JdbcTemplate jdbcTemplate;
    private final EntityClassWrapper entityClassWrapper;
    private final String tableName;

    public SQLExecutor(JdbcTemplate jdbcTemplate, Class<T> klass) {
        this(jdbcTemplate, klass, null);
    }

    public SQLExecutor(JdbcTemplate jdbcTemplate, Class<T> klass, String tableName) {
        this.jdbcTemplate = jdbcTemplate;
        entityClassWrapper = EntityClassWrapper.wrap(klass);
        this.tableName = tableName;
    }

    private String buildColumns(Object entity, boolean includeId) {
        List<String> list = Lists.newArrayList();

        for (ColumnField field : entityClassWrapper.getColumnFields()) {
            if (field.isId() && !includeId) {
                continue;
            }

            if (isNull(entity, field)) {
                continue;
            }

            list.add(field.getColumnName());
        }

        return Joiner.on(",").join(list);
    }

    private String buildColumnPlaceholders(Object entity, boolean includeId) {
        List<String> list = Lists.newArrayList();

        for (ColumnField field : entityClassWrapper.getColumnFields()) {
            if (field.isId() && !includeId) {
                continue;
            }

            if (isNull(entity, field)) {
                continue;
            }

            list.add("?");
        }

        return Joiner.on(",").join(list);
    }

    private List<Object> buildParameters(Object entity, boolean includeId) {
        List<Object> list = Lists.newArrayList();

        for (ColumnField field : entityClassWrapper.getColumnFields()) {
            if (field.isId() && !includeId) {
                continue;
            }

            if (isNull(entity, field)) {
                continue;
            }

            Object value = field.getJdbcValue(entity);
            list.add(value);
        }

        return list;
    }

    private boolean isNull(Object entity, ColumnField f) {
        return f.get(entity) == null;
    }

    private void setupTimestampFields(T entity) {
        for (ColumnField field : entityClassWrapper.getColumnFields()) {
            if (field.isTimestamp() && isNull(entity, field)) {
                if (field.getType() == DateTime.class) {
                    field.set(entity, DateTime.now());
                } else if (field.getType() == Date.class) {
                    field.set(entity, new Date());
                } // ignore else
            }
        }
    }

    public int batchInsert(List<T> entities, boolean replace) {
        if (entities.isEmpty()) {
            return 0;
        }

        T firstEntity = entities.get(0);
        setupTimestampFields(firstEntity);

        boolean includeId = entityClassWrapper.isIdPresent(firstEntity);

        final StringBuilder sql = new StringBuilder();
        final List<Object> parameters = new ArrayList<>();

        if (replace) {
            sql.append("replace into ");
        } else {
            sql.append("insert into ");
        }
        sql.append(getTableName())
                .append("(");
        sql.append(buildColumns(firstEntity, includeId)).append(")");
        sql.append(" values");

        int size = entities.size();
        for (int i = 0; i < size; i++) {
            T entity = entities.get(i);
            setupTimestampFields(entity);

            parameters.addAll(buildParameters(entity, includeId));

            sql.append("(");
            sql.append(buildColumnPlaceholders(entity, includeId));
            sql.append(")");
            if (i < size - 1) {
                sql.append(",");
            }
        }

        return jdbcTemplate.update(sql.toString(), parameters.toArray());
    }

    public int insert(T entity, boolean replace) {
        setupTimestampFields(entity);

        boolean includeId = entityClassWrapper.isIdPresent(entity);

        final StringBuilder sql = new StringBuilder();
        final List<Object> parameters = buildParameters(entity, includeId);

        if (replace) {
            sql.append("replace into ");
        } else {
            sql.append("insert into ");
        }
        sql.append(getTableName())
                .append("(");
        sql.append(buildColumns(entity, includeId)).append(")");
        sql.append(" values(");
        sql.append(buildColumnPlaceholders(entity, includeId));
        sql.append(")");

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        int result = jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql.toString(),
                        Statement.RETURN_GENERATED_KEYS);
                int idx = 1;
                for (Object param : parameters) {
                    StatementCreatorUtils.setParameterValue(ps, idx++,
                            SqlTypeValue.TYPE_UNKNOWN, param);
                }
                return ps;
            }
        }, generatedKeyHolder);

        if (replace) {
            //返回的数量会翻倍, 原因未知
            return result / 2;
        }

        Number key = generatedKeyHolder.getKey();
        if (key != null) {
            ColumnField idField = entityClassWrapper.getIdColumnField();
            if (idField == null) {
                throw new IllegalArgumentException("id column is required!");
            }
            Class<?> keyClass = idField.getField().getType();
            if (keyClass == Long.class || keyClass == Long.TYPE) {
                idField.set(entity, key.longValue());
            } else {
                idField.set(entity, key.intValue());
            }
        }

        return result;
    }

    private String buildSetsForUpdate(Object entity, boolean includeId) {
        List<String> list = Lists.newArrayList();

        for (ColumnField field : entityClassWrapper.getColumnFields()) {
            if (field.isId() && !includeId) {
                continue;
            }

            if (isNull(entity, field)) {
                list.add(field.getColumnName() + " = NULL");
                continue;
            }

            list.add(field.getColumnName() + " = ?");
        }

        return Joiner.on(",").join(list);
    }

    private String buildIdCondition() {
        ColumnField idField = entityClassWrapper.getIdColumnField();
        if (idField == null) {
            throw new IllegalStateException("id column is required");
        }

        return idField.getColumnName() + "=?";
    }

    private void touchUpdatedAtField(T entity) {
        for (ColumnField field : entityClassWrapper.getColumnFields()) {
            if (field.isUpdatedAt()) {
                if (field.getType() == DateTime.class) {
                    field.set(entity, DateTime.now());
                } else if (field.getType() == Date.class) {
                    field.set(entity, new Date());
                } // ignore else
                break;
            }
        }
    }

    public int update(T entity) {
        touchUpdatedAtField(entity);

        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(getTableName())
                .append(" set ");
        sql.append(buildSetsForUpdate(entity, false));
        sql.append(" where ").append(buildIdCondition());

        List<Object> parameters = buildParameters(entity, false);
        Object id = entityClassWrapper.getIdColumnField().get(entity);
        parameters.add(id);

        return jdbcTemplate.update(sql.toString(), parameters.toArray());
    }

    private String getTableName() {
        if (tableName != null) {
            return tableName;
        }
        return entityClassWrapper.getTableName();
    }

    public int update(T entity, String... properties) {
        if (properties == null || properties.length == 0) {
            throw new IllegalArgumentException("properties can't be empty");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(getTableName())
                .append(" set ");

        List<Object> parameters = new ArrayList<>();
        for (String field : properties) {
            ColumnField columnField = entityClassWrapper.getColumnField(field);
            sql.append(columnField.getColumnName()).append(" = ?,");
            parameters.add(columnField.getJdbcValue(entity));
        }
        sql.setLength(sql.length() - 1);

        sql.append(" where ").append(buildIdCondition());

        Object id = entityClassWrapper.getIdColumnField().get(entity);
        parameters.add(id);

        return jdbcTemplate.update(sql.toString(), parameters.toArray());
    }

    public int delete(T entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(getTableName());
        sql.append(" where ").append(buildIdCondition());

        Object id = entityClassWrapper.getIdColumnField().get(entity);
        return jdbcTemplate.update(sql.toString(), id);
    }

}
