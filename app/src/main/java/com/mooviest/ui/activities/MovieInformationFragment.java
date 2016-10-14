package com.mooviest.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mooviest.R;
import com.mooviest.ui.adapters.RatingsAdapter;
import com.mooviest.ui.rest.SingletonRestClient;


public class MovieInformationFragment extends Fragment {

    private TextView movie_synopsis;
    private RecyclerView recycler;
    private GridLayoutManager layoutManager;
    private RatingsAdapter ratingsAdapter;


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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_information, container, false);

        //RecyclerView
        recycler = (RecyclerView) view.findViewById(R.id.ratings_recycler);
        layoutManager = new GridLayoutManager(getActivity(),3);
        recycler.setLayoutManager(layoutManager);

        ratingsAdapter = new RatingsAdapter(SingletonRestClient.getInstance().movie_selected.getRatings());

        recycler.setAdapter(ratingsAdapter);

        //Synopsis
        movie_synopsis= (TextView) view.findViewById(R.id.movie_synopsis);
        movie_synopsis.setText(SingletonRestClient.getInstance().movie_selected.getSynopsis());


        return view;
    }
}
