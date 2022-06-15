package com.fastastapp.model;

import java.util.Collection;

public class Test {


    private int id;
    private String testName;
    private String[] students;
    private User owner;
    private Collection<Question> questions;

    public Test(String testName, User owner) {
        this.testName = testName;
        this.owner = owner;
    }
    public int getId() {
        return id;
    }


    public String getName() {
        return testName;
    }

    public void setName(String name) {
        this.testName = name;
    }
}
