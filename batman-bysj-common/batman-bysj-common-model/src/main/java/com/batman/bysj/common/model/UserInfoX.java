package com.batman.bysj.common.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author victor.qin
 * @date 2018/6/23 14:52
 */
@ToString(callSuper = true)
@Data
@Table(name = "user")
public class UserInfoX extends BaseEntity implements Serializable {
    private String userName;
    private String address;
    private String password;
}
