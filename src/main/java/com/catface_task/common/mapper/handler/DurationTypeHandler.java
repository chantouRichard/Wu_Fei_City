package com.catface_task.common.mapper.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class DurationTypeHandler extends BaseTypeHandler<Duration> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Duration parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public Duration getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String durationString = rs.getString(columnName);
        return durationString != null ? Duration.parse(durationString) : null;
    }

    @Override
    public Duration getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String durationString = rs.getString(columnIndex);
        return durationString != null ? Duration.parse(durationString) : null;
    }

    @Override
    public Duration getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String durationString = cs.getString(columnIndex);
        return durationString != null ? Duration.parse(durationString) : null;
    }
}
