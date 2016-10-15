package com.mooviest.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.RoundedTransformation;
import com.mooviest.ui.activities.MovieDetailActivity;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.GetMovieDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jesus on 14/10/16.
 */

public class MoviesUserListAdapter extends RecyclerView.Adapter<MoviesUserListAdapter.ViewHolder>{

    private ArrayList<Movie> movies;

    public MoviesUserListAdapter(ArrayList<Movie> movies){
        this.movies = movies;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView poster;
        public TextView title;
        public TextView average;
        public LinearLayout movie_linear;
        public View view;

        public ViewHolder(View v) {
            super(v);
            this.view = v;

            poster = (ImageView) v.findViewById(R.id.movie_poster);
            title = (TextView) v.findViewById(R.id.movie_title);
            average = (TextView) v.findViewById(R.id.movie_average);
            movie_linear = (LinearLayout) v.findViewById(R.id.movie_linear);
        }
    }

    @Override
    public MoviesUserListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_user_list, viewGroup, false);
        return new MoviesUserListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MoviesUserListAdapter.ViewHolder viewHolder, int position) {
        /*if(position == 0){
            Resources resources = viewHolder.view.getContext().getResources();
            int width = resources.getDimensionPixelSize(R.dimen.width_movie_user_list);
            int height = resources.getDimensionPixelSize(R.dimen.height_movie_user_list);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    width,
                    height
            );
            params.setMargins(50, 0, 0, 0);
            viewHolder.movie_linear.setLayoutParams(params);
        }*/

        final Movie movie = movies.get(position);

        Picasso p = Picasso.with(viewHolder.itemView.getContext());

        String image = movie.getImage();
        if(image.startsWith("http")){
            p.load(image).transform(new RoundedTransformation(6, 0)).fit().centerCrop().into(viewHolder.poster);
        }else{
            p.load("https://img.tviso.com/ES/poster/w430"+image).transform(new RoundedTransformation(6, 0)).fit().centerCrop().into(viewHolder.poster);
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

                    view.getContext().startActivity(new Intent(view.getContext(), MovieDetailActivity.class));
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

    public void addItems(ArrayList<Movie> movies){
        this.movies.addAll(movies);
    }

    public ArrayList<Movie> getItems(){
        return movies;
    }


}
