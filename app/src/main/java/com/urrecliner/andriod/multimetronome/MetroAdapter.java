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
import static com.urrecliner.andriod.multimetronome.Vars.loopCount;
import static com.urrecliner.andriod.multimetronome.Vars.mActivity;
import static com.urrecliner.andriod.multimetronome.Vars.mContext;
import static com.urrecliner.andriod.multimetronome.Vars.mPos;
import static com.urrecliner.andriod.multimetronome.Vars.meterBeats;
import static com.urrecliner.andriod.multimetronome.Vars.meterLists;
import static com.urrecliner.andriod.multimetronome.Vars.meterTexts;
import static com.urrecliner.andriod.multimetronome.Vars.metroAdapter;
import static com.urrecliner.andriod.multimetronome.Vars.metros;
import static com.urrecliner.andriod.multimetronome.Vars.soundMedias;
import static com.urrecliner.andriod.multimetronome.Vars.soundVolumes;
import static com.urrecliner.andriod.multimetronome.Vars.tempoLists;
import static com.urrecliner.andriod.multimetronome.Vars.tempos;
import static com.urrecliner.andriod.multimetronome.Vars.utils;


public class MetroAdapter extends RecyclerView.Adapter<MetroAdapter.CustomViewHolder> {

    final static String logId = "adapter";
    private ImageView [] mivDots;
    private int [] tagDots;
    private int interval;
    private ImageView nowGo, nowStop;

    MetroAdapter() {
        mivDots = new ImageView[dotRids.length];
        tagDots = new int[dotRids.length];
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView ivHanaBeep;
        TextView tvMeter;
        TextView tvTempo;
        ImageView ivGo, ivStop;
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
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
//                    LayoutInflater inflater = mActivity.getLayoutInflater();
                    View theView = View.inflate(mContext, R.layout.wheel_view, null);
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
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
//                    LayoutInflater inflater = mActivity.getLayoutInflater();
                    View theView = View.inflate(mContext, R.layout.wheel_view, null);

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
                    mPos = getAdapterPosition();
//                    nowGo = view.findViewById(R.id.go);
//                    nowStop = view.findViewById(R.id.stop);
                    for (int idx = 0; idx < dotRids.length; idx++) {
                        ivDots[idx] = itemView.findViewById(dotRids[idx]);
                        mivDots[idx] = itemView.findViewById(dotRids[idx]);
                    }
                    int meter = metros.get(mPos).getMeter();
                    buildTagDots(meter);
                    isRunning = true;
                    ivGo.setVisibility(View.GONE);
                    ivStop.setVisibility(View.VISIBLE);
                    nowGo = ivGo;
                    nowStop = ivStop;
//                    nowGo.setImageResource(R.mipmap.go_red);
                    setupSoundTable();
                    startBeatPlay();
                }
            });

            ivStop = itemView.findViewById(R.id.stop);
            ivStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopBeatPlay();
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
        loopCount = meterBeats[meter].length;
        interval = 60000 / metros.get(mPos).getTempo();
        soundMedias = new MediaPlayer[loopCount];
        soundVolumes = new float[loopCount];
        switch (hanaBeep) {
            case 0:
                setupHanaTable(meter);
                break;
            case 1:
                setupBeepTick(meter);
                break;
            case 2:
                setupBeepTook(meter);
                break;
        }
    }

    private void setupHanaTable(int meter) {
        for (int i = 0; i < loopCount; i++) {
            int dot = meterBeats[meter][i];
            if (dot == 11) {
                soundMedias[i] = hanaMedias[1];
                soundVolumes[i] = highVolume;
            }
            else if (dot == 12) {
                soundMedias[i] = hanaMedias[2];
                soundVolumes[i] = highVolume;
            }
            else if (dot == 13) {
                soundMedias[i] = hanaMedias[3];
                soundVolumes[i] = highVolume;
            }
            else if (dot == 14) {
                soundMedias[i] = hanaMedias[4];
                soundVolumes[i] = highVolume;
            }
            else {
                soundMedias[i] = hanaMedias[dot];
                soundVolumes[i] = lowVolume;
            }
        }
    }

    private void setupBeepTick(int meter) {
        for (int i = 0; i < loopCount; i++) {
            int dot = meterBeats[meter][i];
            if (dot == 11) {
                soundMedias[i] = beepMedias[5];
                soundVolumes[i] = highVolume;
            }
            else if (dot == 12 || dot == 13 || dot == 14) {
                soundMedias[i] = beepMedias[2];
                soundVolumes[i] = highVolume;
            }
            else {
                soundMedias[i] = beepMedias[9];
                soundVolumes[i] = lowVolume;
            }
        }
    }

    private void setupBeepTook(int meter) {
        for (int i = 0; i < loopCount; i++) {
            int dot = meterBeats[meter][i];
            if (dot == 11) {
                soundMedias[i] = beepMedias[7];
                soundVolumes[i] = highVolume;
            }
            else if (dot == 12 || dot == 13 || dot == 14) {
                soundMedias[i] = beepMedias[6];
                soundVolumes[i] = highVolume;
            }
            else {
                soundMedias[i] = beepMedias[9];
                soundVolumes[i] = lowVolume;
            }
        }
    }

    private final float highVolume = (float) (1 - (Math.log(1) / Math.log(10)));
    private final float lowVolume = (float) (1 - (Math.log(5) / Math.log(10)));

    private BeatPlay beatPlay = null;
    private void startBeatPlay() {
        nowGo.setEnabled(false);
        beatPlay = new BeatPlay(interval, tagDots, mivDots);
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
            nowStop.setVisibility(View.GONE);
            nowGo.setVisibility(View.VISIBLE);
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
        holder.ivStop.setVisibility(View.GONE);
    }

    private void buildTagDots(int meter) {
        int size = meterBeats[meter].length;
        for (int idx = 0; idx < size; idx++) {
            int dot = meterBeats[meter][idx];
            if (dot == 11) {
                tagDots[idx] = R.mipmap.circle_red_big;
            }
            else if (dot == 12 || dot == 13 || dot == 14) {
                tagDots[idx] = R.mipmap.circle_red_yellow;
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