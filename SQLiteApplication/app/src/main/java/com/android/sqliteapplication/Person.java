package com.android.sqliteapplication;

public abstract class Person {
    private final String name;
    private final int age;
    public Person(String name, int age){
        this.age = age;
        this.name = name;
    }
}
