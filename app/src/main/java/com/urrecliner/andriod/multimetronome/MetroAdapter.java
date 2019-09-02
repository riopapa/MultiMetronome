package com.urrecliner.andriod.multimetronome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.urrecliner.andriod.multimetronome.Vars.beepMedias;
import static com.urrecliner.andriod.multimetronome.Vars.dotRids;
import static com.urrecliner.andriod.multimetronome.Vars.hanaMedias;
import static com.urrecliner.andriod.multimetronome.Vars.isRunning;
import static com.urrecliner.andriod.multimetronome.Vars.mActivity;
import static com.urrecliner.andriod.multimetronome.Vars.mPos;
import static com.urrecliner.andriod.multimetronome.Vars.meterBeats;
import static com.urrecliner.andriod.multimetronome.Vars.meterLists;
import static com.urrecliner.andriod.multimetronome.Vars.meterTexts;
import static com.urrecliner.andriod.multimetronome.Vars.metroAdapter;
import static com.urrecliner.andriod.multimetronome.Vars.metros;
import static com.urrecliner.andriod.multimetronome.Vars.soundMedias;
import static com.urrecliner.andriod.multimetronome.Vars.tempoLists;
import static com.urrecliner.andriod.multimetronome.Vars.tempos;
import static com.urrecliner.andriod.multimetronome.Vars.utils;


public class MetroAdapter extends RecyclerView.Adapter<MetroAdapter.CustomViewHolder> {

    final static String logId = "adapter";
    private ImageView [] mivDots;
    private int [] tagDots;
    private int interval;
    private ImageView nowGo;
    private ImageView nowHanaBeep;
    private TextView nowMeter;
    private TextView nowTempo;

    MetroAdapter() {
        mivDots = new ImageView[dotRids.length];
        tagDots = new int[dotRids.length];
    }

    static TextView currTVTempo, currTVMeter;

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView ivHanaBeep;
        TextView tvMeter;
        TextView tvTempo;
        ImageView ivGo;
        ImageView [] ivDots = new ImageView[dotRids.length];
        ConstraintLayout constraintLayout;
        int wheelIdx;

