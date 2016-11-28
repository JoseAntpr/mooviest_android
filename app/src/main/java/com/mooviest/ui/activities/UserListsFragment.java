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
import android.widget.TextView;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.PaddingItemDecoration;
import com.mooviest.ui.adapters.MoviesListAdapter;
import com.mooviest.ui.adapters.MoviesUserListAdapter;
import com.mooviest.ui.rest.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.GetUserList;


public class UserListsFragment extends Fragment {

    private RecyclerView seen_list_recycler;
    private RecyclerView watchlist_recycler;
    private RecyclerView favourite_list_recycler;
    private RecyclerView blacklist_recycler;
    private Button seen_button;
    private Button watchlist_button;
    private Button favourite_button;
    private Button blacklist_button;
    private TextView no_favourite_list;
    private TextView no_seen_list;
    private TextView no_watchlist;
    private TextView no_blacklist;



    public UserListsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_lists, container, false);

        PaddingItemDecoration paddingItemDecoration = new PaddingItemDecoration(10, getContext());

        // ********* SEEN ***********
        no_seen_list = (TextView) view.findViewById(R.id.no_seen_list);
        if(!SingletonRestClient.getInstance().seen_list.isEmpty()) {
            no_seen_list.setVisibility(View.GONE);
        }
        seen_button = (Button) view.findViewById(R.id.seen_button);
        seen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserListAPI("seen", 1, R.string.seen_list);
            }
        });

        seen_list_recycler = (RecyclerView) view.findViewById(R.id.seen_list_recycler);
        LinearLayoutManager seenListLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        seen_list_recycler.setLayoutManager(seenListLayoutManager);
        if(SingletonRestClient.getInstance().seenListAdapter == null) {
            SingletonRestClient.getInstance().seenListAdapter = new MoviesUserListAdapter(SingletonRestClient.getInstance().seen_list);
        }
        seen_list_recycler.setAdapter(SingletonRestClient.getInstance().seenListAdapter);
        seen_list_recycler.addItemDecoration(paddingItemDecoration);



        // *********** WATCHLIST ***********
        no_watchlist = (TextView) view.findViewById(R.id.no_watchlist);
        if(!SingletonRestClient.getInstance().watchlist.isEmpty()) {
            no_watchlist.setVisibility(View.GONE);
        }
        watchlist_button = (Button) view.findViewById(R.id.watchlist_button);
        watchlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserListAPI("watchlist", 1, R.string.watchlist_list);
            }
        });

        watchlist_recycler = (RecyclerView) view.findViewById(R.id.watchlist_recycler);
        LinearLayoutManager watchlistLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        watchlist_recycler.setLayoutManager(watchlistLayoutManager);
        if(SingletonRestClient.getInstance().watchlistAdapter == null) {
            SingletonRestClient.getInstance().watchlistAdapter = new MoviesUserListAdapter(SingletonRestClient.getInstance().watchlist);
        }
        watchlist_recycler.setAdapter(SingletonRestClient.getInstance().watchlistAdapter);
        watchlist_recycler.addItemDecoration(paddingItemDecoration);



        // *********** FAVOURITE_LIST ***********
        no_favourite_list = (TextView) view.findViewById(R.id.no_favourite_list);
        if(!SingletonRestClient.getInstance().favourite_list.isEmpty()) {
            no_favourite_list.setVisibility(View.GONE);
        }
        favourite_button = (Button) view.findViewById(R.id.favourite_button);
        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserListAPI("favourite", 1, R.string.favourite_list);
            }
        });

        favourite_list_recycler = (RecyclerView) view.findViewById(R.id.favourite_list_recycler);
        LinearLayoutManager favouriteListLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        favourite_list_recycler.setLayoutManager(favouriteListLayoutManager);
        if(SingletonRestClient.getInstance().favouriteListAdapter == null) {
            SingletonRestClient.getInstance().favouriteListAdapter = new MoviesUserListAdapter(SingletonRestClient.getInstance().favourite_list);
        }
        favourite_list_recycler.setAdapter(SingletonRestClient.getInstance().favouriteListAdapter);
        favourite_list_recycler.addItemDecoration(paddingItemDecoration);



        // *********** BLACKLIST ***********
        no_blacklist = (TextView) view.findViewById(R.id.no_blacklist);
        if(!SingletonRestClient.getInstance().blacklist.isEmpty()) {
            no_blacklist.setVisibility(View.GONE);
        }

        blacklist_button = (Button) view.findViewById(R.id.blacklist_button);
        blacklist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserListAPI("blacklist", 1, R.string.blacklist_list);
            }
        });

        blacklist_recycler = (RecyclerView) view.findViewById(R.id.blacklist_recycler);
        LinearLayoutManager blacklistLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        blacklist_recycler.setLayoutManager(blacklistLayoutManager);
        if(SingletonRestClient.getInstance().blacklistAdapter == null) {
            SingletonRestClient.getInstance().blacklistAdapter = new MoviesUserListAdapter(SingletonRestClient.getInstance().blacklist);
        }
        blacklist_recycler.setAdapter(SingletonRestClient.getInstance().blacklistAdapter);
        blacklist_recycler.addItemDecoration(paddingItemDecoration);

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
                        Toast.makeText(getActivity(), getString(R.string.no_movies_list), Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        // Params: page API result
        getUserList.execute(page);
    }


}
