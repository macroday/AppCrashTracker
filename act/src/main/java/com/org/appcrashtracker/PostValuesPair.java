package com.org.appcrashtracker;

public class PostValuesPair {
    private String key = "";
    private String value = "";

    public PostValuesPair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}
