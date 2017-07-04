package com.siziksu.tmdb.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.siziksu.tmdb.R;

import butterknife.BindView;
import butterknife.ButterKnife;

final class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.movie_view)
    View movieRow;
    @BindView(R.id.movie_card_art)
    ImageView movieArt;
    @BindView(R.id.movie_card_title)
    TextView movieTitle;
    @BindView(R.id.movie_card_average_rating)
    TextView movieCardAverageRating;

    private MoviesAdapter.ClickListener listener;

    MoviesViewHolder(View view, MoviesAdapter.ClickListener listener) {
        super(view);
        ButterKnife.bind(this, view);
        this.listener = listener;
        movieRow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
