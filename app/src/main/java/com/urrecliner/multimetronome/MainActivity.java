package com.urrecliner.multimetronome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static com.urrecliner.multimetronome.Vars.mActivity;
import static com.urrecliner.multimetronome.Vars.mContext;
import static com.urrecliner.multimetronome.Vars.meterLists;
import static com.urrecliner.multimetronome.Vars.meterTexts;
import static com.urrecliner.multimetronome.Vars.metroAdapter;
import static com.urrecliner.multimetronome.Vars.metroInfos;
import static com.urrecliner.multimetronome.Vars.tempoLists;
import static com.urrecliner.multimetronome.Vars.tempos;
import static com.urrecliner.multimetronome.Vars.utils;

public class MainActivity extends AppCompatActivity {

    final static String logId = "MetroInfo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.multi_metronome);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_main);
        mActivity = this;
        mContext = this;
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        utils = new Utils();

        metroInfos = utils.readSharedPrefTables();

        meterLists = new ArrayList<>();
        meterLists = Arrays.asList(meterTexts);
        tempoLists = new ArrayList<>();
        for (int t:tempos) { tempoLists.add("" + t); }

        metroAdapter = new MetroAdapter();
        recyclerView.setAdapter(metroAdapter);
        utils.log(logId,"Ready");

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                mLinearLayoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                MetroInfo metroInfo = new MetroInfo(3,84);
                metroInfos.add(metroInfo);
                metroAdapter.notifyItemInserted(metroInfos.size());
                metroAdapter.notifyItemRangeChanged(metroInfos.size(), metroInfos.size());
                return true;
            case R.id.action_stop:
                    metroAdapter.stopBeatPlay();
                    return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    final long BACK_DELAY = 3000;
    long backKeyPressedTime;
    @Override
    public void onBackPressed() {

        if(System.currentTimeMillis()<backKeyPressedTime+BACK_DELAY){
            finish();
            new Timer().schedule(new TimerTask() {
                public void run() {
                    finishAffinity();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            }, 500);
        }
        Toast.makeText(this, "Press BackKey again to quit",Toast.LENGTH_SHORT).show();
        backKeyPressedTime = System.currentTimeMillis();
    }

}
