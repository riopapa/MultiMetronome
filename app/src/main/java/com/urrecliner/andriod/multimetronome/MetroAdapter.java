package com.urrecliner.andriod.multimetronome;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.urrecliner.andriod.multimetronome.Vars.dotRids;
import static com.urrecliner.andriod.multimetronome.Vars.gxIdx;
import static com.urrecliner.andriod.multimetronome.Vars.meterTexts;


public class MetroAdapter extends RecyclerView.Adapter<MetroAdapter.CustomViewHolder> {

    private ArrayList<MetroInfo> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSoundType;
        TextView tvMeter;
        TextView tvSpeed;
        ImageView ivGo;
        ImageView [] ivDots = new ImageView[dotRids.length];
        ConstraintLayout recyclerView;
//        RecyclerView recyclerView;

        public CustomViewHolder(View view) {
            super(view);
            this.ivSoundType = view.findViewById(R.id.soundType);
            this.tvMeter =  view.findViewById(R.id.meter);
            this.tvSpeed =  view.findViewById(R.id.speed);
            this.ivGo = view.findViewById(R.id.go);
            for (int i = 0; i < dotRids.length; i++) {
                ivDots[i] = itemView.findViewById(dotRids[i]);
            }
            tvMeter = itemView.findViewById(R.id.meter);
            ivGo = itemView.findViewById(R.id.go);
            recyclerView = itemView.findViewById(R.id.oneMetro);
//            constraintLayout = (ConstraintLayout) mContext.getResources().getLayout(R.id.oneMetro);
        }
    }

    public MetroAdapter(ArrayList<MetroInfo> list) {
        this.mList = list;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.one_metro, viewGroup, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        gxIdx = position;

        int color = (gxIdx%2 == 0) ? Color.parseColor("#EFF0F1") : Color.parseColor("#eddada");

        holder.recyclerView.setBackgroundColor(color);

        for (int i = 0; i < dotRids.length; i++) {
            switch (i%4) {
                case 0: holder.ivDots[i].setImageResource(R.mipmap.circle_red); break;
                case 1:
                    holder.ivDots[i].setImageResource(R.mipmap.circle_red2);
                    break;
                case 2:
                case 3:
                    holder.ivDots[i].setVisibility(View.GONE);
                    break;
            }
        }

        int i = mList.get(position).getSoundType();
        if (i == 0)
            holder.ivSoundType.setImageResource(R.mipmap.say_dingdong);
        else if (i == 1)
            holder.ivSoundType.setImageResource(R.mipmap.say_hana);
        holder.tvMeter.setText(meterTexts[mList.get(position).getMeter()]);
        holder.tvSpeed.setText(""+mList.get(position).getSpeed());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
