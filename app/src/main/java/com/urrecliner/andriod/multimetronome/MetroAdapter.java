package com.urrecliner.andriod.multimetronome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
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

import static com.urrecliner.andriod.multimetronome.Vars.beep1Medias;
import static com.urrecliner.andriod.multimetronome.Vars.beep2Medias;
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
import static com.urrecliner.andriod.multimetronome.Vars.oneMedias;
import static com.urrecliner.andriod.multimetronome.Vars.soundMedias;
import static com.urrecliner.andriod.multimetronome.Vars.soundVolumes;
import static com.urrecliner.andriod.multimetronome.Vars.tempoLists;
import static com.urrecliner.andriod.multimetronome.Vars.tempos;
import static com.urrecliner.andriod.multimetronome.Vars.utils;


public class MetroAdapter extends RecyclerView.Adapter<MetroAdapter.CustomViewHolder> {

//    final static String logId = "adapter";
    private ImageView [] mivDots;
    private int [] tagDots;
    private int interval;
    private ImageView nowGo, nowStop;
    private TypedArray soundTypes;

    MetroAdapter() {
        mivDots = new ImageView[dotRids.length];
        tagDots = new int[dotRids.length];
        soundTypes = mActivity.getResources().obtainTypedArray(R.array.soundType);
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
                    int hanaBeep = (metros.get(mPos).getHanaBeep() + 1) % 4;
                    metros.get(mPos).setHanaBeep(hanaBeep);
                    utils.saveSharedPrefTables();
                    ivHanaBeep.setImageResource(soundTypes.getResourceId(hanaBeep, 0));
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
                            .setPositiveButton("이 박자로 하지요",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    metros.get(mPos).setMeter(wheelIdx);
                                    utils.saveSharedPrefTables();
                                    tvMeter.setText(meterTexts[wheelIdx]);
                                    tvMeter.invalidate();
                                    metroAdapter.notifyItemChanged(mPos);
                                }
                            })
                            .setNegativeButton("아니", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { }
                    });
                    final android.support.v7.app.AlertDialog dialog = builder.create();
                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(24);
                        }
                    });
                    dialog.show();
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
                            .setPositiveButton("이 빠르기로 하지요",new DialogInterface.OnClickListener() {
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
                                public void onClick(DialogInterface dialog, int which) { }
                            });
                   final android.support.v7.app.AlertDialog dialog = builder.create();
                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(24);
                        }
                    });
                    dialog.show();
                }
            });

            ivGo = itemView.findViewById(R.id.go);
            ivGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPos = getAdapterPosition();
                    for (int idx = 0; idx < dotRids.length; idx++) {
                        ivDots[idx] = itemView.findViewById(dotRids[idx]);
                        mivDots[idx] = itemView.findViewById(dotRids[idx]);
                    }
                    buildTagDots(metros.get(mPos).getMeter());
                    isRunning = true;
                    ivGo.setVisibility(View.GONE);
                    ivStop.setVisibility(View.VISIBLE);
                    nowGo = ivGo;
                    nowStop = ivStop;
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
            case 1:
                setupOneHana(meter, hanaBeep);
                break;
            case 2:
            case 3:
                setupBeep12(meter, hanaBeep);
                break;
        }
    }

    private void setupOneHana(int meter, int hanaBeep) {
        for (int i = 0; i < loopCount; i++) {
            int dot = meterBeats[meter][i];
            if (dot > 10) {
                soundMedias[i] = (hanaBeep == 0) ? hanaMedias[dot - 10] : oneMedias[dot - 10];
                soundVolumes[i] = highVolume;
            }
            else {
                soundMedias[i] = (hanaBeep == 0) ? hanaMedias[dot] : oneMedias[dot];
                soundVolumes[i] = lowVolume;
            }
        }
    }

    private void setupBeep12(int meter, int hanaBeep) {
        for (int i = 0; i < loopCount; i++) {
            int dot = meterBeats[meter][i];
            if (dot == 11) {
                soundMedias[i] = (hanaBeep == 2) ? beep1Medias[1] : beep2Medias[1];
                soundVolumes[i] = highVolume;
            }
            else if (dot == 12 || dot == 13 || dot == 14) {
                soundMedias[i] = (hanaBeep == 2) ? beep1Medias[2] : beep2Medias[2];
                soundVolumes[i] = highVolume;
            }
            else {
                soundMedias[i] = (hanaBeep == 2) ? beep1Medias[3] : beep2Medias[3];
                soundVolumes[i] = lowVolume;
            }
        }
    }

//    private final float highVolume = (float) (1 - (Math.log(1) / Math.log(10)));
//    private final float lowVolume = (float) (1 - (Math.log(4) / Math.log(10)));
    private final float highVolume = 1.0f;
    private final float lowVolume = 0.4f;

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
        holder.ivHanaBeep.setImageResource(soundTypes.getResourceId(hanaBeep, 0));
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
