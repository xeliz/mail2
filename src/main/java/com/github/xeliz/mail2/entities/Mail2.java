package com.github.xeliz.mail2.entities;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "MAIL2S")
@SecondaryTable(name = "MAIL2_RECEIVERS", pkJoinColumns = @PrimaryKeyJoinColumn(name = "ID"))
public class Mail2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "SENDER")
    private String from;

    @ElementCollection
    @CollectionTable(name = "MAIL2_RECEIVERS", joinColumns = @JoinColumn(name = "MAIL2_ID"))
    @Column(name = "RECEIVER")
    private List<String> to;

    @Column(name = "DATA")
    private String data;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Mail2Status status;

    public Mail2() {
    }

    public Mail2(String from, List<String> to, String data, Mail2Status status) {
        this.from = from;
        this.to = to;
        this.data = data;
        this.status = status;
    }

    public Long getId() {
        return id;
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

    public enum Mail2Status {
        PENDING("pending"),
        DELIVERED("delivered"),
        ERROR("error");

        private String status;

        Mail2Status(final String status) {
            this.status = status;
        }

        @JsonValue
        public String getStatus() {
            return status;
        }
    }
}
