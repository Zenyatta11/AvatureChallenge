package com.zenyatta.challenge.avature.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatusCode;

public class JobberException extends Exception {
    @Getter
    private final HttpStatusCode statusCode;

    public JobberException(final HttpStatusCode statusCode, @NonNull final String errorMessage) {
        super(errorMessage);
        this.statusCode = statusCode;
    }
}
