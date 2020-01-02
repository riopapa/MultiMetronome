package com.urrecliner.andriod.multimetronome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;

import static com.urrecliner.andriod.multimetronome.Vars.mActivity;
import static com.urrecliner.andriod.multimetronome.Vars.mContext;
import static com.urrecliner.andriod.multimetronome.Vars.meterLists;
import static com.urrecliner.andriod.multimetronome.Vars.meterTexts;
import static com.urrecliner.andriod.multimetronome.Vars.metroAdapter;
import static com.urrecliner.andriod.multimetronome.Vars.metroInfos;
import static com.urrecliner.andriod.multimetronome.Vars.tempoLists;
import static com.urrecliner.andriod.multimetronome.Vars.tempos;
import static com.urrecliner.andriod.multimetronome.Vars.utils;

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
}
