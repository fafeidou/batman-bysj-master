package com.batman.bysj.common.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author victor.qin
 * @date 2018/8/11 16:16
 */
@ToString(callSuper = true)
@Data
@Table(name = "role")
public class Role extends BaseEntity implements Serializable {
    private Integer id;
    private String role;
}
