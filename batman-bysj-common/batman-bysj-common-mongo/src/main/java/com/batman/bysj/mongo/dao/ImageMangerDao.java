package com.batman.bysj.mongo.dao;

import com.batman.bysj.mongo.domain.ImageGroup;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageMangerDao extends BaseMongoDao<ImageGroup> {
    public void testQuery() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.ASC, "sort"));
        query.with(new PageRequest(0,10));
        query.addCriteria(Criteria.where("description").is("haha").regex(".*?" + "hah" + ".*"));
        List<ImageGroup> imageGroups = mongoTemplate.find(query, ImageGroup.class);
    }
}
