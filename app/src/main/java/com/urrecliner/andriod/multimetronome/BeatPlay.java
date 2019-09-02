package com.urrecliner.andriod.multimetronome;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import static com.urrecliner.andriod.multimetronome.Vars.dotRids;
import static com.urrecliner.andriod.multimetronome.Vars.isRunning;
import static com.urrecliner.andriod.multimetronome.Vars.soundMedias;
import static com.urrecliner.andriod.multimetronome.Vars.utils;

class BeatPlay extends Thread {
    private int interval;
    static ImageView onViewDot, offViewDot;
    private static int tagDot, oldDot;
    private int nowPos;
    private int [] tagDots = new int[dotRids.length];
    private int [] meterDots = new int[dotRids.length];
    private ImageView [] mivDots = new ImageView[dotRids.length];
    private int loopCount;

    public BeatPlay(int interval, int [] tagDots, ImageView [] mivDots, int [] meterDots) {
        this.interval = interval;
        loopCount = meterDots.length;
        System.arraycopy(tagDots, 0, this.tagDots, 0, tagDots.length );
        System.arraycopy(mivDots, 0, this.mivDots, 0, mivDots.length );
        System.arraycopy(meterDots, 0, this.meterDots, 0, meterDots.length );
        offViewDot = null;
        nowPos = 0;
    }

    @Override public void run() {
        long nextTime = System.currentTimeMillis();
        while (isRunning) {
//            Log.w("time", " nextTime "+ nextTime +" diff "+diff);
//            if (offViewDot != null) {
//            }
            onViewDot = mivDots[nowPos];
            Message msgOn = Message.obtain(); msgOn.obj = "o";
            blinkDot.sendMessage(msgOn);
            boolean dummy = utils.beepSound(soundMedias[nowPos], nextTime);
            offViewDot = onViewDot;
            oldDot = tagDots[nowPos];
            long diff = nextTime - System.currentTimeMillis();
            if (diff >  5) {
//                utils.log("diff",""+diff);
                try { Thread.sleep(diff); } catch (InterruptedException e) {}
            }
            Message msgOff = Message.obtain(); msgOff.obj = "f";
            blinkDot.sendMessage(msgOff);
            nowPos++;
            if (nowPos >= loopCount)
                nowPos = 0;
            nextTime += interval;
        }
    }

    static final Handler blinkDot = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.obj.toString()) {
                case "o":
                    onViewDot.setImageResource(R.mipmap.circle_blue);
                    onViewDot.invalidate();
                    break;
                case "f":
                    offViewDot.setImageResource(oldDot);
                    offViewDot.invalidate();
                    break;
            }
        }
    };

}
