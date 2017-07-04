package com.siziksu.tmdb;

import com.siziksu.tmdb.ui.utils.Pagination;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public final class PaginationTests {

    private Pagination mainPagination;

    @Before
    public void init() {
        mainPagination = new Pagination();
    }

    @Test
    public void restore() throws Exception {
        mainPagination.resetPagination();
        mainPagination.setRestoredTotalPages(30);
        mainPagination.setRestoredNextPage(3);
        mainPagination.resetPagination();
        assertEquals(1, mainPagination.getNextPage());
    }

    @Test
    public void getNextPageIsFirstPage() throws Exception {
        mainPagination.resetPagination();
        assertEquals(1, mainPagination.getNextPage());
    }

    @Test
    public void getNextPageNotFirstPage() throws Exception {
        mainPagination.resetPagination();
        mainPagination.setTotalPages(30);
        assertNotEquals(1, mainPagination.getNextPage());
    }

    @Test
    public void getNextPageIsSecondPage() throws Exception {
        mainPagination.resetPagination();
        mainPagination.setTotalPages(30);
        assertEquals(2, mainPagination.getNextPage());
    }

    @Test
    public void getNextPageIsFourthPage() throws Exception {
        mainPagination.resetPagination();
        mainPagination.setTotalPages(30);
        mainPagination.setTotalPages(30);
        mainPagination.setTotalPages(30);
        assertEquals(4, mainPagination.getNextPage());
    }

    @Test
    public void shouldLoadMore() throws Exception {
        mainPagination.resetPagination();
        mainPagination.setRestoredTotalPages(2);
        mainPagination.setRestoredNextPage(3);
        assertEquals(false, mainPagination.shouldLoadMore());
    }

    @Test
    public void isNotFirstPage() throws Exception {
        mainPagination.resetPagination();
        mainPagination.setRestoredTotalPages(30);
        mainPagination.setRestoredNextPage(3);
        assertEquals(true, mainPagination.isNotFirstPage());
    }

    @Test
    public void getTotalPages() throws Exception {
        mainPagination.resetPagination();
        mainPagination.setRestoredTotalPages(30);
        mainPagination.setRestoredNextPage(3);
        assertEquals(30, mainPagination.getTotalPages());
    }
}