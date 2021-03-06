package com.urrecliner.multimetronome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

class Vars {
    static SharedPreferences sharedPreferences;

//    @SuppressLint("StaticFieldLeak")
    static Context mContext;
//    @SuppressLint("StaticFieldLeak")
    static Activity mActivity;
    @SuppressLint("StaticFieldLeak")
    static MetroAdapter metroAdapter;
    static Utils utils = new Utils();
    static int mPos;
    static boolean isRunning;
    static ArrayList<MetroInfo> metroInfos;

    static int[] dotRids = {R.id.dot00, R.id.dot01, R.id.dot02, R.id.dot03, R.id.dot04, R.id.dot05, R.id.dot06, R.id.dot07,
            R.id.dot08, R.id.dot09, R.id.dot10, R.id.dot11, R.id.dot12, R.id.dot13, R.id.dot14, R.id.dot15 };

    static String[] meterTexts = { "2/2", "2/4", "3/4", "4/4", "4/4+", "4/8", "6/8", "6/8+", "9/8", "12/8", "12/8+" }; // ♫

    static int[][] meterBeats = {
            { 11, 2, 11, 2} ,                                           // 0  2/2
            { 11, 2, 11, 2} ,                                           // 1  2/4
            { 11, 2, 3, 12,  2, 3} ,                                    // 2  3/4
            { 11, 2, 3,  4, 12, 2,  3, 4},                              // 3  4/4
            { 11, 7, 12, 7, 13, 7, 14, 7, 11, 7, 12, 7, 13, 7, 14, 7},  // 4  4/4 ♫
            { 11, 2, 3,  4, 12, 2,  3, 4},                              // 5  4/8
            { 11, 2, 3,  4,  5, 6, 11, 2, 3,  4,  5, 6} ,               // 6  6/8
            { 11, 2, 3,  12, 2, 3, 11, 2, 3, 12, 2, 3} ,                // 7  6/8 ♫
            { 11, 2, 3, 11,  2, 3, 11, 2, 3} ,                          // 8  9/8
            { 11, 2, 3,  4, 12, 2,  3, 4, 13, 2, 3, 4} ,                // 9  12/8
            { 11, 2, 3,  12, 2, 3,  13, 2, 3, 14, 2, 3}                 // 0  12/8 ♫
    };
    static int [] tempos = {52, 56, 60, 66, 70, 72, 76, 80, 82, 84, 88, 90, 92, 96, 100, 102, 104, 108, 110, 112, 120, 128, 130, 132, 140};

    static List<String> meterLists, tempoLists;

}
