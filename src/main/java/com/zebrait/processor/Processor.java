package com.zebrait.processor;

public abstract class Processor {
    public abstract String getData();
    public void process(){
        String data=getData();
    }
}
