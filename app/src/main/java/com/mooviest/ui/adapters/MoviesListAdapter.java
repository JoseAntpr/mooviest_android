package com.mooviest.ui.adapters;

import android.media.Image;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooviest.R;
import com.mooviest.ui.RoundedTransformation;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.models.MovieLang;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jesus on 8/10/16.
 */

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {

    private ArrayList<Movie> movies;

    public MoviesListAdapter(ArrayList<Movie> movies){
        this.movies = movies;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView poster;
        public TextView title;
        public TextView average;

        public ViewHolder(View v) {
            super(v);

            poster = (ImageView) v.findViewById(R.id.movie_poster);
            title = (TextView) v.findViewById(R.id.movie_title);
            average = (TextView) v.findViewById(R.id.movie_average);
        }
    }

    @Override
    public MoviesListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_grid, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MoviesListAdapter.ViewHolder viewHolder, int position) {
        Movie movie = movies.get(position);
        MovieLang movieLang = movie.getLangs();

        Picasso p = Picasso.with(viewHolder.itemView.getContext());

        String image = movieLang.getImage();
        if(image.startsWith("http")){
            p.load(image).transform(new RoundedTransformation(6, 0)).fit().centerCrop().into(viewHolder.poster);
        }else{
            p.load("https://img.tviso.com/ES/poster/w430"+image).transform(new RoundedTransformation(6, 0)).fit().centerCrop().into(viewHolder.poster);
        }

        viewHolder.title.setText(movieLang.getTitle());
        viewHolder.average.setText(movie.getAverage());

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void addItems(ArrayList<Movie> movies){
        this.movies.addAll(movies);
    }

    public ArrayList<Movie> getItems(){
        return movies;
    }


}
