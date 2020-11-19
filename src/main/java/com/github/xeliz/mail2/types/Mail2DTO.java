package com.github.xeliz.mail2.types;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mail2DTO {

    private String from;
    private List<String> to;
    private String data;

    public Mail2DTO() {
    }

    public Mail2DTO(String from, List<String> to, String data) {
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
