package com.github.xeliz.mail2.entities;

import java.util.List;

public class Mail2 {
    private String from;
    private List<String> to;
    private String data;

    public Mail2() {
    }

    public Mail2(String from, List<String> to, String data) {
        this.from = from;
        this.to = to;
        this.data = data;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
