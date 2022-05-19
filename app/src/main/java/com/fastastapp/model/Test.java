package com.fastastapp.model;

import java.util.Collection;

public class Test {


    private int id;
    private String name;
    private String[] students;
    private Collection<Question> questions;
    private User owner;

    public Test(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
