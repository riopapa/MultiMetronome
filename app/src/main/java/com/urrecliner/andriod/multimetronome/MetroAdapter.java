package com.urrecliner.andriod.multimetronome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.urrecliner.andriod.multimetronome.Vars.beepLoads;
import static com.urrecliner.andriod.multimetronome.Vars.cdtRunning;
import static com.urrecliner.andriod.multimetronome.Vars.dotRids;
import static com.urrecliner.andriod.multimetronome.Vars.hanaLoads;
import static com.urrecliner.andriod.multimetronome.Vars.mActivity;
import static com.urrecliner.andriod.multimetronome.Vars.mPos;
import static com.urrecliner.andriod.multimetronome.Vars.meterDots;
import static com.urrecliner.andriod.multimetronome.Vars.meterTexts;
import static com.urrecliner.andriod.multimetronome.Vars.metroAdapter;
import static com.urrecliner.andriod.multimetronome.Vars.metroTimer;
import static com.urrecliner.andriod.multimetronome.Vars.utils;


public class MetroAdapter extends RecyclerView.Adapter<MetroAdapter.CustomViewHolder> {

    private ArrayList<MetroInfo> metroInfos;

    ImageView [] mivDots;
    int [] tagDots;
    int soundType, meter, interval;
    int [] soundLoads;
    float[] volumeLoads;
    ImageView nowSoundType;
    TextView nowMeter;
    TextView nowSpeed;
    ImageView nowGo;

    public MetroAdapter(ArrayList<MetroInfo> list) {
        this.metroInfos = list;
        mivDots = new ImageView[dotRids.length];
        tagDots = new int[dotRids.length];
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSoundType;
        TextView tvMeter;
        TextView tvSpeed;
        ImageView ivGo;
        ImageView [] ivDots = new ImageView[dotRids.length];
        ConstraintLayout constraintLayout;

        CustomViewHolder(View view) {
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
            constraintLayout = itemView.findViewById(R.id.oneMetro);

            ivGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPos = getAdapterPosition();
                    if (cdtRunning) {
                        finishHandler();
                    }
                    else {
                        nowSoundType = view.findViewById(R.id.soundType);
                        nowMeter =  view.findViewById(R.id.meter);
                        nowSpeed =  view.findViewById(R.id.speed);
                        nowGo = view.findViewById(R.id.go);
                        for (int idx = 0; idx < dotRids.length; idx++) {
                            ivDots[idx] = itemView.findViewById(dotRids[idx]);
                            mivDots[idx] = itemView.findViewById(dotRids[idx]);
                        }
                        int meter = metroInfos.get(mPos).getMeter();
                        buildTagDots(meter);
                        cdtRunning = true;
                        nowGo.setImageResource(R.mipmap.go_red);
                        runCountDownTimer(setupSoundTable());
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

        soundType = metroInfos.get(mPos).getSoundType();
        meter = metroInfos.get(mPos).getMeter();
        interval = 60000 / metroInfos.get(mPos).getSpeed();

        return (soundType == 0) ? setupHanaTable() : setupBeepTable();
    }

    private int setupHanaTable() {
        int meter = metroInfos.get(mPos).getMeter();
        int count = meterDots[meter].length;
        utils.log("x","meter "+meter+" "+meterTexts[meter]+" count "+count+" mpos "+mPos);
        soundLoads = new int[count];
        volumeLoads = new float[count];
        for (int i = 0; i < count; i++)
            utils.log("tbl "+mPos," "+meterDots[meter][i]);
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
                volumeLoads[i] = 0.5f;
            }
        }
        return count;
    }
    private int setupBeepTable() {
        int meter = metroInfos.get(mPos).getMeter();
        int count = meterDots[meter].length;
        utils.log("x","meter "+meter+meterTexts[meter]+" count "+count+" mpos "+mPos);
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
                soundLoads[i] = beepLoads[3];
                volumeLoads[i] = 0.5f;
            }
        }
        return count;
    }

    static ImageView ivNow;
    static int tagDot;
    private int nowPos;
    private void runCountDownTimer(int count) {
        final int loop = count;
        nowGo.setEnabled(false);
        nowPos = 0;
        utils.log("info ","count "+count+" interval "+interval);

        metroTimer = new Timer();
        metroTimer.scheduleAtFixedRate(new TimerTask() {
              @Override
              public void run() {
                  if (cdtRunning) {
                      ivNow = mivDots[nowPos];
                      tagDot = tagDots[nowPos];
                      Message msg1 = Message.obtain();
                      msg1.obj = "o";
                      blinkDot.sendMessage(msg1);
                      utils.beepSound(soundLoads[nowPos], volumeLoads[nowPos]);
                      SystemClock.sleep(interval-150);
                      Message msg2 = Message.obtain();
                      msg2.obj = "f";
                      blinkDot.sendMessage(msg2);
                      nowPos++;
                      if (nowPos >= loop)
                          nowPos = 0;
                  }
              }
          },
        0, interval);

        nowGo.setEnabled(true);
    }

    private static final Handler blinkDot = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.obj.toString()) {
                case "o":
                    ivNow.setImageResource(R.mipmap.circle_blue);
                    ivNow.invalidate();
                    break;
                case "f":
                    ivNow.setImageResource(tagDot);
                    ivNow.invalidate();
                    break;
            }
        }
    };

    private void finishHandler() {
        if (metroTimer != null) {
            metroTimer.cancel();
            metroTimer = null;
        }
        cdtRunning = false;
        nowGo.setImageResource(R.mipmap.go_green);
    }

    void stopHandler() {
        finishHandler();
    }


    void confirmDelete(int position) {
        final int pos = position;
        MetroInfo metroInfo = metroInfos.get(position);
        String s = "삭제하려면 [필요없슈] 를 누르세요 \n"+meterTexts[metroInfo.getMeter()]+" speed("+metroInfo.getSpeed()+")";
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
        int speed = metroInfo.getSpeed();
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
            case 1:
                holder.ivSoundType.setImageResource(R.mipmap.say_dingdong);
                break;
            case 0:
                holder.ivSoundType.setImageResource(R.mipmap.say_hana);
                break;
        }
        holder.tvMeter.setText(meterTexts[meter]);
        holder.tvSpeed.setText(""+speed);
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
