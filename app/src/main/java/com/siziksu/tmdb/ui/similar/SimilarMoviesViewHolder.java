package com.siziksu.tmdb.ui.similar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.siziksu.tmdb.R;

import butterknife.BindView;
import butterknife.ButterKnife;

final class SimilarMoviesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.similar_movie_art)
    ImageView similarMovieArt;
    @BindView(R.id.similar_movie_title)
    TextView similarMovieTitle;
    @BindView(R.id.similar_movie_year)
    TextView similarMovieYear;
    @BindView(R.id.similar_movie_overview)
    TextView similarMovieOverview;
    @BindView(R.id.similar_movie_average_rating)
    TextView similarMovieAverageRating;

    SimilarMoviesViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
