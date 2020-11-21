package com.github.xeliz.mail2.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.xeliz.mail2.entities.Mail2;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mail2DTO {

    private String from;
    private List<String> to;
    private String data;
    private Mail2.Mail2Status mail2Status;

    public Mail2DTO() {
    }

    public Mail2DTO(String from, List<String> to, String data, Mail2.Mail2Status mail2Status) {
        this.from = from;
        this.to = to;
        this.data = data;
        this.mail2Status = mail2Status;
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

    public Mail2.Mail2Status getMail2DTOStatus() {
        return mail2Status;
    }

    public void setMail2DTOStatus(Mail2.Mail2Status mail2DTOStatus) {
        this.mail2Status = mail2DTOStatus;
    }
}
