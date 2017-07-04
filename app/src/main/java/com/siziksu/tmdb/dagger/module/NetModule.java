package com.siziksu.tmdb.dagger.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.siziksu.tmdb.dagger.scope.AppScope;
import com.siziksu.tmdb.data.movies.MovieDatabaseClientService;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public final class NetModule {

    private static final String BASE_URL = "https://api.themoviedb.org";
    private static final int CONNECTION_TIMEOUT = 5;

    public NetModule() {}

    @Provides
    @AppScope
    Cache providesOkHttpCache(Context context) {
        final int cacheSize = 10485760; // 10 * 1024 * 1024; // 10 MiB
        return new Cache(context.getCacheDir(), cacheSize);
    }

    @Provides
    @AppScope
    OkHttpClient providesOkHttpClient(Cache cache) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging);
        return builder.build();
    }

    @Provides
    @AppScope
    Converter.Factory providesGsonConverterFactory() {
        Gson gson = new GsonBuilder().create();
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @AppScope
    Retrofit providesRetrofit(OkHttpClient okHttpClient, Converter.Factory factoryConverter) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(factoryConverter)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL);
        builder.client(okHttpClient);
        return builder.build();
    }

    @Provides
    @AppScope
    MovieDatabaseClientService providesSessionClient(Retrofit retrofit) {
        return retrofit.create(MovieDatabaseClientService.class);
    }
}
