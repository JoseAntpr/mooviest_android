package com.mooviest.ui.activities;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mooviest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cardstack.SwipeDeck;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {//@link OneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SwipeDeck cardStack;
    private SwipeDeckAdapter adapter;
    private ArrayList<String> testData;

    Bundle savedInstanceState;
    private Resources resources;
    private int id_image;

    //private OnFragmentInteractionListener mListener;

    public OneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OneFragment newInstance(String param1, String param2) {
        OneFragment fragment = new OneFragment();
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

        this.savedInstanceState=savedInstanceState;
        this.resources=getContext().getResources();
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_one, container, false);

        //**** CARDSTACK ******
        cardStack = (SwipeDeck) v.findViewById(R.id.swipe_deck);
        cardStack.setHardwareAccelerationEnabled(true);
        testData = new ArrayList<>();
        getData();

        adapter = new SwipeDeckAdapter(testData, getContext());
        cardStack.setAdapter(adapter);

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
            }

            @Override
            public void cardSwipedUp(int position) {
                Log.i("MainActivity", "card was swiped Up, position in adapter: " + position);
            }

            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");
            }

            @Override
            public void cardActionDown() {
                Log.i("MainActivity", "cardActionDown");
            }

            @Override
            public void cardActionUp() {
                Log.i("MainActivity", "cardActionUp");
            }

        });

        cardStack.setLeftImage(R.id.left_image);
        cardStack.setRightImage(R.id.right_image);

        ImageButton btn = (ImageButton) v.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardLeft(180);

            }
        });
        ImageButton btn2 = (ImageButton) v.findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardRight(260);
            }
        });
        ImageButton btn4 = (ImageButton) v.findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardUp(150);
            }
        });

        ImageButton btn3 = (ImageButton) v.findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testData.add("hangover");
//                ArrayList<String> newData = new ArrayList<>();
//                newData.add("some new data");
//                newData.add("some new data");
//                newData.add("some new data");
//                newData.add("some new data");
//
//                SwipeDeckAdapter adapter = new SwipeDeckAdapter(newData, context);
//                cardStack.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });


        return v;


    }

    private void getData(){
        testData.add("the_hobbit_2");
        testData.add("mothers_day");
        testData.add("the_revenant");
        testData.add("shrek");
        testData.add("the_conjuring");
        testData.add("titanic");
        testData.add("avengers_2");
        testData.add("as_good_as_it_gets");
        testData.add("hangover");
        testData.add("no_strings_attached");
        testData.add("alvin_and_the_chipmunks");
        testData.add("perdiendo_el_norte");
        testData.add("the_purge_election_year");
    }



    public class SwipeDeckAdapter extends BaseAdapter {

        private List<String> data;
        private Context context;

        public SwipeDeckAdapter(List<String> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = getLayoutInflater(savedInstanceState);
                // normally use a viewholder
                v = inflater.inflate(R.layout.card, parent, false);
            }
            //((TextView) v.findViewById(R.id.textView2)).setText(data.get(position));
            ImageView imageView = (ImageView) v.findViewById(R.id.offer_image);

            //GET IMAGE
            String item = (String)getItem(position);
            Picasso.with(context).load(resources.getIdentifier(item,"drawable",context.getPackageName())).fit().centerCrop().into(imageView);
            Picasso.with(context).setIndicatorsEnabled(false);
            //TextView textView = (TextView) v.findViewById(R.id.sample_text);

            //textView.setText(item);

            /* Hacer click en una tarjeta
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Layer type: ", Integer.toString(v.getLayerType()));
                    Log.i("Hwardware Accel type:", Integer.toString(View.LAYER_TYPE_HARDWARE));
                    Intent i = new Intent(v.getContext(), BlankActivity.class);
                    v.getContext().startActivity(i);
                }
            });
            */
            return v;
        }
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
