package com.github.xeliz.mail2.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.xeliz.mail2.entities.Mail2;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mail2ResponseDTO {

    // required
    private Mail2ResponseDTOStatus status;
    private String message;

    // optional
    private String token;
    private List<Mail2DTO> mails;

    public Mail2ResponseDTO() {
    }

    public Mail2ResponseDTO(Mail2ResponseDTOStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public Mail2ResponseDTO(Mail2ResponseDTOStatus status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }

    public Mail2ResponseDTO(Mail2ResponseDTOStatus status, String message, List<Mail2DTO> mails) {
        this.status = status;
        this.message = message;
        this.mails = mails;
    }

    public Mail2ResponseDTOStatus getStatus() {
        return status;
    }

    public void setStatus(Mail2ResponseDTOStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Mail2DTO> getMails() {
        return mails;
    }

    public void setMails(List<Mail2DTO> mails) {
        this.mails = mails;
    }

    public enum Mail2ResponseDTOStatus {
        OK("ok"),
        ERROR("error");

        private final String status;

        Mail2ResponseDTOStatus(String status) {
            this.status = status;
        }

        @JsonValue
        public String getStatus() {
            return status;
        }
    }
}
