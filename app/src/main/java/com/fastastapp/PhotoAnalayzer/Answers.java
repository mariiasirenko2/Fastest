package com.fastastapp.PhotoAnalayzer;

import java.util.HashMap;
import java.util.Map;

public enum Answers {
    EMPTY(0),
    A(1),
    B(2),
    C(3),
    D(4);
    private int value;
    private static Map map = new HashMap<>();

    private Answers(int value) {
        this.value = value;
    }

    static {
        for (Answers answer : Answers.values()) {
            map.put(answer.value, answer);
        }
    }
    public static Answers valueOf(int answer) {
        return (Answers) map.get(answer);
    }
}
