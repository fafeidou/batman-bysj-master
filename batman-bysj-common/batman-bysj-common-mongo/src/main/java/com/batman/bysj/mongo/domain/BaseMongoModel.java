package com.batman.bysj.mongo.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;

import java.util.Date;

/**
 * @author victor.qin
 * @date 2018/5/21 18:25
 */
public class BaseMongoModel {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;

    @CreatedBy
    private String creator;

    @CreatedDate
    private Date created;

    @LastModifiedDate
    private Date modified;

    @LastModifiedBy
    private String modifier;

    private Integer active = 1;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
