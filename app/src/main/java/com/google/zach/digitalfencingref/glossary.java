package com.google.zach.digitalfencingref;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class glossary extends AppCompatActivity {
    String[] groupList;
    String[] childList;
    Map<String, List<String>> collection;
    ExpandableListView expListView;
    private String currentTime;
    private boolean isCountingDown;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_score:
//                    mTextMessage.setText(R.string.title_home);
                    Intent homeIntent = new Intent(glossary.this,HomeScreen.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityIfNeeded(homeIntent, 0);

                    return true;
                case R.id.action_video:
                   ;
//                    mTextMessage.setText(R.string.title_dashboard);
                    Intent videoIntent = new Intent(glossary.this, video.class);
                    String cur = currentTime;
                    boolean con = isCountingDown;
                    videoIntent.putExtra("currentTime",currentTime);
                    videoIntent.putExtra("isCountingDown",isCountingDown);
                    startActivityForResult(videoIntent,0);

                    finish();
                    return true;
                case R.id.action_stats:
//                    mTextMessage.setText(R.string.title_notifications);
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

        String timeFromEarlierActivity = getIntent().getStringExtra("currentTime");
        boolean wasCountingDown = getIntent().getBooleanExtra("isCountingDown",false);
        isCountingDown = wasCountingDown;
        currentTime = timeFromEarlierActivity;
        if(wasCountingDown) {
            int minutes = Integer.parseInt(timeFromEarlierActivity.substring(0, timeFromEarlierActivity.indexOf(":")));
            int seconds = Integer.parseInt(timeFromEarlierActivity.substring(timeFromEarlierActivity.indexOf(":") + 1));
            long minutesToMilli = TimeUnit.MINUTES.toMillis(minutes);
            long secondsToMilli = TimeUnit.SECONDS.toMillis(seconds);
            long total = minutesToMilli + secondsToMilli;
            CountDownTimer countDownTimer  = new CountDownTimer(total, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    SharedPreferences sharedPreferences = getSharedPreferences("count", 0);
                    currentTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                }

                @Override
                public void onFinish() {
                    Intent homeIntent = new Intent(glossary.this,HomeScreen.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityIfNeeded(homeIntent, 0);
                }
            }.start();

        }

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.app_bar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== R.id.action_about){
            startActivity(new Intent(glossary.this,about.class));
        }
        else if(item.getItemId() == R.id.action_contact){
            startActivity(new Intent(glossary.this,contact.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
