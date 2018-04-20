package com.batman.bysj.common.mail.batmanbysjcommonmail;

import java.util.ArrayList;
import java.util.List;

/**
* 邮件信息模型，记录各种邮件信息
* @author victor.qin 
* @date 2018/4/20 15:21
*/ 
public class Mail {
    private String content;
    private String contentType = "text/html;charset=GB2312";
    private Priority priority = Priority.Normal;
    private String sender;
    private List<String> receivers;
    private String subject;
    private List<String> attachmentPaths;

    public String getContent() {
        return content;
    }

    public Mail setContent(String content) {
        this.content = content;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public Mail setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Priority getPriority() {
        return priority;
    }

    public Mail setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public String getSender() {
        return sender;
    }

    public Mail setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public Mail setReceivers(List<String> receivers) {
        this.receivers = receivers;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Mail setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }

    public Mail setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
        return this;
    }

    public Mail() {
    }

    public Mail(String sender) {
        this.sender = sender;
    }

    public Mail(String sender, String receiver) {
        this.sender = sender;
        this.receivers = new ArrayList<>();
        this.receivers.add(receiver);
    }

    public Mail(List<String> receivers, String subject, String content) {
        this.content = content;
        this.receivers = receivers;
        this.subject = subject;
    }
}
