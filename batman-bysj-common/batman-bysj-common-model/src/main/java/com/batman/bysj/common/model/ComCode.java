package com.batman.bysj.common.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author victor.qin
 * @date 2018/5/23 17:52
 */
@Data
@ToString(callSuper = true)
public class ComCode extends BaseEntity implements Serializable {
    /**
     * Code
     */
    protected String code;

    protected String value1;

    protected String value2;

    protected String value3;

    protected String description;

    /**
     * 是否有效
     */
    protected Boolean active;


}
