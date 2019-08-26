package com.urrecliner.andriod.multimetronome;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.urrecliner.andriod.multimetronome.Vars.beepLoads;
import static com.urrecliner.andriod.multimetronome.Vars.beepSource;
import static com.urrecliner.andriod.multimetronome.Vars.hanaLoads;
import static com.urrecliner.andriod.multimetronome.Vars.hanaSource;
import static com.urrecliner.andriod.multimetronome.Vars.mContext;
import static com.urrecliner.andriod.multimetronome.Vars.metroInfos;
import static com.urrecliner.andriod.multimetronome.Vars.sharedPreferences;


class Utils {

    void log(String tag, String text) {
        StackTraceElement[] traces;
        traces = Thread.currentThread().getStackTrace();
        String where = " " + traces[5].getMethodName() + " > " + traces[4].getMethodName() + " > " + traces[3].getMethodName() + " #" + traces[3].getLineNumber();
        Log.w(tag, where + " " + text);
    }

    void saveTables() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(metroInfos);
        prefsEditor.putString("metroInfo", json);
        prefsEditor.apply();
    }

    ArrayList<MetroInfo> readTables() {

        ArrayList<MetroInfo> list;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("metroInfo", "");
        if (json.isEmpty()) {
            list = new ArrayList<MetroInfo>();
        } else {
            Type type = new TypeToken<List<MetroInfo>>() {
            }.getType();
            list = gson.fromJson(json, type);
        }
        return list;
    }

    private SoundPool soundPool = null;

    void soundInitiate() {

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(5).build();

        hanaLoads = new int[hanaSource.length];
        beepLoads = new int[beepSource.length];
        for (int i = 1; i < hanaSource.length; i++)
            hanaLoads[i] = soundPool.load(mContext, hanaSource[i], 1);
        for (int i = 1; i < beepSource.length; i++)
            beepLoads[i] = soundPool.load(mContext, beepSource[i], 1);
    }

    void beepSound(int soundId, float volume) {
        soundPool.play(soundId, volume, volume, 1, 0, 1);
    }

}
