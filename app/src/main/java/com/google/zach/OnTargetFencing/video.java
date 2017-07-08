package com.google.zach.OnTargetFencing;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import android.widget.VideoView;
import android.content.pm.PackageManager;

import java.util.concurrent.TimeUnit;

public class video extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    VideoView videoView;
    private static final int VIDEO_CAPTURE = 101;
    Uri videoUri;
    final int PERMISION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    CountDownTimer countDownTimer;

    String currentTimeForGlossary;
    boolean isCountingDown;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_score:
                  //  mTextMessage.setText(R.string.title_home);
                    Intent homeIntent = new Intent(video.this,HomeScreen.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityIfNeeded(homeIntent, 0);
                    return true;
                case R.id.action_video:
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.action_stats:
                    //mTextMessage.setText(R.string.title_notifications);
                    Intent glossaryIntent = new Intent(video.this,glossary.class);
                    glossaryIntent.putExtra("currentTime",currentTimeForGlossary);
                    glossaryIntent.putExtra("isCountingDown",isCountingDown);
                    startActivityForResult(glossaryIntent,0);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        String timeFromEarlierActivity = getIntent().getStringExtra("currentTime");
        boolean wasCountingDown = getIntent().getBooleanExtra("isCountingDown",false);
        isCountingDown = wasCountingDown;
        videoView = (VideoView) findViewById(R.id.videoView);
        Button stop = (Button) findViewById(R.id.stop);
        Button play = (Button) findViewById(R.id.play);
        final TextView timer = (TextView) findViewById(R.id.time);
        //Leave the timer at the time from the last activity.
        timer.setText(timeFromEarlierActivity);
        /*
        If the timer was running when leaving the last activity, then a new CountDownTimer must
        be created beginning at the time from the last CountDownTimer.
         */
        if(wasCountingDown) {
            int minutes = Integer.parseInt(timeFromEarlierActivity.substring(0, timeFromEarlierActivity.indexOf(":")));
            int seconds = Integer.parseInt(timeFromEarlierActivity.substring(timeFromEarlierActivity.indexOf(":") + 1));
            long minutesToMilli = TimeUnit.MINUTES.toMillis(minutes);
            long secondsToMilli = TimeUnit.SECONDS.toMillis(seconds);
            long total = minutesToMilli + secondsToMilli;
            countDownTimer = new CountDownTimer(total, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    SharedPreferences sharedPreferences = getSharedPreferences("count", 0);
                    String currentTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    timer.setText(currentTime);
                    currentTimeForGlossary = currentTime;
                }

                @Override
                public void onFinish() {
                    timer.setText("0:00");
                    //Bring the home screen to the front
                    Intent homeIntent = new Intent(video.this,HomeScreen.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityIfNeeded(homeIntent, 0);
                }
            }.start();
            //Cancel the timer that is in effect. Also cancels the timer that is in the home screen.
            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences("count",0);
                    SharedPreferences.Editor edit= sharedPreferences.edit();
                    edit.putBoolean("stop", true);
                    edit.apply();
                    countDownTimer.cancel();
                    isCountingDown=false;
                }
            });
        }





        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePermisions();
            }
        });




    }
    /*
    Check to see if the app has the permissions needed to take video
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /*

     */
    public void handlePermisions(){
        //If the app does not have the needed permissions, ask again
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISION_ALL);
        }
        //If all permissions are granted, then begin recording
        else{
            startRecordingVideo();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISION_ALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    startRecordingVideo();

                } else {

                    // permission denied
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }
    public void startRecordingVideo() {

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
    //Play recorded video in the VideoView
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.setMediaController(new MediaController(this));
            videoView.requestFocus();
            videoView.start();
        }
    }

//    public void playbackRecordedVideo() {
//        VideoView mVideoView = (VideoView) findViewById(R.id.videoView);
//        mVideoView.setVideoURI(videoUri);
//        mVideoView.setMediaController(new MediaController(this));
//        mVideoView.requestFocus();
//        mVideoView.start();
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.app_bar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== R.id.action_about){
            startActivity(new Intent(video.this,about.class));
        }
        else if(item.getItemId() == R.id.action_contact){
            startActivity(new Intent(video.this,contact.class));
        }
        else if(item.getItemId() == R.id.action_faq){
            startActivity(new Intent(video.this,faq.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
