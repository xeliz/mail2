package com.github.xeliz.mail2.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Mail2RequestActionType {
    SEND("send"),
    RECEIVE("receive"),
    AUTH("auth");

    private final String actionType;

    Mail2RequestActionType(String actionType) {
        this.actionType = actionType;
    }

    @JsonValue
    public String getActionType() {
        return actionType;
    }
}
