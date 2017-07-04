package com.siziksu.tmdb.presenter.main;

import com.siziksu.tmdb.common.model.response.movies.Movie;
import com.siziksu.tmdb.presenter.BasePresenter;

public abstract class IMainPresenter extends BasePresenter<IMainView> {

    public abstract void getConfigurationAndMovies();

    public abstract void getMoviesFromSwipeRefresh();

    public abstract void getMovies();

    public abstract void getMovies(int page);

    public abstract void getFilteredMovies(String text, int page);

    public abstract void showMovieInfo(Movie movie);
}
