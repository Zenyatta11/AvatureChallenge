package com.zenyatta.challenge.avature.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public final class Logging {

    private Logging() {
    }

    public static List<String> getErroredFields(final MethodArgumentNotValidException exception) {
        final BindingResult bindingResult = exception.getBindingResult();
        final List<String> missingFields = new ArrayList<>();

        for (final FieldError fieldError : bindingResult.getFieldErrors()) {
            missingFields.add(fieldError.getField());
        }

        return missingFields;
    }

}
