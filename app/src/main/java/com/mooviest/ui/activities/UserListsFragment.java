package com.mooviest.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mooviest.R;
import com.mooviest.ui.rest.MooviestApiResult;
import com.mooviest.ui.rest.SingletonRestClient;
import com.mooviest.ui.tasks.GetUserList;


public class UserListsFragment extends Fragment {

    private Button favourite_button;
    private Button seen_button;
    private Button watchlist_button;


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

        seen_button = (Button) view.findViewById(R.id.seen_button);
        seen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserListAPI("seen_list", 1, R.string.seen_list);
            }
        });

        watchlist_button = (Button) view.findViewById(R.id.watchlist_button);
        watchlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserListAPI("watchlist", 1, R.string.watchlist);
            }
        });

        favourite_button = (Button) view.findViewById(R.id.favourite_button);
        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserListAPI("favourite_list", 1, R.string.favourite_list);
            }
        });

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
                        Toast.makeText(getActivity(), "No movies list", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        // Params: page API result
        getUserList.execute(page);
    }


}
