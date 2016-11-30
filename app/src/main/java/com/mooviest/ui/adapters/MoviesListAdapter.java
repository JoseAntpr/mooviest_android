package com.mooviest.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.RoundedTransformation;
import com.mooviest.ui.activities.movie_detail.MovieDetailActivity;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.movie_detail.GetMovieDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jesus on 8/10/16.
 */

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.ViewHolder> {

    private ArrayList<Movie> movies;
    private String list_name;

    public MoviesListAdapter(){
        this.movies = new ArrayList<Movie>();
        this.list_name = "";
    };

    public MoviesListAdapter(ArrayList<Movie> movies){
        this.movies = movies;
        this.list_name = "";
    }

    public MoviesListAdapter(ArrayList<Movie> movies, String list_name){
        this.movies = movies;
        this.list_name = list_name;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView poster;
        public TextView title;
        public TextView average;
        public View view;

        public ViewHolder(View v) {
            super(v);
            this.view = v;

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
        final Movie movie = movies.get(position);

        Picasso p = Picasso.with(viewHolder.itemView.getContext());

        String image = movie.getImage();
        if(image == null || image == "") {
            p.load(R.drawable.no_movie_list).transform(new RoundedTransformation(6, 0)).fit().centerCrop().into(viewHolder.poster);
        }else{
            if (image.startsWith("http")) {
                p.load(image).placeholder(R.drawable.no_movie_list).transform(new RoundedTransformation(6, 0)).fit().centerCrop().into(viewHolder.poster);
            } else if (image.startsWith("EXTERNAL#")) {
                p.load("https://cdn.tviso.com/" + image.substring(9, image.length())).placeholder(R.drawable.no_movie_list).transform(new RoundedTransformation(6, 0)).fit().centerCrop().into(viewHolder.poster);
            } else {
                p.load("https://img.tviso.com/ES/poster/w430" + image).placeholder(R.drawable.no_movie_list).transform(new RoundedTransformation(6, 0)).fit().centerCrop().into(viewHolder.poster);
            }
        }

        viewHolder.title.setText(movie.getTitle());
        viewHolder.average.setText(movie.getAverage());

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences user_prefs = view.getContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
                getMovieDetail(movie.getId(), movie.getMovie_lang_id(), user_prefs.getInt("id", 0), view);
            }
        });

    }

    // CALL TO ASYNCTASK FOR GET MOVIE DETAIL
    private void getMovieDetail(int movie_id, int movie_lang_id, int user_id, final View view) {

        GetMovieDetail getMovieDetail = new GetMovieDetail(){
            @Override
            protected void onPostExecute(Movie result) {
                super.onPostExecute(result);
                if(result != null){
                    SingletonRestClient.getInstance().movie_selected = result;
                    Intent intent = new Intent(view.getContext(), MovieDetailActivity.class);
                    intent.putExtra("FROM", list_name);
                    view.getContext().startActivity(intent);
                }else {
                    Toast.makeText(view.getContext(), "No detail for movie selected", Toast.LENGTH_LONG).show();
                }
            }
        };

        getMovieDetail.execute(movie_id, movie_lang_id, user_id);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void removeItem(Movie movie){
        this.movies.remove(movie);
    }

    public void removeAllItems(){ this.movies = new ArrayList<Movie>();}

    public void addItems(ArrayList<Movie> movies){
        this.movies.addAll(movies);
    }

    public void addItem(Movie movie){this.movies.add(movie);}

    public ArrayList<Movie> getItems(){
        return movies;
    }

    public void setListName(String list_name){
        this.list_name = list_name;
    }


}
