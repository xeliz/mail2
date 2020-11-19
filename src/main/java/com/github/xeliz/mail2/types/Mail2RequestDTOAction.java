package com.github.xeliz.mail2.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Mail2RequestDTOAction {
    SEND("send"),
    RECEIVE("receive"),
    AUTH("auth");

    private final String actionType;

    Mail2RequestDTOAction(String actionType) {
        this.actionType = actionType;
    }

    @JsonValue
    public String getActionType() {
        return actionType;
    }
}
