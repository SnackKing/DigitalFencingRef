package com.example.zach.digitalfencingref;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zach.digitalfencingref.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_glossary extends AppCompatActivity {
    String[] groupList;
    String[] childList;
    Map<String, List<String>> collection;
    ExpandableListView expListView;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_score:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.action_video:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.action_stats:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        groupList = getResources().getStringArray(R.array.terms);
        childList = getResources().getStringArray(R.array.definitions);

        List<String> groupArrayList = new ArrayList<>();
        for(int i = 0; i < groupList.length;i++){
            groupArrayList.add(groupList[i]);
        }

       collection = createCollection(groupList,childList);

        expListView = (ExpandableListView) findViewById(R.id.list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, groupArrayList,collection);
        expListView.setAdapter(expListAdapter);
        expListView.setOnChildClickListener(new OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                        .show();

                return true;
            }
        });



    }
    private static Map<String,List<String>> createCollection(String[] groupList, String childList[]){
        Map<String,List<String>> collection = new HashMap<>();
        for(int i = 0; i < groupList.length;i++){
            List<String> child = new ArrayList<>();
            child.add(childList[i]);
            collection.put(groupList[i],child);
        }
        return collection;
    }
    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

}
