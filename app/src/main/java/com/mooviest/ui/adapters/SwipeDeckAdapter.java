package com.mooviest.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.activities.MovieDetailActivity;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.GetMovieDetail;
import com.mooviest.ui.tasks.SwipeMovieInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jesus on 25/10/16.
 */

public class SwipeDeckAdapter extends BaseAdapter {

    private List<Movie> data;
    private Context context;
    private SwipeMovieInterface swipeMovieInterface;

    public SwipeDeckAdapter(List<Movie> data, Context context, SwipeMovieInterface swipeMovieInterface) {
        this.data = data;
        this.context = context;
        this.swipeMovieInterface = swipeMovieInterface;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Movie getItem(int position) {
        return data.get(position);
    }

    public void removeItem(int position){ data.remove(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(Movie m){ data.add(getCount(),m);}

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
            // normally use a viewholder
            v = inflater.inflate(R.layout.card, parent, false);
        }
        final Movie movie = (Movie)getItem(position);

        TextView title = (TextView) v.findViewById(R.id.movie_title);
        title.setText(movie.getTitle());

        TextView average = (TextView) v.findViewById(R.id.movie_average);
        average.setText(movie.getAverage());

        ImageView imageView = (ImageView) v.findViewById(R.id.offer_image);
        Log.i("OneFragment", "load new Image");
        //GET IMAGE
        String image = movie.getImage();
        String cover = "";

        if(image != null) {
            if (image.startsWith("http")) {
                cover = image;
            } else if (image.startsWith("EXTERNAL#")) {
                cover = "https://cdn.tviso.com/" + image.substring(9, image.length());
            } else {
                cover = "https://img.tviso.com/ES/poster/w430" + image;
            }
            Picasso.with(context).load(cover).placeholder(R.drawable.no_movie_swipe).fit().centerCrop().into(imageView);
        }else{
            Picasso.with(context).load(R.drawable.no_movie_swipe).fit().centerCrop().into(imageView);
        }
        Log.d("COVER", cover);

        Picasso.with(context).setIndicatorsEnabled(false);

        //Click in movie
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences user_prefs = view.getContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
                getMovieDetail(movie.getId(), movie.getMovie_lang_id(), user_prefs.getInt("id", 0), view);
            }
        });
        return v;
    }


    // CALL TO ASYNCTASK FOR GET MOVIE DETAIL
    private void getMovieDetail(int movie_id, int movie_lang_id, int user_id, final View view) {

        GetMovieDetail getMovieDetail = new GetMovieDetail(){
            @Override
            protected void onPostExecute(Movie result) {
                super.onPostExecute(result);
                if(result != null){
                    SingletonRestClient.getInstance().movie_selected = result;

                    if(swipeMovieInterface != null) {
                        swipeMovieInterface.movieClicked();
                    }
                }else {
                    Toast.makeText(view.getContext(), view.getContext().getString(R.string.no_movie_detail), Toast.LENGTH_LONG).show();
                }
            }
        };

        getMovieDetail.execute(movie_id, movie_lang_id, user_id);

    }
}
