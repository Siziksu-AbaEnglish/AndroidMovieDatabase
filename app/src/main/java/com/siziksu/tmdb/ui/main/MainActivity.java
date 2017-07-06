package com.siziksu.tmdb.ui.main;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.siziksu.tmdb.App;
import com.siziksu.tmdb.R;
import com.siziksu.tmdb.common.Constants;
import com.siziksu.tmdb.common.functions.Consumer;
import com.siziksu.tmdb.common.model.response.movies.Movie;
import com.siziksu.tmdb.presenter.main.IMainPresenter;
import com.siziksu.tmdb.presenter.main.IMainView;
import com.siziksu.tmdb.ui.utils.IPagination;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class MainActivity extends AppCompatActivity implements IMainView {

    @Inject
    IMainPresenter presenter;
    @Inject
    IMoviesAdapter adapter;
    @Inject
    IPagination pagination;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.movies_recycler_view)
    RecyclerView movies;
    @BindView(R.id.movies_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.movies_error)
    TextView error;
    @BindView(R.id.movies_loading)
    ProgressBar loading;

    private SearchView searchView;
    private boolean firstTime = true;
    private String filter;
    private boolean refreshing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.get(getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter.init(
                (v, position) -> {
                    Movie movie = adapter.getItem(position);
                    if (movie != null) {
                        presenter.showMovieInfo(movie);
                    }
                },
                filtered -> {
                    if (!pagination.shouldLoadMore()) {
                        return;
                    }
                    if (!filtered) {
                        presenter.getMovies(pagination.getNextPage());
                    } else {
                        presenter.getFilteredMovies(filter, pagination.getNextPage());
                    }
                }
        );
        movies.addOnScrollListener(adapter.getOnScrollListener());
        movies.setHasFixedSize(true);
        movies.setAdapter(adapter.getAdapter());
        movies.setItemAnimator(new DefaultItemAnimator());
        movies.setLayoutManager(adapter.getLayoutManager());
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            doAfterClearingAdapter(() -> presenter.getMoviesFromSwipeRefresh());
            if (!searchView.isIconified()) {
                refreshing = true;
                searchView.onActionViewCollapsed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (searchView != null) {
            if (!searchView.isIconified()) {
                searchView.onActionViewCollapsed();
            } else if (searchView.isIconified() && adapter.isFiltered()) {
                requestFirstPage();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(Constants.EXTRAS_NEXT_PAGE, pagination.getNextPage());
        savedInstanceState.putInt(Constants.EXTRAS_TOTAL_PAGES, pagination.getTotalPages());
        savedInstanceState.putString(Constants.EXTRAS_FILTER, filter);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        pagination.setRestoredNextPage(savedInstanceState.getInt(Constants.EXTRAS_NEXT_PAGE));
        pagination.setRestoredTotalPages(savedInstanceState.getInt(Constants.EXTRAS_TOTAL_PAGES));
        filter = savedInstanceState.getString(Constants.EXTRAS_FILTER);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.register(this);
        if (firstTime) {
            presenter.getConfigurationAndMovies();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(onQueryTextListener);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {

        private boolean submit;

        @Override
        public boolean onQueryTextSubmit(String text) {
            submit = true;
            searchView.onActionViewCollapsed();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String text) {
            search(text);
            return false;
        }

        private void search(String text) {
            if (!refreshing) {
                if (!submit) {
                    filter = text;
                    if (!TextUtils.isEmpty(text)) {
                        doAfterClearingAdapter(() -> presenter.getFilteredMovies(text, pagination.getNextPage()));
                    } else {
                        requestFirstPage();
                    }
                }
            }
            refreshing = false;
            submit = false;
        }
    };

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showProgress(boolean value) {
        loading.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMovies(List<Movie> movies, boolean filtered, int totalPages) {
        pagination.setTotalPages(totalPages);
        adapter.showMovies(movies, filtered, pagination.isNotFirstPage());
        showError(adapter.isEmpty(), getString(R.string.no_data_available));
    }

    @Override
    public void showConnected(boolean value) {
        showError(!value, getString(R.string.connection_error));
    }

    @Override
    public void stopRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void connectionTimeout() {
        showError(true, getString(R.string.connection_timeout));
    }

    private void requestFirstPage() {
        doAfterClearingAdapter(() -> presenter.getMovies());
    }

    private void doAfterClearingAdapter(Consumer consumer) {
        adapter.clear();
        pagination.resetPagination();
        consumer.consume();
    }

    private void showError(boolean value, String string) {
        error.setVisibility(value ? View.VISIBLE : View.GONE);
        error.setText(string);
    }
}
