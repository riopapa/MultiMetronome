package com.urrecliner.andriod.multimetronome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import static com.urrecliner.andriod.multimetronome.Vars.utils;

public class MainActivity extends AppCompatActivity {

    ArrayList<MetroInfo> metroInfos;
    MetroAdapter metroAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        utils = new Utils();

        metroInfos = new ArrayList<>();

        metroAdapter = new MetroAdapter(metroInfos);
        mRecyclerView.setAdapter(metroAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        MetroInfo metroInfo = new MetroInfo(1,120, 0);
        metroInfos.add(metroInfo);
        metroInfo = new MetroInfo(2,22, 1);
        metroInfos.add(metroInfo);
        metroInfo = new MetroInfo(3,333, 0);
        metroInfos.add(metroInfo);
        metroInfo = new MetroInfo(4,160, 1);
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
