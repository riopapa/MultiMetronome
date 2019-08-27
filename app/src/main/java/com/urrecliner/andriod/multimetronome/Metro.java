package com.urrecliner.andriod.multimetronome;

class Metro {

    private int hanaBeep;
    private int meter;
    private int tempo;

    Metro(int meter, int tempo, int hanaBeep) {
        this.meter = meter;
        this.tempo = tempo;
        this.hanaBeep = hanaBeep;
    }

    int getHanaBeep() {
        return hanaBeep;
    }

    void setHanaBeep(int hanaBeep) {
        this.hanaBeep = hanaBeep;
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
