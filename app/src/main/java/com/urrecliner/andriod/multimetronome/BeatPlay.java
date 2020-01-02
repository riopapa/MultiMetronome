package com.urrecliner.andriod.multimetronome;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import static com.urrecliner.andriod.multimetronome.Vars.dotRids;

class BeatPlay extends AsyncTask<Void, Void, String> {
    private int interval;
    ImageView onViewDot, offViewDot;
    private static int tagDot, oldDot;
    private int colIndex;
    private int [] tagDots = new int[dotRids.length];
    private ImageView [] mivDots = new ImageView[dotRids.length];
    MetroInfo metroInfo;
    private Handler mHandler;
    Metro_nome metro_nnome;
    private short bpm = 100;
    private short note_VValue = 4;
    private short beats = 4;


    public BeatPlay(MetroInfo metroInfo, int [] tagDots, ImageView [] mivDots) {
        this.metroInfo = metroInfo;
        System.arraycopy(tagDots, 0, this.tagDots, 0, tagDots.length );
        System.arraycopy(mivDots, 0, this.mivDots, 0, mivDots.length );

        offViewDot = null;
        colIndex = 0;
        mHandler = MetroAdapter.getHandler();
        metro_nnome = new Metro_nome(mHandler);
    }

    protected String doInBackground(Void... params) {
        metro_nnome.play(metroInfo);
        return null;
    }

    public void stop() {
        if (metro_nnome != null) {
            metro_nnome.stop();
            metro_nnome = null;
            Message msg = new Message();
          msg.obj = "x";
            mHandler.sendMessage(msg);
        }
    }

    int prevPos = -1;

}