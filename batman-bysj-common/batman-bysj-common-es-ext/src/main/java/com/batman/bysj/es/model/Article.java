package com.batman.bysj.es.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @author victor.qin
 * @date 2018/8/27 13:04
 */
@Document(indexName = "projectname", type = "article", indexStoreType = "fs", shards = 5, replicas = 1, refreshInterval = "-1")
public class Article implements Serializable {
    @Id
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 摘要
     */
    private String abstracts;
    /**
     * 内容
     */
    private String content;
    /**
     * 发表时间
     */
    private Date postTime;
    /**
     * 点击率
     */
    private Long clickCount;
    private Author author;
    private Tutorial tutorial;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Tutorial getTutorial() {
        return tutorial;
    }

    public void setTutorial(Tutorial tutorial) {
        this.tutorial = tutorial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }
}
