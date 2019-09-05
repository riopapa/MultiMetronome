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

import static com.urrecliner.andriod.multimetronome.Vars.hanaMedias;
import static com.urrecliner.andriod.multimetronome.Vars.rawHana;
import static com.urrecliner.andriod.multimetronome.Vars.mContext;
import static com.urrecliner.andriod.multimetronome.Vars.metros;
import static com.urrecliner.andriod.multimetronome.Vars.oneMedias;
import static com.urrecliner.andriod.multimetronome.Vars.rawOne;
import static com.urrecliner.andriod.multimetronome.Vars.sharedPreferences;
import static com.urrecliner.andriod.multimetronome.Vars.beep1Medias;
import static com.urrecliner.andriod.multimetronome.Vars.rawBeep1;
import static com.urrecliner.andriod.multimetronome.Vars.beep2Medias;
import static com.urrecliner.andriod.multimetronome.Vars.rawBeep2;


class Utils {

    void log(String tag, String text) {
        String where = " ";
        StackTraceElement[] traces;
        traces = Thread.currentThread().getStackTrace();
        if (traces.length > 5) {
            where += traces[5].getMethodName() + " > " + traces[4].getMethodName() + " > " + traces[3].getMethodName() + " #" + traces[3].getLineNumber();
        }
        else if (traces.length > 4){
            where += traces[4].getMethodName() + " > " + traces[3].getMethodName() + " #" + traces[3].getLineNumber();
        }
        else {
            where += traces[3].getMethodName() + " #" + traces[2].getLineNumber();
        }
        Log.w(tag, where + " " + text);
    }

    void saveSharedPrefTables() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(metros);
        prefsEditor.putString("metroInfo", json);
        prefsEditor.apply();
    }

    ArrayList<Metro> readSharedPrefTables() {

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

        hanaMedias = new MediaPlayer[rawHana.length];
        for (int i = 1; i < rawHana.length; i++) {
            hanaMedias[i] = readyMedia(rawHana[i]);
        }
        oneMedias = new MediaPlayer[rawOne.length];
        for (int i = 1; i < rawOne.length; i++) {
            oneMedias[i] = readyMedia(rawOne[i]);
        }
        beep1Medias = new MediaPlayer[rawBeep1.length];
        for (int i = 1; i < rawBeep1.length; i++) {
            beep1Medias[i] = readyMedia(rawBeep1[i]);
        }
        beep2Medias = new MediaPlayer[rawBeep2.length];
        for (int i = 1; i < rawBeep2.length; i++) {
            beep2Medias[i] = readyMedia(rawBeep2[i]);
        }
    }

    private MediaPlayer readyMedia(int rawId){
        return MediaPlayer.create(mContext, rawId);
    }

    void beepSound(MediaPlayer id, float volume) {
        id.setVolume(volume, volume);
        id.start();
    }

}