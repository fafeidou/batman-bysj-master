package com.batman.bysj.mongo.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片管理的model
 */
@Data
@Document(collection = "image_group")
public class ImageGroup extends BaseMongoModel{
    //封面图片
    private String coverImage;
    //图片标签
    private String title;
    //分组状态， tru表示小程序可以显示
    private boolean status;
    //图片内容
    private List<ImageModel> imageList = new ArrayList<ImageModel>();
    //图片描述
    private String description;
    //图片排序
    private int sort;

}
