package com.github.xeliz.mail2.entities;

import java.util.List;

public class Mail2 {
    private String from;
    private List<String> to;
    private String data;
    private Mail2Status status;

    public Mail2() {
    }

    public Mail2(String from, List<String> to, String data, Mail2Status status) {
        this.from = from;
        this.to = to;
        this.data = data;
        this.status = status;
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

    public Mail2Status getStatus() {
        return status;
    }

    public void setStatus(Mail2Status status) {
        this.status = status;
    }
}
