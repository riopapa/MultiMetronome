package com.urrecliner.andriod.multimetronome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import static com.urrecliner.andriod.multimetronome.Vars.mActivity;
import static com.urrecliner.andriod.multimetronome.Vars.mContext;
import static com.urrecliner.andriod.multimetronome.Vars.metroAdapter;
import static com.urrecliner.andriod.multimetronome.Vars.utils;

public class MainActivity extends AppCompatActivity {

    ArrayList<MetroInfo> metroInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        mContext = this;
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        utils = new Utils();

        metroInfos = new ArrayList<>();
        utils.soundInitiate();

        metroAdapter = new MetroAdapter(metroInfos);
        recyclerView.setAdapter(metroAdapter);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                mLinearLayoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);

        MetroInfo metroInfo = new MetroInfo(1,120, 0);
        metroInfos.add(metroInfo);
        metroInfo = new MetroInfo(2,96, 1);
        metroInfos.add(metroInfo);
        metroInfo = new MetroInfo(3,90, 0);
        metroInfos.add(metroInfo);
        metroInfo = new MetroInfo(4,120, 1);
        metroInfos.add(metroInfo);
        metroInfo = new MetroInfo(4,128, 0);
        metroInfos.add(metroInfo);
        metroInfo = new MetroInfo(5,120, 0);
        metroInfos.add(metroInfo);
        metroInfo = new MetroInfo(6,90, 1);
        metroInfos.add(metroInfo);
        metroInfo = new MetroInfo(0,120, 0);
        metroInfos.add(metroInfo);

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
                MetroInfo metroInfo = new MetroInfo(1,120, 1);
                metroInfos.add(metroInfo);
                metroAdapter.notifyItemInserted(metroInfos.size());
                metroAdapter.notifyItemRangeChanged(metroInfos.size(), metroInfos.size());
                Log.w("add","returned");
                return true;
            case R.id.action_stop:
                    metroAdapter.stopHandler();
                    return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
