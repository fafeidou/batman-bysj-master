package com.batman.bysj.common.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * @author victor.qin
 * @date 2018/8/11 16:16
 */
@ToString(callSuper = true)
@Data
@Table(name = "role")
public class Role {
    private Integer id;
    private String role;
}
