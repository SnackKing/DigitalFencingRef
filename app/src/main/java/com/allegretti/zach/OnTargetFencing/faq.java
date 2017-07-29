package com.allegretti.zach.OnTargetFencing;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class faq extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textview = (TextView) findViewById(R.id.faq);

        textview.setMovementMethod(new ScrollingMovementMethod());
        //Apparently html does not work in textviews, which is sad.
        SpannableString ss;
        ss = new SpannableString(getString(R.string.heading));
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), 0);
        textview.append(ss);

        ss=  new SpannableString(getString(R.string.q1));
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), 0);
        textview.append(ss);

        textview.append(getString(R.string.a1));

        ss = new SpannableString(getString(R.string.q2));
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), 0);
        textview.append(ss);

        textview.append(getString(R.string.a2));

        ss = new SpannableString(getString(R.string.q3));
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), 0);
        textview.append(ss);

        textview.append(getString(R.string.a3));


        ss = new SpannableString(getString(R.string.q5));
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), 0);
        textview.append(ss);

        textview.append(getString(R.string.a5));

        ss = new SpannableString(getString(R.string.q6));
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), 0);
        textview.append(ss);

        textview.append(getString(R.string.a6));

        ss = new SpannableString((getString(R.string.q7)));
        ss.setSpan(new StyleSpan(Typeface.BOLD),0,ss.length(),0);
        textview.append(ss);

        textview.append(getString(R.string.a7));



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.app_bar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== R.id.action_about){
            startActivity(new Intent(faq.this,about.class));
            return true;        }
        else if(item.getItemId() == R.id.action_contact){
            startActivity(new Intent(faq.this,contact.class));
        }
        else if (item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.action_faq){
           //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

}
