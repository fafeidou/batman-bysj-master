package com.batman.bysj.common.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author victor.qin
 * @date 2018/8/22 19:56
 */
@ToString(callSuper = true)
@Data
@Table(name = "role_menu")
public class RoleMenu extends BaseEntity implements Serializable {
    private Integer roleId;
    private Integer roleMenu;
}
