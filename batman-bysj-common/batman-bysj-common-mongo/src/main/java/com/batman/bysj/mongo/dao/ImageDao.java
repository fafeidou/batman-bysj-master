package com.batman.bysj.mongo.dao;

import com.batman.bysj.mongo.domain.ImageGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author victor.qin
 * @date 2018/7/13 15:55
 */
@Repository
public interface ImageDao extends MongoRepository<ImageGroup, String> {
}
