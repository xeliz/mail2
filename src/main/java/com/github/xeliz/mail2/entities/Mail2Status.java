package com.github.xeliz.mail2.entities;

import com.fasterxml.jackson.annotation.JsonValue;

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
