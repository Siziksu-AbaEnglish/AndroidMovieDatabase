package com.siziksu.tmdb.dagger.module;

import com.siziksu.tmdb.domain.movies.IMovieDatabaseRequests;
import com.siziksu.tmdb.domain.movies.MovieDatabaseRequests;

import dagger.Module;
import dagger.Provides;

@Module
public final class RequestModule {

    public RequestModule() {}

    @Provides
    IMovieDatabaseRequests providesMovieDatabaseRequests(MovieDatabaseRequests request) {
        return request;
    }
}
