package com.batman.bysj.common.model;

import java.io.Serializable;
import java.util.Date;



/**
 * Code配置表
 * 
 * @author victor.qin
 * @email xx@.com
 * @date 2018-05-25 10:08:03
 */
public class ComCode extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//Code
	private String code;
	//
	private String value1;
	//
	private String value2;
	//
	private String value3;
	//
	private String description;
	//是否有效
	private Integer active;
	//创建时间
	private Date createdTime;
	//
	private Integer creatorId;
	//修改时间
	private Date updatedTime;
	//
	private Integer updaterId;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：Code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：Code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：
	 */
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	/**
	 * 获取：
	 */
	public String getValue1() {
		return value1;
	}
	/**
	 * 设置：
	 */
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	/**
	 * 获取：
	 */
	public String getValue2() {
		return value2;
	}
	/**
	 * 设置：
	 */
	public void setValue3(String value3) {
		this.value3 = value3;
	}
	/**
	 * 获取：
	 */
	public String getValue3() {
		return value3;
	}
	/**
	 * 设置：
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：是否有效
	 */
	public void setActive(Integer active) {
		this.active = active;
	}
	/**
	 * 获取：是否有效
	 */
	public Integer getActive() {
		return active;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreatedTime() {
		return createdTime;
	}
	/**
	 * 设置：
	 */
	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * 获取：
	 */
	public Integer getCreatorId() {
		return creatorId;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdaterId(Integer updaterId) {
		this.updaterId = updaterId;
	}
	/**
	 * 获取：
	 */
	public Integer getUpdaterId() {
		return updaterId;
	}
}
