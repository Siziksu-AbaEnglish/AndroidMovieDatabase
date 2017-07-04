package com.siziksu.tmdb.ui.similar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.siziksu.tmdb.App;
import com.siziksu.tmdb.R;
import com.siziksu.tmdb.common.Constants;
import com.siziksu.tmdb.common.model.response.movies.Movie;
import com.siziksu.tmdb.presenter.similar.ISimilarMoviesPresenter;
import com.siziksu.tmdb.presenter.similar.ISimilarMoviesView;
import com.siziksu.tmdb.ui.utils.IPagination;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class SimilarMoviesActivity extends AppCompatActivity implements ISimilarMoviesView {

    @Inject
    ISimilarMoviesPresenter presenter;
    @Inject
    ISimilarMoviesAdapter adapter;
    @Inject
    IPagination pagination;

    @BindView(R.id.similar_movies_recycler_view)
    RecyclerView movies;
    @BindView(R.id.similar_movies_loading)
    ProgressBar loading;
    @BindView(R.id.similar_movies_error)
    TextView error;
    @BindView(R.id.similar_movies_swipe_tutorial)
    View tutorial;

    private Movie movie;
    private boolean firstTime = true;

    public static void launch(Activity activity, Movie movie) {
        Intent intent = new Intent(activity, SimilarMoviesActivity.class);
        intent.putExtra(Constants.EXTRAS_MOVIE_KEY, movie);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.animation_enter, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_movies);
        App.get(getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        checkExtras();
        initTutorial();
        initRecyclerView();
    }

    private void checkExtras() {
        if (getIntent().getExtras() == null || !getIntent().getExtras().containsKey(Constants.EXTRAS_MOVIE_KEY)) {
            finish();
        }
        movie = getIntent().getExtras().getParcelable(Constants.EXTRAS_MOVIE_KEY);
    }

    private void initTutorial() {
        if (!presenter.isTutorialCompleted()) {
            showTutorial(true);
        } else {
            showTutorial(false);
        }
    }

    private void initRecyclerView() {
        adapter.init(
                () -> {
                    if (!pagination.shouldLoadMore()) {
                        return;
                    }
                    presenter.getSimilarMovies(movie, pagination.getNextPage());
                }
        );
        movies.addOnScrollListener(adapter.getOnScrollListener());
        movies.setAdapter(adapter.getAdapter());
        movies.setItemAnimator(new DefaultItemAnimator());
        movies.setLayoutManager(adapter.getLayoutManager());
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(movies);
    }

    @Override
    public void onBackPressed() {
        if (presenter.isTutorialCompleted()) {
            super.onBackPressed();
            overridePendingTransition(0, R.anim.animation_leave);
        } else {
            presenter.tutorialCompleted();
            showTutorial(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(Constants.EXTRAS_NEXT_PAGE, pagination.getNextPage());
        savedInstanceState.putInt(Constants.EXTRAS_TOTAL_PAGES, pagination.getTotalPages());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        pagination.setRestoredNextPage(savedInstanceState.getInt(Constants.EXTRAS_NEXT_PAGE));
        pagination.setRestoredTotalPages(savedInstanceState.getInt(Constants.EXTRAS_TOTAL_PAGES));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.register(this);
        if (firstTime) {
            presenter.getSimilarMovies(movie);
            firstTime = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.unregister();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showProgress(boolean value) {
        loading.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMovies(List<Movie> movies, int totalPages) {
        pagination.setTotalPages(totalPages);
        adapter.showMovies(movies, pagination.isNotFirstPage());
    }

    @Override
    public void showConnected(boolean value) {
        showError(!value, getString(R.string.connection_error));
    }

    @Override
    public void connectionTimeout() {
        showError(true, getString(R.string.connection_timeout));
    }

    private void showError(boolean value, String string) {
        error.setVisibility(value ? View.VISIBLE : View.GONE);
        error.setText(string);
        movies.setVisibility(value ? View.GONE : View.VISIBLE);
    }

    private void showTutorial(boolean value) {
        tutorial.setVisibility(value ? View.VISIBLE : View.GONE);
        tutorial.setOnClickListener(value ? (view) -> {
        } : null);
    }
}
