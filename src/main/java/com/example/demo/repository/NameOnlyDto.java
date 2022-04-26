package com.example.demo.repository;

public class NameOnlyDto {
    private final String name;

    public NameOnlyDto(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}
