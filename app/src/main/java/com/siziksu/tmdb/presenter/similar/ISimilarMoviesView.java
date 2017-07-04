package com.siziksu.tmdb.presenter.similar;

import com.siziksu.tmdb.common.model.response.movies.Movie;
import com.siziksu.tmdb.presenter.IBaseView;

import java.util.List;

public interface ISimilarMoviesView extends IBaseView {

    void showMovies(List<Movie> movies, int totalPages);
}
