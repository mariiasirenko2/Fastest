package com.fastastapp.model;

import java.util.Set;

public class Variant {
     private  int id;
     private String studentName;
     private int mark;
    private Set<VariantQuestion> variantQuestions;

    public Variant(String fullName, int mark) {
        this.studentName = fullName;
        this.mark = mark;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return studentName;
    }

    public void setFullName(String fullName) {
        this.studentName = fullName;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
