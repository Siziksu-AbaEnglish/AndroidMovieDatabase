package com.siziksu.tmdb.ui.utils;

import javax.inject.Inject;

public final class Pagination implements IPagination {

    private static final int FIRST_PAGE = 1;

    private int nextPage = FIRST_PAGE;
    private int totalPages = FIRST_PAGE;

    @Inject
    public Pagination() {}

    @Override
    public boolean shouldLoadMore() {
        return !(nextPage > FIRST_PAGE && nextPage > totalPages);
    }

    @Override
    public int getNextPage() {
        return nextPage;
    }

    @Override
    public void resetPagination() {
        nextPage = FIRST_PAGE;
        totalPages = FIRST_PAGE;
    }

    @Override
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        nextPage++;
    }

    @Override
    public boolean isNotFirstPage() {
        return nextPage > FIRST_PAGE;
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public void setRestoredNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    @Override
    public void setRestoredTotalPages(int currentPage) {
        this.totalPages = currentPage;
    }
}
