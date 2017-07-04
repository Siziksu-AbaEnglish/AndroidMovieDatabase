package com.siziksu.tmdb.presenter.main;

import com.siziksu.tmdb.common.model.response.movies.Movie;
import com.siziksu.tmdb.presenter.IBaseView;

import java.util.List;

public interface IMainView extends IBaseView {

    void showMovies(List<Movie> movies, boolean filtered, int totalPages);

    void stopRefreshing();
}
