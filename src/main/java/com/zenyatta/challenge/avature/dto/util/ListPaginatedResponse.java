package com.zenyatta.challenge.avature.dto.util;

import java.util.List;
import org.springframework.data.domain.Page;

public class ListPaginatedResponse<T> {

    public final List<T> data;
    public final PaginationResponse pagination;

    public ListPaginatedResponse(final List<T> data, final PaginationResponse pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    public ListPaginatedResponse(final Page<T> page) {
        data = page.stream().toList();
        pagination = PaginationResponse.fromPage(page);
    }
}