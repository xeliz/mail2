package com.github.xeliz.mail2.entities;

import javax.persistence.*;

@Entity
@Table(name = "MAIL2_TOKENS")
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "TOKEN")
    private String token;

    public AccessToken() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
