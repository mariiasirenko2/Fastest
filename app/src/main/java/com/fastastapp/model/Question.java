package com.fastastapp.model;

import java.util.Collection;
import java.util.Set;

public class Question {

    private int id;

    private String text;
    private Test test;
    private Collection<Answer> answers;
    private Set<VariantQuestion> variantQuestions;
}
