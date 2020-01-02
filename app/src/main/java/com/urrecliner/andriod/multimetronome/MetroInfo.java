package com.urrecliner.andriod.multimetronome;

class MetroInfo {

    private int hanaBeep;
    private int beatIndex;
    private int tempo;

    MetroInfo(int beatIndex, int tempo, int hanaBeep) {
        this.beatIndex = beatIndex;
        this.tempo = tempo;
        this.hanaBeep = hanaBeep;
    }

    int getHanaBeep() {
        return hanaBeep;
    }

    void setHanaBeep(int hanaBeep) {
        this.hanaBeep = hanaBeep;
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