        CustomViewHolder(View view) {
            super(view);
            for (int i = 0; i < dotRids.length; i++) {
                ivDots[i] = itemView.findViewById(dotRids[i]);
            }
//            utils.log(logId," view"+view.toString());
            ivHanaBeep = view.findViewById(R.id.hanaBeep);
            ivHanaBeep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopBeatPlay();
                    mPos = getAdapterPosition();
                    int hanaBeep = metros.get(mPos).getHanaBeep() + 1;
                    if (hanaBeep > 2)
                        hanaBeep = 0;
                    metros.get(mPos).setHanaBeep(hanaBeep);
                    utils.saveSharedPrefTables();
                    if (hanaBeep == 0)
                        ivHanaBeep.setImageResource(R.mipmap.say_hana);
                    else if (hanaBeep == 1)
                        ivHanaBeep.setImageResource(R.mipmap.say_ticktick);
                    else {
                        ivHanaBeep.setImageResource(R.mipmap.say_tooktook);
                    }
                    ivHanaBeep.invalidate();
                }
            });

            tvMeter = view.findViewById(R.id.meter);
            tvMeter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopBeatPlay();
                    mPos = getAdapterPosition();
                    final Metro metro = metros.get(mPos);
                    currTVMeter = tvMeter;
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
                    LayoutInflater inflater = mActivity.getLayoutInflater();
                    View theView = inflater.inflate(R.layout.wheel_view, null);
                    WheelView wV = theView.findViewById(R.id.wheel);
                    wV.setItems(meterLists);
                    wV.selectIndex(metro.getMeter());
                    wV.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
                        @Override
                        public void onWheelItemSelected(WheelView wheelView, int position) {
                            wheelIdx = position;
                        }
                        @Override
                        public void onWheelItemChanged(WheelView wheelView, int position) {
                        }
                    });

                    builder.setView(theView)
                            .setPositiveButton("이걸로",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    metros.get(mPos).setMeter(wheelIdx);
                                    utils.saveSharedPrefTables();
                                    tvMeter.setText(meterTexts[wheelIdx]);
                                    tvMeter.invalidate();
                                    metroAdapter.notifyItemChanged(mPos);
                                }
                            }).setNegativeButton("아니", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
            });

            tvTempo = view.findViewById(R.id.tempo);
            tvTempo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopBeatPlay();
                    mPos = getAdapterPosition();
                    final Metro metro = metros.get(mPos);
                    currTVTempo = tvTempo;
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
                    LayoutInflater inflater = mActivity.getLayoutInflater();
                    View theView = inflater.inflate(R.layout.wheel_view, null);

                    WheelView wV = theView.findViewById(R.id.wheel);
                    wV.setItems(tempoLists);
                    int tempo = metro.getTempo();
                    for (int i = 0; i < tempos.length; i++)
                        if (tempos[i] == tempo)
                            wV.selectIndex(i);

                    wV.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
                        @Override
                        public void onWheelItemSelected(WheelView wheelView, int position) {
                            wheelIdx = position;
                        }
                        @Override
                        public void onWheelItemChanged(WheelView wheelView, int position) {
                        }
                    });

                    builder.setView(theView)
                            .setPositiveButton("이걸로",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String s;
                                    int val = tempos[wheelIdx];
                                    metros.get(mPos).setTempo(val);
                                    utils.saveSharedPrefTables();
                                    s = ""+val;
                                    tvTempo.setText(s);
                                    tvTempo.invalidate();

                                }
                            })
                            .setNegativeButton("아니", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
            });

            ivGo = itemView.findViewById(R.id.go);
            ivGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isRunning)
                        stopBeatPlay();
                    else {
                        mPos = getAdapterPosition();
                        nowHanaBeep = view.findViewById(R.id.hanaBeep);
                        nowMeter =  view.findViewById(R.id.meter);
                        nowTempo =  view.findViewById(R.id.tempo);
                        nowGo = view.findViewById(R.id.go);
                        for (int idx = 0; idx < dotRids.length; idx++) {
                            ivDots[idx] = itemView.findViewById(dotRids[idx]);
                            mivDots[idx] = itemView.findViewById(dotRids[idx]);
                        }
                        int meter = metros.get(mPos).getMeter();
                        buildTagDots(meter);
                        isRunning = true;
                        nowGo.setImageResource(R.mipmap.go_red);
                        setupSoundTable();
                        startBeatPlay();
                    }
                }
            });

            constraintLayout = view.findViewById(R.id.oneMetro);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    confirmDelete(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    private void setupSoundTable() {

        int hanaBeep = metros.get(mPos).getHanaBeep();
        int meter = metros.get(mPos).getMeter();
        int count = meterBeats[meter].length;
        interval = 60000 / metros.get(mPos).getTempo();
        soundMedias = new MediaPlayer[count];
        switch (hanaBeep) {
            case 0:
                setupHanaTable(meter, count);
                break;
            case 1:
                setupBeepTable(meter, count);
                break;
            case 2:
                setupBeep2Table(meter, count);
                break;
        }
    }

    private void setupHanaTable(int meter, int count) {
        for (int i = 0; i < count; i++) {
            int dot = meterBeats[meter][i];
            if (dot == 11) {
                soundMedias[i] = hanaMedias[1];
            }
            else if (dot == 12) {
                soundMedias[i] = hanaMedias[2];
            }
            else if (dot == 13) {
                soundMedias[i] = hanaMedias[3];
            }
            else if (dot == 14) {
                soundMedias[i] = hanaMedias[4];
            }
            else {
                soundMedias[i] = hanaMedias[dot];
            }
        }
    }

    private void setupBeepTable(int meter, int count) {
        for (int i = 0; i < count; i++) {
            int dot = meterBeats[meter][i];
            if (dot == 11) {
                soundMedias[i] = beepMedias[1];
            }
            else if (dot == 12 || dot == 13 || dot == 14) {
                soundMedias[i] = beepMedias[2];
            }
            else {
                soundMedias[i] = beepMedias[9];
            }
        }
    }

    private void setupBeep2Table(int meter, int count) {
        for (int i = 0; i < count; i++) {
            int dot = meterBeats[meter][i];
            if (dot == 11) {
                soundMedias[i] = beepMedias[5];
            }
            else if (dot == 12 || dot == 13 || dot == 14) {
                soundMedias[i] = beepMedias[4];
            }
            else {
                soundMedias[i] = beepMedias[9];
            }
        }
    }

    private BeatPlay beatPlay = null;
    private void startBeatPlay() {
        nowGo.setEnabled(false);
        int meter = metros.get(mPos).getMeter();
        beatPlay = new BeatPlay(interval, tagDots, mivDots, meterBeats[meter]);
//        utils.log("mPos "+mPos,">> "+metros.get(mPos).getMeter());
        beatPlay.start();
        nowGo.setEnabled(true);
    }

    void stopBeatPlay() {
        if (isRunning) {
            beatPlay.interrupt();
            nowGo.setImageResource(R.mipmap.go_green);
            metroAdapter.notifyItemChanged(mPos);
//            utils.log("mPos "+mPos,">> "+metros.get(mPos).getMeter());
            isRunning = false;
        }
    }

    private void confirmDelete(int position) {
        final int pos = position;
        Metro metro = metros.get(position);
        String s = "삭제하려면 [필요없슈] 를 누르세요 \n"+meterTexts[metro.getMeter()]+" tempo("+ metro.getTempo()+")";
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Delete Metronome?");
        builder.setMessage(s);
        builder.setPositiveButton("필요없슈",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeItemView(pos);
                    }
                });
        builder.setNegativeButton("아뉴",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    private void removeItemView(int position) {
        metros.remove(position);
        metroAdapter.notifyItemRemoved(position);
        metroAdapter.notifyItemRangeChanged(position, metros.size());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.one_metro, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int pos) {

        int dotMax = dotRids.length;
        Metro metro = metros.get(pos);
        int meter = metro.getMeter();
        int hanaBeep = metro.getHanaBeep();
        int tempo = metro.getTempo();
        int idx;

        int color = (pos%2 == 0) ? Color.parseColor("#EFF0F1") : Color.parseColor("#eddada");
        holder.constraintLayout.setBackgroundColor(color);
        buildTagDots(meter);
        int size = meterBeats[meter].length;
        for (idx = 0; idx < size; idx++) {
                holder.ivDots[idx].setImageResource(tagDots[idx]);
            holder.ivDots[idx].setVisibility(View.VISIBLE);
        }
        while (idx < dotMax) {
            holder.ivDots[idx].setVisibility(View.GONE);
            idx++;
        }
        switch (hanaBeep) {
            case 0:
                holder.ivHanaBeep.setImageResource(R.mipmap.say_hana);
                break;
            case 1:
                holder.ivHanaBeep.setImageResource(R.mipmap.say_ticktick);
                break;
            case 2:
                holder.ivHanaBeep.setImageResource(R.mipmap.say_tooktook);
                break;
        }
        holder.tvMeter.setText(meterTexts[meter]);
        holder.tvTempo.setText(String.valueOf(tempo));
    }

    private void buildTagDots(int meter) {
        int size = meterBeats[meter].length;
        for (int idx = 0; idx < size; idx++) {
            int dot = meterBeats[meter][idx];
            if (dot == 11) {
                tagDots[idx] = R.mipmap.circle_red;
            }
            else if (dot == 12 || dot == 13 || dot == 14) {
                tagDots[idx] = R.mipmap.circle_red2;
            }
            else {
                tagDots[idx] = R.mipmap.circle_pink;
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != metros ? metros.size() : 0);
    }
}
