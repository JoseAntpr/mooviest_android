package com.mooviest.ui.activities.movie_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mooviest.R;
import com.mooviest.ui.adapters.CelebritiesAdapter;
import com.mooviest.ui.rest.SingletonRestClient;

public class MovieCastFragment extends Fragment {

    private RecyclerView recycler;
    private GridLayoutManager layoutManager;
    private CelebritiesAdapter celebritiesAdapter;

    public MovieCastFragment() {
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
        View view = inflater.inflate(R.layout.fragment_movie_cast, container, false);

        // RecyclerView
        recycler = (RecyclerView) view.findViewById(R.id.celebrity_recycler);
        layoutManager = new GridLayoutManager(getActivity(),3);
        recycler.setLayoutManager(layoutManager);

        celebritiesAdapter = new CelebritiesAdapter(SingletonRestClient.getInstance().movie_selected.getParticipations());

        recycler.setAdapter(celebritiesAdapter);

        return view;
    }
}
