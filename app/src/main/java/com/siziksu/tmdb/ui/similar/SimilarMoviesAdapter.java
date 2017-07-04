package com.siziksu.tmdb.ui.similar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siziksu.tmdb.R;
import com.siziksu.tmdb.common.Constants;
import com.siziksu.tmdb.common.manager.PreferencesManager;
import com.siziksu.tmdb.common.model.response.movies.Movie;
import com.siziksu.tmdb.common.utils.DatesUtilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public final class SimilarMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ISimilarMoviesAdapter {

    @Inject
    Context context;
    @Inject
    PreferencesManager preferencesManager;

    private List<Movie> movies;
    private EndOfListListener endOfListListener;
    private OnScrollListener onScrollListener;
    private LinearLayoutManager layoutManager;

    @Inject
    SimilarMoviesAdapter() {}

    public void init(EndOfListListener endOfListListener) {
        movies = new ArrayList<>();
        onScrollListener = new OnScrollListener();
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        this.endOfListListener = endOfListListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_similar_movie, parent, false);
        return new SimilarMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SimilarMoviesViewHolder) {
            SimilarMoviesViewHolder localHolder = (SimilarMoviesViewHolder) holder;
            Movie movie = movies.get(position);
            String url = preferencesManager.getValue(Constants.PREFERENCES_BACKDROP_URL_KEY, "") + movie.backdropPath;
            Picasso.with(context)
                   .load(url)
                   .placeholder(R.drawable.banner_placeholder)
                   .into(localHolder.similarMovieArt);
            localHolder.similarMovieTitle.setText(movie.title);
            localHolder.similarMovieYear.setText(
                    String.format(context.getResources().getString(R.string.year), DatesUtilities.getYear(movie.releaseDate))
            );
            localHolder.similarMovieOverview.setText(movie.overview);
            localHolder.similarMovieAverageRating.setText(String.valueOf(movie.voteAverage));
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public RecyclerView.OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    @Override
    public void showMovies(List<Movie> list, boolean more) {
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
    public RecyclerView.Adapter getAdapter() {
        return this;
    }

    private final class OnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!movies.isEmpty() && (layoutManager.findLastCompletelyVisibleItemPosition() + 1) == movies.size()) {
                if (endOfListListener != null) {
                    endOfListListener.onEndOfListReached();
                }
            }
        }
    }

    interface EndOfListListener {

        void onEndOfListReached();
    }
}
