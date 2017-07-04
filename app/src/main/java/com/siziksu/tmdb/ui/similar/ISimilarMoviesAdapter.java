package com.siziksu.tmdb.ui.similar;

import android.support.v7.widget.RecyclerView;

import com.siziksu.tmdb.common.model.response.movies.Movie;

import java.util.List;

public interface ISimilarMoviesAdapter {

    void init(SimilarMoviesAdapter.EndOfListListener endOfListListener);

    RecyclerView.LayoutManager getLayoutManager();

    RecyclerView.OnScrollListener getOnScrollListener();

    void showMovies(List<Movie> list, boolean more);

    RecyclerView.Adapter getAdapter();
}
