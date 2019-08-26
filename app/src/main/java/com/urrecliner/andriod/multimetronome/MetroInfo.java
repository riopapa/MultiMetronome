package com.urrecliner.andriod.multimetronome;

class MetroInfo {

    private int soundType;
    private int meter;
    private int tempo;

    MetroInfo(int meter, int tempo, int soundType) {
        this.meter = meter;
        this.tempo = tempo;
        this.soundType = soundType;
    }

    int getSoundType() {
        return soundType;
    }

    void setSoundType(int soundType) {
        this.soundType = soundType;
    }

    int getMeter() {
        return meter;
    }

    void setMeter(int meter) {
        this.meter = meter;
    }

    int getTempo() {
        return tempo;
    }

    void setTempo(int tempo) {
        this.tempo = tempo;
    }
}
