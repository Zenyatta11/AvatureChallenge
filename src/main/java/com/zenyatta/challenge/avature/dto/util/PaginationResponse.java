package com.zenyatta.challenge.avature.dto.util;

import org.springframework.data.domain.Page;

public record PaginationResponse(
        Long totalElements,
        Integer pageNumber,
        Integer pageSize,
        Integer totalPages) {

    public static PaginationResponse fromPage(final Page<?> page) {
        return new PaginationResponse(
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages());
    }
}