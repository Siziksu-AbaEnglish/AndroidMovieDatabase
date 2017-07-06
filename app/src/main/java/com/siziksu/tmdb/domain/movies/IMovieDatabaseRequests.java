package com.siziksu.tmdb.domain.movies;

import com.siziksu.tmdb.common.model.response.configuration.Configuration;
import com.siziksu.tmdb.common.model.response.movies.Movies;

import io.reactivex.Single;

public interface IMovieDatabaseRequests {

    Single<Configuration> getConfiguration();

    Single<Movies> getMovies(int page, String text, boolean includeAdult);

    Single<Movies> getSimilarMovies(int movieId, int page, boolean includeAdult);
}
