package com.siziksu.tmdb.presenter.similar;

import android.util.Log;

import com.siziksu.tmdb.R;
import com.siziksu.tmdb.common.manager.ConnectionManager;
import com.siziksu.tmdb.common.manager.PreferencesManager;
import com.siziksu.tmdb.common.model.response.movies.Movie;
import com.siziksu.tmdb.common.model.response.movies.Movies;
import com.siziksu.tmdb.domain.movies.IMovieDatabaseRequests;

import java.net.SocketTimeoutException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public final class SimilarMoviesPresenter extends ISimilarMoviesPresenter {

    @Inject
    PreferencesManager preferencesManager;
    @Inject
    ConnectionManager connectionManager;
    @Inject
    IMovieDatabaseRequests mainRequests;

    private static final String TAG = "SimilarMoviesPresenter";
    private static final int FIRST_PAGE = 1;
    private Movie movie;
    private int page;

    private Disposable disposable;

    @Inject
    SimilarMoviesPresenter() {}

    @Override
    public void getSimilarMovies(Movie movie) {
        getSimilarMovies(movie, FIRST_PAGE);
    }

    @Override
    public void getSimilarMovies(Movie movie, int page) {
        this.movie = movie;
        this.page = page;
        cancelLastRequest(page);
        boolean isConnected = connectionManager.doIfThereIsConnection(
                () -> doIfViewIsRegistered(
                        () -> {
                            view.showProgress(true);
                            disposable = mainRequests.getSimilarMovies(movie.id, page, false)
                                                     .observeOn(AndroidSchedulers.mainThread())
                                                     .subscribe(this::onMovies,
                                                                this::onError);
                        }));
        tellToTheViewIfItIsConnected(isConnected);
    }

    @Override
    public void tutorialCompleted() {
        preferencesManager.tutorialCompleted(true);
    }

    @Override
    public boolean isTutorialCompleted() {
        return preferencesManager.isTutorialCompleted();
    }

    private void cancelLastRequest(int page) {
        if (disposable != null && page == FIRST_PAGE) {
            disposable.dispose();
            disposable = null;
        }
    }

    private void onMovies(Movies response) {
        disposable = null;
        if (page == FIRST_PAGE) {
            response.movies.add(0, movie);
        }
        doIfViewIsRegistered(() -> view.showMovies(response.movies, response.totalPages));
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

    private void stopProgress() {
        doIfViewIsRegistered(() -> view.showProgress(false));
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
