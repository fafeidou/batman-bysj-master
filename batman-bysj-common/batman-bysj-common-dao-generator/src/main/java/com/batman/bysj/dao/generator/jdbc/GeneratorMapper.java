package com.batman.bysj.dao.generator.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 查询数据库
 *
 * @author victor.qin
 * @date 2018/5/24 16:12
 */
@Service
public class GeneratorMapper {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> list() {
        return jdbcTemplate.queryForList("select table_name tableName, engine, table_comment tableComment, create_time" +
                " createTime from information_schema.tables" + " where table_schema = (select database())");
    }

    public int count() {
        return jdbcTemplate.queryForList("select count(*) from information_schema.tables where " +
                "table_schema = (select database())").size();
    }

    public Map<String, String> get(String tableName) {
        Map<String, Object> map = jdbcTemplate.queryForList("select table_name tableName, engine, table_comment " +
                " tableComment, create_time createTime from information_schema.tables " +
                " where table_schema = (select database()) and table_name = ?", tableName).get(0);
        Map<String, String> result1 = getStringMaps(map);
        if (result1 != null) {
            return result1;
        }
        return null;
    }

    public List<Map<String, Object>> listColumns(String tableName) {
        return jdbcTemplate.queryForList("select column_name columnName, data_type dataType, column_comment " +
                "columnComment, column_key columnKey, extra from information_schema.columns" +
                " where table_name = ? and table_schema = (select database()) order by ordinal_position",tableName);
    }

    private Map<String, String> getStringMaps(Map<String, Object> map) {
        Map<String, String> result = new HashMap<>();
        if (!CollectionUtils.isEmpty(map)) {
            Set<String> keys = map.keySet();
            for (String key : keys) {
                result.put(key, map.get(key).toString());
            }
            return result;
        }
        return null;
    }
}
