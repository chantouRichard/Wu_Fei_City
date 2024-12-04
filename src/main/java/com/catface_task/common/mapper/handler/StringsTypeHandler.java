package com.catface_task.common.mapper.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description: 对用 Tags：String[] 类型和数据库之间转换的方式。
 */
public class StringsTypeHandler extends BaseTypeHandler<String[]> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, String.join(",", parameter));
    }

    @Override
    public String[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String tagsStr = rs.getString(columnName);
        return tagsStr != null ? tagsStr.split(",") : new String[0];
    }

    @Override
    public String[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String tagsStr = rs.getString(columnIndex);
        return tagsStr != null ? tagsStr.split(",") : new String[0];
    }

    @Override
    public String[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String tagsStr = cs.getString(columnIndex);
        return tagsStr != null ? tagsStr.split(",") : new String[0];
    }
}
