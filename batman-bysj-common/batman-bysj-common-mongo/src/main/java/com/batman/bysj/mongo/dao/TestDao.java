package com.batman.bysj.mongo.dao;

import com.batman.bysj.mongo.domain.TestModel;
import com.mongodb.WriteResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * @author victor.qin
 * @date 2018/5/21 18:38
 */
@Repository
public class TestDao extends BaseMongoDao<TestModel> {

    public List<Map> getProperties() {
        Query query = newActiveQuery(true);
        return mongoTemplate.find(query, Map.class);
    }

    public TestModel getPropertie(String key) {
        Query query = newActiveQuery(true).addCriteria(Criteria.where("name").is(key));
        return findOne(query);
    }

    public Map<String, TestModel> getPropertiesMap(final Collection<String> keys) {
        Query query = newActiveQuery(true).addCriteria(Criteria.where("name").in(keys));
        return find(query).stream().collect(toMap(TestModel::getName, Function.identity(), (key1, key2) -> key2));
    }

    public Map<String, TestModel> getAllPropertiesMap() {
        Query query = newActiveQuery(false);
        return find(query).stream().collect(toMap(TestModel::getName, Function.identity(), (key1, key2) -> key2));
    }

    /**
     * 分页查询
     */
    public Page<TestModel> findByPage(final String name, PageRequest request) {
        Query query = newActiveQuery(false);
        if (StringUtils.isNotEmpty(name)) {
            query.addCriteria(Criteria.where("title").regex(name.trim(), "ig"));
        }
        return findPage(query, request);
    }

    public void deleteAll() {
        Query query = newActiveQuery(false);
        WriteResult delete = delete(query);
    }
}
