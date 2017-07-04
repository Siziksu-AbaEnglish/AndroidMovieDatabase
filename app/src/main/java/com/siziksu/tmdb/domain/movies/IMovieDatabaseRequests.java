package com.siziksu.tmdb.domain.movies;

import com.siziksu.tmdb.common.model.response.configuration.Configuration;
import com.siziksu.tmdb.common.model.response.movies.Movies;

import io.reactivex.Observable;

public interface IMovieDatabaseRequests {

    Observable<Configuration> getConfiguration();

    Observable<Movies> getMovies(int page, String text, boolean includeAdult);

    Observable<Movies> getSimilarMovies(int movieId, int page, boolean includeAdult);
}
