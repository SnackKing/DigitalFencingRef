package com.google.zach.OnTargetFencing;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;


import java.util.concurrent.TimeUnit;

import static android.R.attr.data;

public  class HomeScreen extends AppCompatActivity {

    /*
      Contains the current time on the CountDownTimer
     */
    private TextView time;
    /*
        Cancels the current CountDownTimer
     */
     Button stop;
    /*
        Begins a new CountDownTimer starting at @String currentTIme
     */
     Button start;
    /*
        The current time displayed in @TextView time
     */
    private String currentTime;
    /*
        The current CountDownTimer in use
     */
    private CountDownTimer countDownTimer;
    /*
        Whether or not the last countDownTimer has been canceled or not.
        Used to pass to the other activities
     */
    private boolean isCountingDown;
    /*
        Used to stop the timer from a different activity.
     */
    SharedPreferences sharedPreferences;

    private boolean hasBooted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        AppRater.app_launched(this);

        isCountingDown = false;
        sharedPreferences = this.getSharedPreferences("count", 0);
        //Initialize all components
        ImageButton redPlus = (ImageButton) findViewById(R.id.redPlus);
        ImageButton redMinus = (ImageButton) findViewById(R.id.redMinus);
        ImageButton greenPlus = (ImageButton) findViewById(R.id.greenPlus);
        ImageButton greenMinus = (ImageButton) findViewById(R.id.greenMinus);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);

        Button reset = (Button) findViewById(R.id.reset);
        Button addMinute = (Button) findViewById(R.id.addMinute);
        Button removeMinute = (Button) findViewById(R.id.removeMinute);
        Button resetScore = (Button) findViewById(R.id.resetScore);

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
                        hasBooted = true;
                        Intent intent = new Intent(HomeScreen.this, video.class);
                        intent.putExtra("currentTime",currentTime);
                        intent.putExtra("isCountingDown",isCountingDown);
                        startActivityForResult(intent,2);

                        return true;
                    case R.id.action_glossary:
                       // mTextMessage.setText(R.string.title_stats);
                        hasBooted = true;
                        Intent glossaryIntent = new Intent(HomeScreen.this,glossary.class);
                        glossaryIntent.putExtra("currentTime",currentTime);
                        glossaryIntent.putExtra("isCountingDown",isCountingDown);
                        startActivityForResult(glossaryIntent,2);
                        return true;
                }
                return false;
            }
        });
        //Increases the score for the fencer on the left
        redPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldValue = Integer.parseInt(redScore.getText().toString());
                redScore.setText(String.valueOf(oldValue+1));
            }
        });
        //Decreases the score for the fener on the left
        redMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldValue = Integer.parseInt(redScore.getText().toString());
                if(oldValue > 0) {
                    redScore.setText(String.valueOf(oldValue - 1));
                }
            }
        });
        //Increases the score for the fencer on the right
        greenPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldValue = Integer.parseInt(greenScore.getText().toString());
                greenScore.setText(String.valueOf(oldValue+1));
            }
        });
        //Decreases the score for the fencer on the right
        greenMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldValue = Integer.parseInt(greenScore.getText().toString());
                if(oldValue > 0) {
                    greenScore.setText(String.valueOf(oldValue - 1));
                }
            }
        });

        /*
        Resumes the timer by creating a new CountDownTimer starting at whatever time is
        currently in the TextView
         */
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
        /*
        Stops the timer by canceling the last CountDownTimer that was in effect

         */
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setClickable(true);
                stop.setClickable(false);
                countDownTimer.cancel();
                isCountingDown = false;
            }
        });
        /*
        Resets the timer to the default time of 3 minutes. Stops the count down.
         */
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                start.setClickable(true);
                stop.setClickable(false);
                time.setText("3:00");
                currentTime = "3:00";
                isCountingDown = false;


            }
        });
        /*
        Adds 1 minute to the countdown. If the timer is running, then the current CountDownTimer
        must be cancelled so that a new one at the newly designated time can be created immediately
         after the TextView is updated.
         */
        addMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldTime = time.getText().toString();

                int newMinutes = Integer.parseInt(oldTime.substring(0,oldTime.indexOf(':')))+1;
                String newTime = String.valueOf(newMinutes) + oldTime.substring(oldTime.indexOf(':'));

                //timer not active, simply update textview
                if(!isCountingDown) {
                    time.setText(newTime);
                    currentTime = newTime;
                }
                //timer active, cancel timer and create new one
                else {
                    countDownTimer.cancel();
                    time.setText(newTime);
                    createCountDownTimer();
                }
            }
        });
         /*
        Removes 1 minute to the countdown. If the timer is running, then the current CountDownTimer
        must be cancelled so that a new one at the newly designated time can be created immediately
         after the TextView is updated.
         */
        removeMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldTime = time.getText().toString();
                int newMinutes = Integer.parseInt(oldTime.substring(0, oldTime.indexOf(':')));
                if (newMinutes > 1) {
                    newMinutes--;
                    String newTime = "";
                    newTime =  String.valueOf(newMinutes) + oldTime.substring(oldTime.indexOf(':'));
                    //timer not active, simply update textview
                    if(!isCountingDown) {
                        time.setText(newTime);
                        currentTime = newTime;
                    }
                    //timer active, cancel timer and create new one
                    else {
                        countDownTimer.cancel();
                        time.setText(newTime);
                        createCountDownTimer();
                    }

                }
                else{
                    Toast.makeText(HomeScreen.this, "Not enough time to remove more", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Resets both scores to 0.
        resetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redScore.setText("0");
                greenScore.setText("0");
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
        else if(item.getItemId() == R.id.action_faq){
            startActivity(new Intent(HomeScreen.this,faq.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //Creates a new CountDownTimer beginning at whatever time was left in the TextView
    private void createCountDownTimer(){
        currentTime = time.getText().toString();
        //compute the total amount of remaining milliseconds
        int minutes = Integer.parseInt(currentTime.substring(0,currentTime.indexOf(":")));
        int seconds = Integer.parseInt(currentTime.substring(currentTime.indexOf(":") + 1));
        long minutesToMilli = TimeUnit.MINUTES.toMillis(minutes);
        long secondsToMilli = TimeUnit.SECONDS.toMillis(seconds);
        long total = minutesToMilli + secondsToMilli;
        sharedPreferences = getSharedPreferences("count", 0);
        SharedPreferences.Editor edit= sharedPreferences.edit();
        edit.putBoolean("stop", false);
        edit.apply();


        countDownTimer = new CountDownTimer(total,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTime = time.getText().toString();
                //if the timer is stopped in the video activity, this will run
                if (sharedPreferences.getBoolean("stop", true)) {
                    countDownTimer.cancel();
                    start.setClickable(true);
                    stop.setClickable(false);
                    isCountingDown=false;
                }
                //update the TextView on every tick
                else {
                    String currentTime = String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    time.setText(currentTime);
                }

            }

            @Override
            //bring this activity to the front when the timer runs out
            public void onFinish() {
                time.setText("0:00");
                start.setClickable(true);
                stop.setClickable(false);
            }
        }.start();

    }
    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null)
            setIntent(intent);
    }
    @Override
    public void onResume(){
        super.onResume();

        //Don't do anything when the application initially launches
        if(hasBooted) {
            //get intent variables
            Intent intent = getIntent();
            String timeFromEarlierActivity = "";
            Boolean wasCountingDown = false;
            Bundle extras = intent.getExtras();
            boolean finished = false;
            if(extras !=null) {
                 timeFromEarlierActivity = extras.getString("currentTime");
                wasCountingDown = extras.getBoolean("isCountingDown");
                finished = extras.getBoolean("finished");
            }
            /*
             *if the timer has expired in another activity while the timer in the main activity
             * is not running, then manually set the time to 0.
             */
            if(finished){
                time.setText("0:00");
                start.setClickable(true);
                stop.setClickable(false);
                currentTime = "0:00";
            }

            else {
                /*
                 * If the timer was resumed in video mode and the timer was not running in the
                 * scoring activity, then a new timer must be created upon launching this activity.
                 */
                if (wasCountingDown && !isCountingDown) {
                    currentTime = timeFromEarlierActivity;
                    time.setText(currentTime);
                    createCountDownTimer();
                    start.setClickable(false);
                    stop.setClickable(true);
                    isCountingDown = true;
                }
            }
        }
    }
    //Implemented for when the back button is used in video or glossary mode.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(resultCode == RESULT_OK) {
                String timeFromEarlierActivity = data.getStringExtra("currentTime");
                Boolean wasCountingDown = data.getBooleanExtra("isCountingDown",false);
                //create new timer if it was stopped in the scoring activity and running in the previous activity
                if(wasCountingDown && !isCountingDown){
                    currentTime = timeFromEarlierActivity;
                    time.setText(currentTime);
                    createCountDownTimer();
                    start.setClickable(false);
                    stop.setClickable(true);
                    isCountingDown = true;
                }

            }
        }
    }
    /*
        if back is used, then data must be passed to the glossary or scoring mode to prevent the
        scoring activity timer from missing data.
     */
    @Override
    public void onBackPressed()
    {
        //put extra value in intent
        Intent intent = new Intent();
        intent.putExtra("currentTime",currentTime);
        intent.putExtra("isCountingDown",isCountingDown);        setResult(RESULT_OK, intent);
        //execute standard back button function
        super.onBackPressed();
    }


}
