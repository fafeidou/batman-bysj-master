package com.batman.bysj.common.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class Test extends BaseEntity implements Serializable{
	private  String name ;
}
