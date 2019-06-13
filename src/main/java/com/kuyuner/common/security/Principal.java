package com.kuyuner.common.security;

import java.io.Serializable;

/**
 * 用户授权信息
 *
 * @author tangzj
 */
public class Principal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String name;

    public Principal() {
    }

    public Principal(String id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return id;
    }

}