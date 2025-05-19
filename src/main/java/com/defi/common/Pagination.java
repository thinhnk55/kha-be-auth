package com.defi.common;

import org.springframework.data.domain.Pageable;

public record Pagination(int page, int size, long total){
    public static  Pagination of(int page, int size, int total) {
        return new Pagination(page, size, total);
    }

    public static  Pagination of(int page, int size) {
        return new Pagination(page, size, 0);
    }
    public static Pagination of(Pageable pageable) {
        return Pagination.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }
}
