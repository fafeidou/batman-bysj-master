package com.batman.bysj.mongo.domain;

import lombok.Data;

@Data
public class ImageModel {
    //url
    private String imageUrl;
    //图片排序
    private int imageSort;
    //图片名称
    private String imageName;
}
