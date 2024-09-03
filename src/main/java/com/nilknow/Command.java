package com.nilknow;

import java.util.List;

public class Command {
    public String type;
    public String key;
    public String value;
    public List<String> args;
    public Object response;

    public Command(String type, String key, String value, List<String> args) {
        this.type = type;
        this.key = key;
        this.value = value;
        this.args = args;
    }
}