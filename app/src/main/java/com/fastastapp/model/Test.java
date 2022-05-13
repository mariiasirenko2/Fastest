package com.fastastapp.model;

import java.io.File;

public class Test {

    private int id;
    private String name;
   // private File questions;
  //  private File list_students;
    //private User owner;
   private int questions;
   private int list_students;

    public Test(String name) {
        this.name = name;
    }

  /*  public Test(String name, int questions, int list_students) {
        this.name = name;
        this.questions = questions;
        this.list_students = list_students;
    }*/

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuestions() {
        return questions;
    }

    public int getList_students() {
        return list_students;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public void setList_students(int list_students) {
        this.list_students = list_students;
    }
}
