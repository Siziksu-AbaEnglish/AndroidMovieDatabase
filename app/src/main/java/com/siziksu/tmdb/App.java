package com.siziksu.tmdb;

import android.app.Application;
import android.os.Build;
import android.webkit.WebView;

import com.siziksu.tmdb.dagger.component.AppComponent;
import com.siziksu.tmdb.dagger.component.DaggerAppComponent;
import com.siziksu.tmdb.dagger.module.AppModule;
import com.siziksu.tmdb.dagger.module.NetModule;

public final class App extends Application {

    private AppComponent appComponent;

    public static App get(Application application) {
        return ((App) application);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (BuildConfig.DEBUG) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        setUpDagger();
    }

    private void setUpDagger() {
        appComponent = DaggerAppComponent.builder()
                                         .appModule(new AppModule(this))
                                         .netModule(new NetModule())
                                         .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}