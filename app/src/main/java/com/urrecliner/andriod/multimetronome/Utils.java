package com.urrecliner.andriod.multimetronome;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.urrecliner.andriod.multimetronome.Vars.beepMedias;
import static com.urrecliner.andriod.multimetronome.Vars.beepSource;
import static com.urrecliner.andriod.multimetronome.Vars.hanaMedias;
import static com.urrecliner.andriod.multimetronome.Vars.hanaSource;
import static com.urrecliner.andriod.multimetronome.Vars.mContext;
import static com.urrecliner.andriod.multimetronome.Vars.metros;
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
        String json = gson.toJson(metros);
        prefsEditor.putString("metroInfo", json);
        prefsEditor.apply();
    }

    ArrayList<Metro> readTables() {

        ArrayList<Metro> list;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("metroInfo", "");
        if (json.isEmpty()) {
            list = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<Metro>>() {
            }.getType();
            list = gson.fromJson(json, type);
        }
        return list;
    }

    void soundInitiate() {

        hanaMedias = new MediaPlayer[hanaSource.length];
        beepMedias = new MediaPlayer[beepSource.length];
        for (int i = 1; i < hanaSource.length; i++) {
            hanaMedias[i] = readyMedia(hanaSource[i]);
        }
        for (int i = 1; i < beepSource.length; i++) {
            beepMedias[i] = readyMedia(beepSource[i]);
        }

    }
    private MediaPlayer readyMedia(int rawId){
        return MediaPlayer.create(mContext, rawId);
    }
    void beepSound(MediaPlayer id) { id.start();}

}
