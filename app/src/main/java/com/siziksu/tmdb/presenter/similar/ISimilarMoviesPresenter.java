package com.siziksu.tmdb.presenter.similar;

import com.siziksu.tmdb.common.model.response.movies.Movie;
import com.siziksu.tmdb.presenter.BasePresenter;

public abstract class ISimilarMoviesPresenter extends BasePresenter<ISimilarMoviesView> {

    public abstract void getSimilarMovies(Movie movie);

    public abstract void getSimilarMovies(Movie movie, int page);

    public abstract void tutorialCompleted();

    public abstract boolean isTutorialCompleted();
}
