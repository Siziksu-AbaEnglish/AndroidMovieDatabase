package com.siziksu.tmdb.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.siziksu.tmdb.App;
import com.siziksu.tmdb.common.manager.ConnectionManager;
import com.siziksu.tmdb.common.manager.PreferencesManager;
import com.siziksu.tmdb.dagger.scope.AppScope;
import com.siziksu.tmdb.ui.utils.IPagination;
import com.siziksu.tmdb.ui.main.IMoviesAdapter;
import com.siziksu.tmdb.ui.utils.Pagination;
import com.siziksu.tmdb.ui.main.MoviesAdapter;
import com.siziksu.tmdb.ui.similar.ISimilarMoviesAdapter;
import com.siziksu.tmdb.ui.similar.SimilarMoviesAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {

    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    @Provides
    Context providesContext() {
        return application.getApplicationContext();
    }

    @Provides
    @AppScope
    SharedPreferences providesSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @AppScope
    PreferencesManager providesPreferencesManager(SharedPreferences sharedPreferences) {
        return new PreferencesManager(sharedPreferences);
    }

    @Provides
    @AppScope
    ConnectionManager providesConnectionManager(Context context) {
        return new ConnectionManager(context);
    }

    @Provides
    IPagination providesPagination(Pagination mainPagination) {
        return mainPagination;
    }

    @Provides
    IMoviesAdapter providesMoviesAdapter(MoviesAdapter adapter) {
        return adapter;
    }

    @Provides
    ISimilarMoviesAdapter providesSimilarMoviesAdapter(SimilarMoviesAdapter adapter) {
        return adapter;
    }
}
