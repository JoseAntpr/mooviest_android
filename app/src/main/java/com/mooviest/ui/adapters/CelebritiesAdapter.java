package com.mooviest.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooviest.R;
import com.mooviest.ui.models.Participation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jesus on 3/9/16.
 *
 * Adaptador usado para las celebrities usadas en el tab cast de movie_detail
 */
public class CelebritiesAdapter extends RecyclerView.Adapter<CelebritiesAdapter.ViewHolder> {

    private final List<Participation> participations;

    public CelebritiesAdapter(List<Participation> participations) {
        this.participations = participations;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView celebrity_image;
        public TextView celebrity_name;
        public TextView celebrity_character;

        public ViewHolder(View v) {
            super(v);

            celebrity_image = (ImageView) v.findViewById(R.id.celebrity_image);
            celebrity_name = (TextView) v.findViewById(R.id.celebrity_name);
            celebrity_character = (TextView) v.findViewById(R.id.celebrity_character);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.celebrity_cast_grid, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Participation participation = participations.get(position);

        Picasso p = Picasso.with(viewHolder.itemView.getContext());

        String image = participation.getCelebrity().getImage();
        if(image.length() > 0){
            p.load("https://img.tviso.com/XX/face/w175"+image).fit().centerCrop().into(viewHolder.celebrity_image);
        }else{
            p.load(R.drawable.face_175).fit().centerCrop().into(viewHolder.celebrity_image);
        }

        viewHolder.celebrity_name.setText(participation.getCelebrity().getName());
        viewHolder.celebrity_character.setText(participation.getCharacter());
    }

    @Override
    public int getItemCount() {
        return participations.size();
    }
}
