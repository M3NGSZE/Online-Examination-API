package com.m3ngsze.sentry.onlineexaminationapi.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationResponse {
    private Long totalItems;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;

    private boolean hasNextPage;
    private boolean hasPreviousPage;

    private Integer nextPage;
    private Integer previousPage;
    private Integer firstPage;
    private Integer lastPage;

    public static PaginationResponse of(Long totalItems, int page, int size) {

        int totalPages = (int) Math.ceil((double) totalItems / size);
        int currentPage = Math.max(1, Math.min(page, Math.max(totalPages, 1)));

        boolean hasNext = currentPage < totalPages;
        boolean hasPrev = currentPage > 1;

        return PaginationResponse.builder()
                .totalItems(totalItems)
                .pageSize(size)
                .totalPages(totalPages)
                .currentPage(currentPage)
                .hasNextPage(hasNext)
                .hasPreviousPage(hasPrev)
                .nextPage(hasNext ? currentPage + 1 : null)
                .previousPage(hasPrev ? currentPage - 1 : null)
                .firstPage(totalPages > 0 ? 1 : null)
                .lastPage(totalPages > 0 ? totalPages : null)
                .build();
    }
}
