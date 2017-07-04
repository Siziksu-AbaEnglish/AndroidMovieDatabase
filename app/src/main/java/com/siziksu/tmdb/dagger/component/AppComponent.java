package com.siziksu.tmdb.dagger.component;

import com.siziksu.tmdb.dagger.module.AppModule;
import com.siziksu.tmdb.dagger.module.NetModule;
import com.siziksu.tmdb.dagger.module.PresenterModule;
import com.siziksu.tmdb.dagger.module.RequestModule;
import com.siziksu.tmdb.dagger.scope.AppScope;
import com.siziksu.tmdb.ui.main.MainActivity;
import com.siziksu.tmdb.ui.similar.SimilarMoviesActivity;

import dagger.Component;

@AppScope
@Component(
        modules = {
                AppModule.class,
                PresenterModule.class,
                RequestModule.class,
                NetModule.class
        }
)
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(SimilarMoviesActivity activity);
}
