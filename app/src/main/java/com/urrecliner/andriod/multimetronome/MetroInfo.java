package com.urrecliner.andriod.multimetronome;

class MetroInfo {

    private int beatIndex;
    private int tempo;

    MetroInfo(int beatIndex, int tempo) {
        this.beatIndex = beatIndex;
        this.tempo = tempo;
    }

    int getBeatIndex() {
        return beatIndex;
    }

    void setBeatIndex(int beatIndex) {
        this.beatIndex = beatIndex;
    }

    int getTempo() {
        return tempo;
    }

    void setTempo(int tempo) {
        this.tempo = tempo;
    }
}
