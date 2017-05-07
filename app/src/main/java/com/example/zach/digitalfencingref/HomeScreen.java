package com.example.zach.digitalfencingref;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public  class HomeScreen extends AppCompatActivity {

    private TextView mTextMessage;

    private CountDownTimer countDownTimer;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_score:
                    mTextMessage.setText(R.string.title_score);
                    return true;
                case R.id.action_video:
                    mTextMessage.setText(R.string.title_camera);
                    return true;
                case R.id.action_stats:
                    mTextMessage.setText(R.string.title_stats);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);



        ImageButton redPlus = (ImageButton) findViewById(R.id.redPlus);
        ImageButton redMinus = (ImageButton) findViewById(R.id.redMinus);
        ImageButton greenPlus = (ImageButton) findViewById(R.id.greenPlus);
        ImageButton greenMinus = (ImageButton) findViewById(R.id.greenMinus);
        Button start = (Button) findViewById(R.id.start);
        Button stop = (Button) findViewById(R.id.stop);

        final TextView redScore = (TextView) findViewById(R.id.redScore);
        final TextView greenScore = (TextView) findViewById(R.id.greenScore);
        final TextView time = (TextView) findViewById(R.id.time);


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
                String currentTime = time.getText().toString();
                int minutes = Integer.parseInt(currentTime.substring(0,currentTime.indexOf(":")));
                int seconds = Integer.parseInt(currentTime.substring(currentTime.indexOf(":") + 1));
                long minutesToMilli = TimeUnit.MINUTES.toMillis(minutes);
                long secondsToMilli = TimeUnit.SECONDS.toMillis(seconds);
                long total = minutesToMilli + secondsToMilli;
                 countDownTimer = new CountDownTimer(total,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        String currentTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                        time.setText(currentTime);
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
            }
        });





    }


}
