package com.example.zach.digitalfencingref;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;


import java.util.concurrent.TimeUnit;

public  class HomeScreen extends AppCompatActivity {

    private TextView time;
    private Button stop;
    private Button start;
    private String currentTime;
    private CountDownTimer countDownTimer;
    private boolean isCountingDown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        isCountingDown = false;
        ImageButton redPlus = (ImageButton) findViewById(R.id.redPlus);
        ImageButton redMinus = (ImageButton) findViewById(R.id.redMinus);
        ImageButton greenPlus = (ImageButton) findViewById(R.id.greenPlus);
        ImageButton greenMinus = (ImageButton) findViewById(R.id.greenMinus);
        final Button start = (Button) findViewById(R.id.start);
        final Button stop = (Button) findViewById(R.id.stop);
        Button reset = (Button) findViewById(R.id.reset);
        Button addMinute = (Button) findViewById(R.id.addMinute);
        Button removeMinute = (Button) findViewById(R.id.removeMinute);

        final TextView redScore = (TextView) findViewById(R.id.redScore);
        final TextView greenScore = (TextView) findViewById(R.id.greenScore);
         time = (TextView) findViewById(R.id.time);
        currentTime = time.getText().toString();



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_score:
                        //mTextMessage.setText(R.string.title_score);
                        return true;
                    case R.id.action_video:
                      //  mTextMessage.setText(R.string.title_camera);
                        Intent intent = new Intent(HomeScreen.this, video.class);
                        intent.putExtra("currentTime",currentTime);
                        intent.putExtra("isCountingDown",isCountingDown);
                        startActivityForResult(intent,0);

                        return true;
                    case R.id.action_stats:
                       // mTextMessage.setText(R.string.title_stats);
                        Intent glossaryIntent = new Intent(HomeScreen.this,glossary.class);
                        glossaryIntent.putExtra("currentTime",currentTime);
                        glossaryIntent.putExtra("isCountingDown",isCountingDown);
                        startActivityForResult(glossaryIntent,0);
                        return true;
                }
                return false;
            }
        });

        redPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldValue = Integer.parseInt(redScore.getText().toString());
                redScore.setText(String.valueOf(oldValue+1));
            }
        });
        redMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldValue = Integer.parseInt(redScore.getText().toString());
                if(oldValue > 0) {
                    redScore.setText(String.valueOf(oldValue - 1));
                }
            }
        });
        greenPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldValue = Integer.parseInt(greenScore.getText().toString());
                greenScore.setText(String.valueOf(oldValue+1));
            }
        });
        greenMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldValue = Integer.parseInt(greenScore.getText().toString());
                if(oldValue > 0) {
                    greenScore.setText(String.valueOf(oldValue - 1));
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setClickable(false);
                stop.setClickable(true);
                currentTime = time.getText().toString();
                isCountingDown = true;
                createCountDownTimer();

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setClickable(true);
                stop.setClickable(false);
                countDownTimer.cancel();
                isCountingDown = false;
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                start.setClickable(true);
                stop.setClickable(false);
                time.setText("03:00");
                currentTime = "03:00";
                isCountingDown = false;


            }
        });

        addMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldTime = time.getText().toString();
                int newMinutes = Integer.parseInt(oldTime.substring(1,oldTime.indexOf(':')))+1;
                String newTime = "";
                if(oldTime.charAt(0) == '0') {
                    newTime = "0" + String.valueOf(newMinutes) + oldTime.substring(oldTime.indexOf(':'));
                }
                else{
                    newTime = String.valueOf(newMinutes) + oldTime.substring(oldTime.indexOf(':'));
                }
                if(!isCountingDown) {
                    time.setText(newTime);
                }
                else {
                    countDownTimer.cancel();
                    time.setText(newTime);
                    createCountDownTimer();
                }
            }
        });
        removeMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldTime = time.getText().toString();
                int newMinutes = Integer.parseInt(oldTime.substring(1, oldTime.indexOf(':')));
                if (newMinutes > 0) {
                    newMinutes--;
                    String newTime = "";
                    if (oldTime.charAt(0) == '0') {
                        newTime = "0" + String.valueOf(newMinutes) + oldTime.substring(oldTime.indexOf(':'));
                    } else {
                        newTime =  String.valueOf(newMinutes) + oldTime.substring(oldTime.indexOf(':'));
                    }
                    if(!isCountingDown) {
                        time.setText(newTime);
                    }
                    else {
                        countDownTimer.cancel();
                        time.setText(newTime);
                        createCountDownTimer();
                    }

                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.app_bar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== R.id.action_about){
            startActivity(new Intent(HomeScreen.this,about.class));
        }
        else if(item.getItemId() == R.id.action_contact){
            startActivity(new Intent(HomeScreen.this,contact.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void createCountDownTimer(){
        currentTime = time.getText().toString();
        int minutes = Integer.parseInt(currentTime.substring(0,currentTime.indexOf(":")));
        int seconds = Integer.parseInt(currentTime.substring(currentTime.indexOf(":") + 1));
        long minutesToMilli = TimeUnit.MINUTES.toMillis(minutes);
        long secondsToMilli = TimeUnit.SECONDS.toMillis(seconds);
        long total = minutesToMilli + secondsToMilli;
        final SharedPreferences sharedPreferences = getSharedPreferences("count", 0);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putBoolean("stop", false);
        edit.apply();


        countDownTimer = new CountDownTimer(total,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTime = time.getText().toString();

                if (sharedPreferences.getBoolean("stop", true)) {
                    countDownTimer.cancel();
                    start.setClickable(true);
                    stop.setClickable(false);
                }
                else {
                    String currentTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    time.setText(currentTime);
                }

            }

            @Override
            public void onFinish() {
                time.setText("0:00");
            }
        }.start();

    }


}
