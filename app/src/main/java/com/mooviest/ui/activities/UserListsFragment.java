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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //UserListsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserListsFragment extends Fragment {

    private Button favourite_button;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public UserListsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserListsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserListsFragment newInstance(String param1, String param2) {
        UserListsFragment fragment = new UserListsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_lists, container, false);

        favourite_button = (Button) view.findViewById(R.id.favourite_button);
        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserListAPI("favourite_list", 1);
            }
        });

        return view;

    }

    private void getUserListAPI(String list_name, int page){
        GetUserList getUserList = new GetUserList(list_name){
            @Override
            protected void onPostExecute(MooviestApiResult result) {
                super.onPostExecute(result);
                if(result!=null) {
                    if(result.getCount() >= 1) {
                        SingletonRestClient.getInstance().movies_list = result.getMovies();

                        Intent i = new Intent(getActivity(), MoviesListActivity.class);
                        i.putExtra("TITLE", R.string.favourite_list);
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

    /*

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    */

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
     */
}
