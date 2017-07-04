package com.siziksu.tmdb.ui.main;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siziksu.tmdb.R;
import com.siziksu.tmdb.common.Constants;
import com.siziksu.tmdb.common.manager.PreferencesManager;
import com.siziksu.tmdb.common.model.response.movies.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public final class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IMoviesAdapter {

    @Inject
    Context context;
    @Inject
    PreferencesManager preferencesManager;

    private List<Movie> movies;
    private ClickListener listener;
    private EndOfListListener endOfListListener;
    private OnScrollListener onScrollListener;
    private GridLayoutManager layoutManager;
    private boolean filtered;

    @Inject
    MoviesAdapter() {}

    public void init(ClickListener listener, EndOfListListener endOfListListener) {
        movies = new ArrayList<>();
        onScrollListener = new OnScrollListener();
        this.listener = listener;
        int spanCount = context.getResources().getInteger(R.integer.movies_columns);
        layoutManager = new GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false);
        this.endOfListListener = endOfListListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MoviesViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MoviesViewHolder) {
            MoviesViewHolder localHolder = (MoviesViewHolder) holder;
            Movie movie = movies.get(position);
            String url = preferencesManager.getValue(Constants.PREFERENCES_POSTER_URL_KEY, "") + movie.posterPath;
            Picasso.with(context)
                   .load(url)
                   .placeholder(R.drawable.placeholder)
                   .into(localHolder.movieArt);
            if (TextUtils.isEmpty(movie.posterPath)) {
                localHolder.movieTitle.setVisibility(View.VISIBLE);
                localHolder.movieTitle.setText(movie.title);
            } else {
                localHolder.movieTitle.setVisibility(View.GONE);
            }
            localHolder.movieCardAverageRating.setText(String.valueOf(movie.voteAverage));
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        if (!movies.isEmpty() && position < movies.size()) {
            return movies.get(position);
        } else {
            return null;
        }
    }

    @Override
    public boolean isEmpty() {
        return movies.isEmpty();
    }

    @Override
    public GridLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public RecyclerView.OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    @Override
    public void showMovies(List<Movie> list, boolean filtered, boolean more) {
        this.filtered = filtered;
        if (!more) {
            movies.clear();
        }
        showAllMovies(list);
    }

    private void showAllMovies(List<Movie> list) {
        movies.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return this;
    }

    @Override
    public boolean isFiltered() {
        return filtered;
    }

    private final class OnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!movies.isEmpty() && (layoutManager.findLastCompletelyVisibleItemPosition() + 1) == movies.size()) {
                if (endOfListListener != null) {
                    endOfListListener.onEndOfListReached(filtered);
                }
            }
        }
    }

    interface ClickListener {

        void onClick(View view, int position);
    }

    interface EndOfListListener {

        void onEndOfListReached(boolean filtered);
    }
}
