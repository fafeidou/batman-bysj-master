package com.batman.bysj.common.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * @author victor.qin
 * @date 2018/8/11 16:18
 */
@ToString(callSuper = true)
@Data
@Table(name = "user_role")
public class UserRole {
    private Integer id;
    private Integer userId;
    private Integer roleId;
}
