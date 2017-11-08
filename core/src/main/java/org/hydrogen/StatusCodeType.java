package org.hydrogen;

import java.util.Arrays;

public enum StatusCodeType {
    INFORMATION(100),
    SUCCESSFUL_RESPONSE(200),
    REDIRECTION_MESSAGE(300),
    CLIENT_ERROR(400),
    SERVER_ERROR(500);

    private final int rangeStart;

    StatusCodeType(int rangeStart) {
        this.rangeStart = rangeStart;
    }

    public static StatusCodeType forCode(int code) {
        return Arrays.stream(values())
                .filter(type -> code >= type.rangeStart && code < type.rangeStart + 100)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid code " + code + "."));
    }
}
