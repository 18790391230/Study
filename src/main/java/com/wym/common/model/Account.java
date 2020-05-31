package com.wym.common.model;

import lombok.Builder;

@Builder
public class Account {

    public Account() {
    }

    public Account(Integer id, String name, Integer userId, Long money) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.money = money;
    }

    private Integer id;

    private String name;

    private Integer userId;

    private Long money;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }
}