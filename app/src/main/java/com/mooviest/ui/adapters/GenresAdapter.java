package com.mooviest.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mooviest.R;
import com.mooviest.ui.models.Genre;

import java.util.List;

/**
 * Created by jesus on 16/11/16.
 */

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.ViewHolder> {

    private List<Genre> genres;

    public GenresAdapter(List<Genre> genres){ this.genres = genres; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView genre_title;

        public ViewHolder(View v) {
            super(v);
            genre_title = (TextView) v.findViewById(R.id.genre_title);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.genre_grid, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Genre genre = genres.get(position);

        viewHolder.genre_title.setText(genre.getName());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }
}
