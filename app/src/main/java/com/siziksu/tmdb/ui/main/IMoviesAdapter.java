package com.siziksu.tmdb.ui.main;

import android.support.v7.widget.RecyclerView;

import com.siziksu.tmdb.common.model.response.movies.Movie;

import java.util.List;

public interface IMoviesAdapter {

    void init(MoviesAdapter.ClickListener listener, MoviesAdapter.EndOfListListener endOfListListener);

    Movie getItem(int position);

    boolean isEmpty();

    void clear();

    RecyclerView.LayoutManager getLayoutManager();

    RecyclerView.OnScrollListener getOnScrollListener();

    void showMovies(List<Movie> list, boolean filtered, boolean more);

    RecyclerView.Adapter getAdapter();

    boolean isFiltered();
}
