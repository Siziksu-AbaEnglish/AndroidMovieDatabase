package com.siziksu.tmdb.dagger.module;

import com.siziksu.tmdb.presenter.main.IMainPresenter;
import com.siziksu.tmdb.presenter.main.MainPresenter;
import com.siziksu.tmdb.presenter.similar.ISimilarMoviesPresenter;
import com.siziksu.tmdb.presenter.similar.SimilarMoviesPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public final class PresenterModule {

    public PresenterModule() {}

    @Provides
    IMainPresenter providesMainPresenter(MainPresenter presenter) {
        return presenter;
    }

    @Provides
    ISimilarMoviesPresenter providesSimilarMoviesPresenter(SimilarMoviesPresenter presenter) {
        return presenter;
    }
}
