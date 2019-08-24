package com.urrecliner.andriod.multimetronome;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

public class Vars {
    static SharedPreferences sharedPreferences;

    static Context mContext;
    static Activity mActivity;
    static Utils utils;
    static int gxIdx;
    static CountDownTimer gxCDT;
    static boolean cdtRunning;

    static int[] dotRids = {R.id.dot00, R.id.dot01, R.id.dot02, R.id.dot03, R.id.dot04, R.id.dot05,
            R.id.dot06, R.id.dot07, R.id.dot08, R.id.dot09, R.id.dot10, R.id.dot11, R.id.dot12,
            R.id.dot13, R.id.dot14, R.id.dot15 };

    static String [] meterTexts = { "2/2", "3/4", "4/4", "6/8", "9/8", "12/8", "2/4" };

}
