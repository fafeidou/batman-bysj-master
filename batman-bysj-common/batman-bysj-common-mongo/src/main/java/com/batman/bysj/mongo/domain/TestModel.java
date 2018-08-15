package com.batman.bysj.mongo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author victor.qin
 * @date 2018/5/21 18:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "test")
public class TestModel extends BaseMongoModel {
    private String name;
}
