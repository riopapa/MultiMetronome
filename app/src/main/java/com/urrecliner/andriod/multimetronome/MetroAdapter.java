package com.urrecliner.andriod.multimetronome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.urrecliner.andriod.multimetronome.Vars.dotRids;
import static com.urrecliner.andriod.multimetronome.Vars.isRunning;
import static com.urrecliner.andriod.multimetronome.Vars.mActivity;
import static com.urrecliner.andriod.multimetronome.Vars.mContext;
import static com.urrecliner.andriod.multimetronome.Vars.mPos;
import static com.urrecliner.andriod.multimetronome.Vars.meterBeats;
import static com.urrecliner.andriod.multimetronome.Vars.meterLists;
import static com.urrecliner.andriod.multimetronome.Vars.meterTexts;
import static com.urrecliner.andriod.multimetronome.Vars.metroAdapter;
import static com.urrecliner.andriod.multimetronome.Vars.metroInfos;
import static com.urrecliner.andriod.multimetronome.Vars.tempoLists;
import static com.urrecliner.andriod.multimetronome.Vars.tempos;
import static com.urrecliner.andriod.multimetronome.Vars.utils;


public class MetroAdapter extends RecyclerView.Adapter<MetroAdapter.CustomViewHolder> {

//    final static String logId = "adapter";
    static ImageView [] mImageView;
    static int [] tagIcons;
    private ImageView nowGo, nowStop;

    MetroAdapter() {
        mImageView = new ImageView[dotRids.length];
        tagIcons = new int[dotRids.length];
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tvBeat;
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
            tvBeat = view.findViewById(R.id.beatIndex);
            tvBeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopBeatPlay();
                    mPos = getAdapterPosition();
                    final MetroInfo metroInfo = metroInfos.get(mPos);
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
                    View theView = View.inflate(mContext, R.layout.wheel_view, null);
                    WheelView wV = theView.findViewById(R.id.wheel);
                    wV.setItems(meterLists);
                    wV.selectIndex(metroInfo.getBeatIndex());
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
                                    metroInfos.get(mPos).setBeatIndex(wheelIdx);
                                    utils.saveSharedPrefTables();
                                    tvBeat.setText(meterTexts[wheelIdx]);
                                    tvBeat.invalidate();
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
                    final MetroInfo metroInfo = metroInfos.get(mPos);
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
                    View theView = View.inflate(mContext, R.layout.wheel_view, null);
                    WheelView wV = theView.findViewById(R.id.wheel);
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
                            .setPositiveButton("이 빠르기로 하지요",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String s;
                                    int val = tempos[wheelIdx];
                                    metroInfos.get(mPos).setTempo(val);
                                    utils.saveSharedPrefTables();
                                    s = ""+val;
                                    tvTempo.setText(s);
                                    tvTempo.invalidate();
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

            ivGo = itemView.findViewById(R.id.go);
            ivGo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopBeatPlay();
                    mPos = getAdapterPosition();
                    for (int idx = 0; idx < dotRids.length; idx++) {
                        ivDots[idx] = itemView.findViewById(dotRids[idx]);
                        mImageView[idx] = itemView.findViewById(dotRids[idx]);
                    }
                    isRunning = true;
                    ivGo.setVisibility(View.GONE);
                    ivStop.setVisibility(View.VISIBLE);
                    nowGo = ivGo;
                    nowStop = ivStop;
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


    static Handler getHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                blinkDots(msg.obj.toString());
            }
        };
    }

    private static ImageView iV = null;
    private static int iCon = 0;
    private static void blinkDots (String msgText) {
        String msgHead = msgText.substring(0, 1);
        switch (msgHead) {
            case "s":
                if (iV != null) {
                    iV.setImageResource(iCon);
                    iV.invalidate();
                }
                int pos = Integer.parseInt(msgText.substring(1));
                iV = mImageView[pos];
                iV.setImageResource(R.drawable.dot_now);
                iCon = tagIcons[pos];
                iV.invalidate();
                break;
            case "x":
                if (iV != null) {
                    iV.setImageResource(iCon);
                    iV.invalidate();
                }
                iV = null;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (null != metroInfos ? metroInfos.size() : 0);
    }


    private BeatPlay beatPlay = null;
    private synchronized void startBeatPlay() {

        MetroInfo metroInfo = metroInfos.get(mPos);
        buildTagDots(metroInfo.getBeatIndex());
        nowGo.setEnabled(false);
        beatPlay = new BeatPlay(metroInfo);
        beatPlay.execute();
        nowGo.setEnabled(true);
    }

    void stopBeatPlay() {
        if (beatPlay != null)
            beatPlay.stop();
        if (isRunning) {
            nowGo.setImageResource(R.mipmap.go_green);
            metroAdapter.notifyItemChanged(mPos);
//            utils.log("mPos "+mPos,">> "+metroInfos.get(mPos).getBeatIndex());
            isRunning = false;
            nowStop.setVisibility(View.GONE);
            nowGo.setVisibility(View.VISIBLE);
        }
    }

    private void confirmDelete(int position) {
        final int pos = position;
        MetroInfo metroInfo = metroInfos.get(position);
        String s = "삭제하려면 [그래요] 를 누르세요 \n"+meterTexts[metroInfo.getBeatIndex()]+" tempo("+ metroInfo.getTempo()+")";
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("이거 날려요 ?");
        builder.setMessage(s);
        builder.setPositiveButton("그래요",
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
        int beatIndex = metroInfo.getBeatIndex();
        int tempo = metroInfo.getTempo();
        int idx;

        int color = (pos%2 == 0) ? Color.parseColor("#EFF0F1") : Color.parseColor("#eddada");
        holder.constraintLayout.setBackgroundColor(color);
        buildTagDots(beatIndex);
        int size = meterBeats[beatIndex].length;
        for (idx = 0; idx < size; idx++) {
            holder.ivDots[idx].setImageResource(tagIcons[idx]);
            holder.ivDots[idx].setVisibility(View.VISIBLE);
        }
        while (idx < dotMax) {
            holder.ivDots[idx].setVisibility(View.GONE);
            idx++;
        }
        holder.tvBeat.setText(meterTexts[beatIndex]);
        holder.tvTempo.setText(String.valueOf(tempo));
        holder.ivStop.setVisibility(View.GONE);
    }

    private void buildTagDots(int meter) {
        int [] beats = meterBeats[meter];
        int size = beats.length;
        for (int idx = 0; idx < size; idx++) {
            int dot = beats[idx];
            if (dot == 11) {
                tagIcons[idx] = R.drawable.dot_red_big;
            }
            else if (dot == 12 || dot == 13 || dot == 14) {
                tagIcons[idx] = R.drawable.dot_red_small;
            }
            else {
                tagIcons[idx] = R.drawable.dot_pink;
            }
        }
    }

}
