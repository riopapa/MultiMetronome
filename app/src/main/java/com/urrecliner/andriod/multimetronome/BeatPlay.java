package com.urrecliner.andriod.multimetronome;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import static com.urrecliner.andriod.multimetronome.Vars.meterBeats;

class BeatPlay extends AsyncTask<Void, Void, String> {

    private MetroInfo metroInfo;
    private double bpm;
    private int silence;
    private int [] beats;
    private final int tick = 1000; // samples of tick
    private final int SAMPLE_RATE = 8000;

    private AudioGenerator audioGenerator = new AudioGenerator(SAMPLE_RATE);
    private Handler mHandler;
    private double[] highSounds, midSounds, lowSounds, silenceSoundArray;
    private boolean isRunning;

    BeatPlay(MetroInfo metroInfo) {
        this.metroInfo = metroInfo;
        audioGenerator.createPlayer();
        mHandler = MetroAdapter.getHandler();
        isRunning = true;
    }

    protected String doInBackground(Void... params) {
        beats = meterBeats[metroInfo.getBeatIndex()];
        bpm = metroInfo.getTempo();
        calcSilence();
        int colIndex = 0;
        do {
            int beatType = beats[colIndex];
            switch (beatType) {
                case 11:
                    audioGenerator.writeSound(highSounds);
                    break;
                case 12:
                case 13:
                case 14:
                    audioGenerator.writeSound(midSounds);
                    break;
                default:
                    audioGenerator.writeSound(lowSounds);
            }
            Message msg = new Message();
            msg.obj = "s"+colIndex;
            mHandler.sendMessage(msg);
            audioGenerator.writeSound(silenceSoundArray);
            colIndex++;
            if(colIndex >= beats.length)
                colIndex = 0;
        } while(isRunning);
        return null;
    }

    void stop() {
        isRunning = false;
        audioGenerator.destroyAudioTrack();
        Message msg = new Message();
        msg.obj = "x0";
        mHandler.sendMessage(msg);
    }

    private void calcSilence() {
        double highFreq = 4440;
        double midFreq = 2440;
        double lowFreq = 8440;

        silence = (int) ((SAMPLE_RATE*60/bpm)-tick);
        highSounds = new double[this.tick];
        midSounds = new double[this.tick];
        lowSounds = new double[this.tick];
        silenceSoundArray = new double[this.silence];
        double[] highs = audioGenerator.getSineWave(this.tick, SAMPLE_RATE, highFreq);
        double[] mids = audioGenerator.getSineWave(this.tick, SAMPLE_RATE, midFreq);
        double[] lows = audioGenerator.getSineWave(this.tick, SAMPLE_RATE, lowFreq);
        for(int i=0;i<this.tick;i++) {
            highSounds[i] = highs[i];
            midSounds[i] = mids[i];
            lowSounds[i] = lows[i];
        }
        for(int i=0;i<silence;i++)
            silenceSoundArray[i] = 0;
    }
}