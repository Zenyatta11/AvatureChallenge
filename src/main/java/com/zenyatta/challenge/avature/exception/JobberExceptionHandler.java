package com.zenyatta.challenge.avature.exception;

import com.zenyatta.challenge.avature.util.Logging;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.webjars.NotFoundException;

@RestControllerAdvice
@Slf4j
public class JobberExceptionHandler {

    private static final String EXCEPTION_ARGUMENT_MISSING = "The following arguments cannot be null: ";

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException() {
        if (log.isErrorEnabled()) {
            log.error("Not found.");
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Not Found");
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentConversionNotSupportedException.class })
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
            final Exception exception) {
        if (log.isErrorEnabled()) {
            log.error(exception.getMessage(), exception);
        }

        String returnMessage = MDC.get("message");

        if (exception instanceof MethodArgumentNotValidException && returnMessage == null) {
            returnMessage = getMissingFields((MethodArgumentNotValidException) exception);
        }

        if (returnMessage == null) {
            returnMessage = "Argument mismatch.";
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(returnMessage);
    }

    @ExceptionHandler(JobberException.class)
    protected ResponseEntity<Object> handleJobberException(final JobberException exception) {
        final HttpStatusCode code = exception.getStatusCode();

        if (log.isErrorEnabled()) {
            MDC.put("exception", exception.getMessage());

            String description = "Information";

            if (code.is2xxSuccessful()) {
                description = "Success";
            } else if (code.is3xxRedirection()) {
                description = "Redirect";
            } else if (code.is4xxClientError()) {
                description = "Client Error";
            } else if (code.is5xxServerError()) {
                description = "Server Error";
            }

            log.error(description, exception);
        }

        return ResponseEntity
                .status(code)
                .body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleFallbackExceptions(final Exception exception) {
        if (log.isErrorEnabled()) {
            MDC.put("exception", exception.getMessage());
            log.error("Internal server error", exception);
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    private String getMissingFields(final MethodArgumentNotValidException exception) {
        final List<String> missingFields = Logging.getErroredFields((MethodArgumentNotValidException) exception);
        if (!missingFields.isEmpty()) {
            if (log.isErrorEnabled()) {
                log.error(EXCEPTION_ARGUMENT_MISSING + missingFields.toString());
            }

            return EXCEPTION_ARGUMENT_MISSING + missingFields.toString();
        }

        return null;
    }
}
