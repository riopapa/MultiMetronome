package com.urrecliner.andriod.multimetronome;

public class MetroInfo {

    private int soundType;
    private int meter;
    private int speed;

    public MetroInfo(int meter, int speed, int soundType) {
        this.meter = meter;
        this.speed = speed;
        this.soundType = soundType;
    }

    public int getSoundType() {
        return soundType;
    }

    public void setSoundType(int soundType) {
        this.soundType = soundType;
    }

    public int getMeter() {
        return meter;
    }

    public void setMeter(int meter) {
        this.meter = meter;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
