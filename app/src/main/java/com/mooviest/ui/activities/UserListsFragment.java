package com.mooviest.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.adapters.MoviesListAdapter;
import com.mooviest.ui.adapters.MoviesUserListAdapter;
import com.mooviest.ui.rest.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.GetUserList;


public class UserListsFragment extends Fragment {

    private RecyclerView seen_list_recycler;
    private RecyclerView watchlist_recycler;
    private RecyclerView favourite_list_recycler;
    private Button seen_button;
    private Button watchlist_button;
    private Button favourite_button;



    public UserListsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_lists, container, false);

        // SEEN
        seen_button = (Button) view.findViewById(R.id.seen_button);
        seen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserListAPI("seen_list", 1, R.string.seen_list);
            }
        });

        seen_list_recycler = (RecyclerView) view.findViewById(R.id.seen_list_recycler);
        LinearLayoutManager seenListLayoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        seen_list_recycler.setLayoutManager(seenListLayoutManager);
        MoviesUserListAdapter seenListAdapter = new MoviesUserListAdapter(SingletonRestClient.getInstance().seen_list);
        seen_list_recycler.setAdapter(seenListAdapter);


        // WATCHLIST
        watchlist_button = (Button) view.findViewById(R.id.watchlist_button);
        watchlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserListAPI("watchlist", 1, R.string.watchlist);
            }
        });


        // FAVOURITE_LIST
        favourite_button = (Button) view.findViewById(R.id.favourite_button);
        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserListAPI("favourite_list", 1, R.string.favourite_list);
            }
        });

        favourite_list_recycler = (RecyclerView) view.findViewById(R.id.favourite_list_recycler);
        LinearLayoutManager favouriteListLayoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        favourite_list_recycler.setLayoutManager(favouriteListLayoutManager);
        MoviesUserListAdapter favouriteListAdapter = new MoviesUserListAdapter(SingletonRestClient.getInstance().favourite_list);
        favourite_list_recycler.setAdapter(favouriteListAdapter);

        return view;

    }

    private void getUserListAPI(final String list_name, int page, final int titleResource){
        GetUserList getUserList = new GetUserList(list_name){
            @Override
            protected void onPostExecute(MooviestApiResult result) {
                super.onPostExecute(result);
                if(result!=null) {
                    if(result.getCount() >= 1) {
                        SingletonRestClient.getInstance().movies_list = result.getMovies();
                        Boolean next = false;
                        if(result.getNext() != null) {
                            next = true;
                        }

                        Intent i = new Intent(getActivity(), MoviesListActivity.class);

                        i.putExtra("TITLE", titleResource);
                        i.putExtra("LIST_NAME", list_name);
                        i.putExtra("NEXT", next);
                        i.putExtra("COUNT",result.getCount());
                        startActivity(i);
                    }else{
                        Toast.makeText(getActivity(), "No movies list in" + getString(titleResource), Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        // Params: page API result
        getUserList.execute(page);
    }


}
