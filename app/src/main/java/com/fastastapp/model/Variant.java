package com.fastastapp.model;

public class Variant {
     private  int id;
     private String fullName;
     private int mark;

    public Variant(String fullName, int mark) {
        this.fullName = fullName;
        this.mark = mark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
