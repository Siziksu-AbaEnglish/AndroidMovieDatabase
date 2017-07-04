package com.siziksu.tmdb.ui.utils;

public interface IPagination {

    boolean shouldLoadMore();

    int getNextPage();

    void resetPagination();

    void setTotalPages(int totalPages);

    boolean isNotFirstPage();

    int getTotalPages();

    void setRestoredNextPage(int nextPage);

    void setRestoredTotalPages(int currentPage);
}
