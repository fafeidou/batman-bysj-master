package com.batman.bysj.mongo.domain;

import com.batman.bysj.mongo.domain.BaseMongoModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author victor.qin
 * @date 2018/5/21 18:38
 */
@Data
@Document(collection = "test")
public class TestModel extends BaseMongoModel {
    private String name;
}
