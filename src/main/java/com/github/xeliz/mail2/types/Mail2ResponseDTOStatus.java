package com.github.xeliz.mail2.types;

import com.fasterxml.jackson.annotation.JsonValue;

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
