package com.urrecliner.multimetronome;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.urrecliner.multimetronome.Vars.mContext;
import static com.urrecliner.multimetronome.Vars.metroInfos;
import static com.urrecliner.multimetronome.Vars.sharedPreferences;


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
        String json = gson.toJson(metroInfos);
        prefsEditor.putString("metroInfo", json);
        prefsEditor.apply();
    }

    ArrayList<MetroInfo> readSharedPrefTables() {

        ArrayList<MetroInfo> list;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("metroInfo", "");
        if (json.isEmpty()) {
            list = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<MetroInfo>>() {
            }.getType();
            list = gson.fromJson(json, type);
        }
        return list;
    }
}