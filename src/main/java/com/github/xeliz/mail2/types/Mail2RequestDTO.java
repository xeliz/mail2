package com.github.xeliz.mail2.types;

public class Mail2RequestDTO {

    private Mail2RequestActionType action;
    private Mail2RequestBodyDTO body;

    public Mail2RequestDTO() {
    }

    public Mail2RequestDTO(Mail2RequestActionType action, Mail2RequestBodyDTO body) {
        this.action = action;
        this.body = body;
    }

    public Mail2RequestActionType getAction() {
        return action;
    }

    public void setAction(Mail2RequestActionType action) {
        this.action = action;
    }

    public Mail2RequestBodyDTO getBody() {
        return body;
    }

    public void setBody(Mail2RequestBodyDTO body) {
        this.body = body;
    }
}
