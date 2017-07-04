package com.siziksu.tmdb.presenter.main;

import android.util.Log;

import com.siziksu.tmdb.R;
import com.siziksu.tmdb.common.Constants;
import com.siziksu.tmdb.common.manager.ConnectionManager;
import com.siziksu.tmdb.common.manager.PreferencesManager;
import com.siziksu.tmdb.common.model.response.configuration.Configuration;
import com.siziksu.tmdb.common.model.response.movies.Movie;
import com.siziksu.tmdb.common.model.response.movies.Movies;
import com.siziksu.tmdb.domain.movies.IMovieDatabaseRequests;
import com.siziksu.tmdb.ui.similar.SimilarMoviesActivity;

import java.net.SocketTimeoutException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public final class MainPresenter extends IMainPresenter {

    @Inject
    PreferencesManager preferencesManager;
    @Inject
    ConnectionManager connectionManager;
    @Inject
    IMovieDatabaseRequests mainRequests;

    private static final String TAG = "MainPresenter";
    private static final int FIRST_PAGE = 1;

    private Disposable disposable;
    private boolean swipeOn;

    @Inject
    MainPresenter() {}

    @Override
    public void getConfigurationAndMovies() {
        boolean isConnected = connectionManager.doIfThereIsConnection(
                () -> doIfViewIsRegistered(
                        () -> {
                            startProgress();
                            mainRequests.getConfiguration()
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(this::onConfiguration,
                                                   this::onError);
                        }
                ));
        tellToTheViewIfItIsConnected(isConnected);
    }

    @Override
    public void getMoviesFromSwipeRefresh() {
        swipeOn = true;
        cancelProgress();
        getMovies(false, null, FIRST_PAGE);
    }

    @Override
    public void getMovies() {
        getMovies(FIRST_PAGE);
    }

    @Override
    public void getMovies(int page) {
        cancelSwipeRefresh();
        getMovies(false, null, page);
    }

    @Override
    public void getFilteredMovies(String text, int page) {
        cancelSwipeRefresh();
        getMovies(true, text, page);
    }

    @Override
    public void showMovieInfo(Movie movie) {
        doIfViewIsRegistered(() -> SimilarMoviesActivity.launch(this.view.getActivity(), movie));
    }

    private void getMovies(boolean filtered, String text, int page) {
        cancelLastRequest(page);
        boolean isConnected = connectionManager.doIfThereIsConnection(
                () -> doIfViewIsRegistered(
                        () -> {
                            startProgress();
                            disposable = mainRequests.getMovies(page, text, false)
                                                     .observeOn(AndroidSchedulers.mainThread())
                                                     .subscribe(response -> onMovies(response, filtered),
                                                                this::onError);
                        }));
        tellToTheViewIfItIsConnected(isConnected);
    }

    private void cancelLastRequest(int page) {
        if (disposable != null && page == FIRST_PAGE) {
            disposable.dispose();
            disposable = null;
        }
    }

    private void onConfiguration(Configuration configuration) {
        preferencesManager.setBackdropUrl(
                configuration.images.baseUrl +
                checkValue(configuration.images.backdropSizes, Constants.PREFERENCES_DEFAULT_BACKDROP_WIDTH_780_VALUE)
        );
        preferencesManager.setPoserUrl(
                configuration.images.baseUrl +
                checkValue(configuration.images.posterSizes, Constants.PREFERENCES_DEFAULT_POSTER_WIDTH_500_VALUE)
        );
        getMovies();
    }

    private String checkValue(List<String> list, String value) {
        String result;
        if (list.contains(value)) {
            result = value;
        } else {
            result = Constants.PREFERENCES_IMAGES_WIDTH_ORIGINAL_VALUE;
        }
        return result;
    }

    private void onMovies(Movies response, boolean filtered) {
        disposable = null;
        doIfViewIsRegistered(() -> view.showMovies(response.movies, filtered, response.totalPages));
        stopProgress();
    }

    private void onError(Throwable throwable) {
        disposable = null;
        Log.e(TAG, throwable.getMessage(), throwable);
        if (throwable instanceof SocketTimeoutException) {
            doIfViewIsRegistered(() -> view.connectionTimeout());
        }
        stopProgress();
    }

    private void cancelProgress() {
        doIfViewIsRegistered(() -> view.showProgress(false));
    }

    private void cancelSwipeRefresh() {
        if (swipeOn) {
            swipeOn = false;
            doIfViewIsRegistered(() -> view.stopRefreshing());
        }
    }

    private void startProgress() {
        doIfViewIsRegistered(() -> view.showProgress(true));
    }

    private void stopProgress() {
        doIfViewIsRegistered(() -> {
            if (swipeOn) {
                view.stopRefreshing();
                swipeOn = false;
            } else {
                view.showProgress(false);
            }
        });
    }

    private void tellToTheViewIfItIsConnected(boolean isConnected) {
        doIfViewIsRegistered(
                () -> {
                    if (!isConnected) {
                        Log.e(TAG, view.getActivity().getResources().getString(R.string.connection_error));
                        stopProgress();
                    }
                    view.showConnected(isConnected);
                });
    }
}
