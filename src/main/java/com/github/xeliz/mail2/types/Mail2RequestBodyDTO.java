package com.github.xeliz.mail2.types;

import java.util.List;

public class Mail2RequestBodyDTO {

    // send, receive, validate_token
    private String token;

    // send
    private String from;
    private List<String> to;
    private String data;

    // auth, validate_token
    private String address;

    // auth
    private String password;

    public Mail2RequestBodyDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
