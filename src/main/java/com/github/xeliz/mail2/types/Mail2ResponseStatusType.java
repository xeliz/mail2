package com.github.xeliz.mail2.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Mail2ResponseStatusType {
    OK("ok"),
    ERROR("error");

    private final String status;

    Mail2ResponseStatusType(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }
}
