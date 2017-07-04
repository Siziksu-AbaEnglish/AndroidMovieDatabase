package com.siziksu.tmdb.domain.movies;

import android.text.TextUtils;

import com.siziksu.tmdb.BuildConfig;
import com.siziksu.tmdb.common.model.response.configuration.Configuration;
import com.siziksu.tmdb.common.model.response.movies.Movies;
import com.siziksu.tmdb.data.movies.MovieDatabaseClientService;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public final class MovieDatabaseRequests implements IMovieDatabaseRequests {

    @Inject
    MovieDatabaseClientService service;

    @Inject
    MovieDatabaseRequests() {}

    @Override
    public Observable<Configuration> getConfiguration() {
        return service.getConfiguration(BuildConfig.API_PUBLIC_KEY)
                      .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Movies> getMovies(int page, String text, boolean includeAdult) {
        if (TextUtils.isEmpty(text)) {
            return service.getTopRatedMovies(page, includeAdult, BuildConfig.API_PUBLIC_KEY)
                          .subscribeOn(Schedulers.io());
        } else {
            return service.searchMovieByName(page, text, includeAdult, BuildConfig.API_PUBLIC_KEY)
                          .subscribeOn(Schedulers.io());
        }
    }

    @Override
    public Observable<Movies> getSimilarMovies(int movieId, int page, boolean includeAdult) {
        return service.getSimilarMovies(movieId, page, includeAdult, BuildConfig.API_PUBLIC_KEY)
                      .subscribeOn(Schedulers.io());
    }
}
