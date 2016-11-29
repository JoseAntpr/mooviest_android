package com.mooviest.ui.activities.movie_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mooviest.R;
import com.mooviest.ui.PaddingItemDecoration;
import com.mooviest.ui.adapters.GenresAdapter;
import com.mooviest.ui.adapters.RatingsAdapter;
import com.mooviest.ui.models.Genre;
import com.mooviest.ui.models.Movie;
import com.mooviest.ui.rest.SingletonRestClient;


import java.util.List;


public class MovieInformationFragment extends Fragment {

    private TextView movie_synopsis;
    private TextView movie_released;

    private RecyclerView ratings_recycler;
    private GridLayoutManager gridLayoutManagerRatings;
    private RatingsAdapter ratingsAdapter;

    private RecyclerView genres_recycler;
    private LinearLayoutManager linearLayoutManagerGenres;
    private GenresAdapter genresAdapter;


    public MovieInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Movie movie_selected = SingletonRestClient.getInstance().movie_selected;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_information, container, false);

        //Ratings
        ratings_recycler = (RecyclerView) view.findViewById(R.id.ratings_recycler);
        gridLayoutManagerRatings = new GridLayoutManager(getActivity(),3);
        ratings_recycler.setLayoutManager(gridLayoutManagerRatings);

        ratingsAdapter = new RatingsAdapter(movie_selected.getRatings());

        ratings_recycler.setAdapter(ratingsAdapter);

        //Released
        movie_released = (TextView) view.findViewById(R.id.movie_released);
        movie_released.setText(String.valueOf(movie_selected.getReleased()));

        //Synopsis
        movie_synopsis = (TextView) view.findViewById(R.id.movie_synopsis);
        movie_synopsis.setText(movie_selected.getSynopsis());


        //Genres
        List<Genre> genreList = movie_selected.getGenres();
        if(!genreList.isEmpty()) {

            genres_recycler = (RecyclerView) view.findViewById(R.id.genres_recycler);
            linearLayoutManagerGenres = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            genres_recycler.setLayoutManager(linearLayoutManagerGenres);


            genresAdapter = new GenresAdapter(genreList);

            genres_recycler.setAdapter(genresAdapter);
            genres_recycler.addItemDecoration(new PaddingItemDecoration(30, getContext()));
        }

        return view;
    }
}
