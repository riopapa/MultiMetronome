package com.urrecliner.andriod.multimetronome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.urrecliner.andriod.multimetronome.Vars.beepLoads;
import static com.urrecliner.andriod.multimetronome.Vars.dotRids;
import static com.urrecliner.andriod.multimetronome.Vars.hanaLoads;
import static com.urrecliner.andriod.multimetronome.Vars.isRunning;
import static com.urrecliner.andriod.multimetronome.Vars.mActivity;
import static com.urrecliner.andriod.multimetronome.Vars.mPos;
import static com.urrecliner.andriod.multimetronome.Vars.meterDots;
import static com.urrecliner.andriod.multimetronome.Vars.meterLists;
import static com.urrecliner.andriod.multimetronome.Vars.meterTexts;
import static com.urrecliner.andriod.multimetronome.Vars.metroAdapter;
import static com.urrecliner.andriod.multimetronome.Vars.metroInfos;
import static com.urrecliner.andriod.multimetronome.Vars.soundLoads;
import static com.urrecliner.andriod.multimetronome.Vars.tempoLists;
import static com.urrecliner.andriod.multimetronome.Vars.tempos;
import static com.urrecliner.andriod.multimetronome.Vars.utils;
import static com.urrecliner.andriod.multimetronome.Vars.volumeLoads;


public class MetroAdapter extends RecyclerView.Adapter<MetroAdapter.CustomViewHolder> {

    private ImageView [] mivDots;
    private int [] tagDots;
    private int interval;
    private ImageView nowGo;

    MetroAdapter() {
        mivDots = new ImageView[dotRids.length];
        tagDots = new int[dotRids.length];
    }

    static TextView currTVTempo, currTVMeter;

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSoundType;
        TextView tvMeter;
        TextView tvTempo;
        ImageView ivGo;
        ImageView [] ivDots = new ImageView[dotRids.length];
        ConstraintLayout constraintLayout;
        private ImageView nowSoundType;
        private TextView nowMeter;
        private TextView nowTempo;
        int wheelIdx;

