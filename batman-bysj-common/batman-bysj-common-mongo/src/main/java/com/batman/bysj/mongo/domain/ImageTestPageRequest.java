package com.batman.bysj.mongo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Administrator
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageTestPageRequest extends Page {
    private String title;
}
