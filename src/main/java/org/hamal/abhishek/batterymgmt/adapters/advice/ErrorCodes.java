package org.hamal.abhishek.batterymgmt.adapters.advice;

import lombok.Getter;

@Getter
public enum ErrorCodes {

    BAD_REQUEST("001", "Bad Request"),
    INTERNAL_ERROR("999", "Something went wrong");

    private String code;
    private String description;

    ErrorCodes(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCodeAndDescription() {
        return String.format("%s (%s)", code, description);
    }

}