        CustomViewHolder(View view) {
            super(view);
            for (int i = 0; i < dotRids.length; i++) {
                ivDots[i] = itemView.findViewById(dotRids[i]);
            }
            ivSoundType = view.findViewById(R.id.soundType);
            tvMeter = itemView.findViewById(R.id.meter);
            tvTempo =  view.findViewById(R.id.tempo);
            ivGo = itemView.findViewById(R.id.go);
            constraintLayout = itemView.findViewById(R.id.oneMetro);

            ivSoundType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopBeatPlay();
                    mPos = getAdapterPosition();
                    int soundType = metroInfos.get(mPos).getSoundType() + 1;
                    if (soundType > 2)
                        soundType = 0;
                    metroInfos.get(mPos).setSoundType(soundType);
                    utils.saveTables();
                    if (soundType == 0)
                        ivSoundType.setImageResource(R.mipmap.say_hana);
                    else if (soundType == 1)
                        ivSoundType.setImageResource(R.mipmap.say_ticktick);
                    else {
                        ivSoundType.setImageResource(R.mipmap.say_tooktook);
                    }
                    ivSoundType.invalidate();
                }
            });

            tvMeter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopBeatPlay();
                    mPos = getAdapterPosition();
                    final MetroInfo metroInfo = metroInfos.get(mPos);
                    currTVMeter = tvMeter;
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
                    LayoutInflater inflater = mActivity.getLayoutInflater();
                    View theView = inflater.inflate(R.layout.wheel_view, null);
                    WheelView wV = theView.findViewById(R.id.wheel);
                    wV.setItems(meterLists);
                    wV.selectIndex(metroInfo.getMeter());
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
                                    metroInfos.get(mPos).setMeter(wheelIdx);
                                    utils.saveTables();
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

            tvTempo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopBeatPlay();
                    mPos = getAdapterPosition();
                    final MetroInfo metroInfo = metroInfos.get(mPos);
                    currTVTempo = tvTempo;
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
                    LayoutInflater inflater = mActivity.getLayoutInflater();
                    View theView = inflater.inflate(R.layout.wheel_view, null);

                    WheelView wV = theView.findViewById(R.id.wheel);
                    if (wV == null)
                        Log.e("wV"," is null");
                    wV.setItems(tempoLists);
                    int tempo = metroInfo.getTempo();
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
                                    metroInfos.get(mPos).setTempo(val);
                                    utils.saveTables();
                                    s = ""+val;
                                    tvTempo.setText(s);
                                    tvTempo.invalidate();

                                }
                            }).setNegativeButton("아니", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
            });


            ivGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isRunning)
                        stopBeatPlay();
                    else {
                        mPos = getAdapterPosition();
                        nowSoundType = view.findViewById(R.id.soundType);
                        nowMeter =  view.findViewById(R.id.meter);
                        nowTempo =  view.findViewById(R.id.tempo);
                        nowGo = view.findViewById(R.id.go);
                        for (int idx = 0; idx < dotRids.length; idx++) {
                            ivDots[idx] = itemView.findViewById(dotRids[idx]);
                            mivDots[idx] = itemView.findViewById(dotRids[idx]);
                        }
                        int meter = metroInfos.get(mPos).getMeter();
                        buildTagDots(meter);
                        isRunning = true;
                        nowGo.setImageResource(R.mipmap.go_red);
                        setupSoundTable();
                        startBeatPlay();
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    confirmDelete(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    private int setupSoundTable() {

        int soundType = metroInfos.get(mPos).getSoundType();
        interval = 60000 / metroInfos.get(mPos).getTempo();

        switch (soundType) {
            case 0:
                return setupHanaTable();
            case 1:
                return setupBeepTable();
            case 2:
                return setupBeep2Table();
        }
        return 0;
    }

    private int setupHanaTable() {
        int meter = metroInfos.get(mPos).getMeter();
        int count = meterDots[meter].length;
        soundLoads = new int[count];
        volumeLoads = new float[count];
        for (int i = 0; i < count; i++) {
            int dot = meterDots[meter][i];
            if (dot == 11) {
                soundLoads[i] = hanaLoads[1];
                volumeLoads[i] = 1f;
            }
            else if (dot == 12) {
                soundLoads[i] = hanaLoads[2];
                volumeLoads[i] = 1f;
            }
            else if (dot == 13) {
                soundLoads[i] = hanaLoads[3];
                volumeLoads[i] = 1f;
            }
            else if (dot == 14) {
                soundLoads[i] = hanaLoads[4];
                volumeLoads[i] = 1f;
            }
            else {
                soundLoads[i] = hanaLoads[dot];
                volumeLoads[i] = 0.6f;
            }
        }
        return count;
    }

    private int setupBeepTable() {
        int meter = metroInfos.get(mPos).getMeter();
        int count = meterDots[meter].length;
        soundLoads = new int[count];
        volumeLoads = new float[count];
        for (int i = 0; i < count; i++) {
            int dot = meterDots[meter][i];
            if (dot == 11) {
                soundLoads[i] = beepLoads[1];
                volumeLoads[i] = 1f;
            }
            else if (dot == 12 || dot == 13 || dot == 14) {
                soundLoads[i] = beepLoads[2];
                volumeLoads[i] = 1f;
            }
            else {
                soundLoads[i] = beepLoads[4];
                volumeLoads[i] = 0.5f;
            }
        }
        return count;
    }

    private int setupBeep2Table() {
        int meter = metroInfos.get(mPos).getMeter();
        int count = meterDots[meter].length;
        soundLoads = new int[count];
        volumeLoads = new float[count];
        for (int i = 0; i < count; i++) {
            int dot = meterDots[meter][i];
            if (dot == 11) {
                soundLoads[i] = beepLoads[5];
                volumeLoads[i] = 1f;
            }
            else if (dot == 12 || dot == 13 || dot == 14) {
                soundLoads[i] = beepLoads[6];
                volumeLoads[i] = 1f;
            }
            else {
                soundLoads[i] = beepLoads[7];
                volumeLoads[i] = 0.5f;
            }
        }
        return count;
    }

    private BeatPlay beatPlay = null;
    private void startBeatPlay() {
        nowGo.setEnabled(false);
        int meter = metroInfos.get(mPos).getMeter();
        beatPlay = new BeatPlay(interval, tagDots, mivDots, meterDots[meter]);
        beatPlay.start();
        nowGo.setEnabled(true);
    }

    void stopBeatPlay() {
        beatPlay.quitPlay();
        nowGo.setImageResource(R.mipmap.go_green);
    }

    private void confirmDelete(int position) {
        final int pos = position;
        MetroInfo metroInfo = metroInfos.get(position);
        String s = "삭제하려면 [필요없슈] 를 누르세요 \n"+meterTexts[metroInfo.getMeter()]+" tempo("+metroInfo.getTempo()+")";
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
        metroInfos.remove(position);
        metroAdapter.notifyItemRemoved(position);
        metroAdapter.notifyItemRangeChanged(position, metroInfos.size());
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
        MetroInfo metroInfo = metroInfos.get(pos);
        int meter = metroInfo.getMeter();
        int soundType = metroInfo.getSoundType();
        int tempo = metroInfo.getTempo();
        int idx;

        int color = (pos%2 == 0) ? Color.parseColor("#EFF0F1") : Color.parseColor("#eddada");
        holder.constraintLayout.setBackgroundColor(color);

        int size = meterDots[meter].length;
        buildTagDots(meter);
        for (idx = 0; idx < size; idx++) {
                holder.ivDots[idx].setImageResource(tagDots[idx]);
        }
        while (idx < dotMax) {
            holder.ivDots[idx].setVisibility(View.GONE);
            idx++;
        }
        switch (soundType) {
            case 2:
                holder.ivSoundType.setImageResource(R.mipmap.say_tooktook);
                break;
            case 1:
                holder.ivSoundType.setImageResource(R.mipmap.say_ticktick);
                break;
            case 0:
                holder.ivSoundType.setImageResource(R.mipmap.say_hana);
                break;
        }
        holder.tvMeter.setText(meterTexts[meter]);
        holder.tvTempo.setText(String.valueOf(tempo));
    }

    private void buildTagDots(int meter) {
        int size = meterDots[meter].length;
        for (int idx = 0; idx < size; idx++) {
            int dot = meterDots[meter][idx];
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
        return (null != metroInfos ? metroInfos.size() : 0);
    }

}
