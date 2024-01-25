package com.zenyatta.challenge.avature.dto;

public record CommandResponse(
        Long jobId,
        ResponseStatus responseStatus,
        String errorMessage) {
    public static CommandResponse buildJobResponse(
            final Long jobId,
            final ResponseStatus status,
            final String errorMessage) {
        return new CommandResponse(jobId, status, errorMessage);
    }
}
