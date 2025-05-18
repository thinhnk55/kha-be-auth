package com.defi.auth.common;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {


    private Pagination pagination;
    private List<T> data;


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pagination {
        private int page;
        private int size;
        private long total;
    }
}
