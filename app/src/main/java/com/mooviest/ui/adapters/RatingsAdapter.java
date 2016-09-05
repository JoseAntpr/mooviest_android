package com.mooviest.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooviest.R;
import com.mooviest.ui.RoundedTransformation;
import com.mooviest.ui.models.Rating;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jesus on 5/9/16.
 */
public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.ViewHolder> {

    private final List<Rating> ratings;

    public RatingsAdapter(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView logo_image;
        public TextView rating;

        public ViewHolder(View v) {
            super(v);

            logo_image = (ImageView) v.findViewById(R.id.logo_image);
            rating = (TextView) v.findViewById(R.id.movie_detail_rating);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rating_grid, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Rating rating = ratings.get(position);

        Picasso p = Picasso.with(viewHolder.itemView.getContext());

        String name = rating.getName();
        int logo = 0; //R.drawable.logo_undefined
        switch (name){
            case "IMDb":
                logo = R.drawable.imdb_logo;
                break;
            case "Tviso":
                logo = R.drawable.tviso_logo;
                break;
        }

        p.load(logo).transform(new RoundedTransformation(100, 0)).fit().centerCrop().into(viewHolder.logo_image);


        viewHolder.rating.setText(String.valueOf(rating.getRating()));
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }
}