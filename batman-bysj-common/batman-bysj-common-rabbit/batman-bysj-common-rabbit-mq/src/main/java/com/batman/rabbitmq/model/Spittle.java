package com.batman.rabbitmq.model;

import java.io.Serializable;

/**
 * @author victor.qin
 * @date 2018/9/4 14:49
 */
public class Spittle implements Serializable {
    private int id;

    private String message;

    public Spittle() {
    }

    public Spittle(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;

    }

    @Override
    public String toString() {
        return "Spittle{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
