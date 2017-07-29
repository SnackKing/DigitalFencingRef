package com.allegretti.zach.OnTargetFencing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class about extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textview = (TextView) findViewById(R.id.about);
        textview.setMovementMethod(new ScrollingMovementMethod());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.app_bar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== R.id.action_about){
            //do nothing
        }
        else if(item.getItemId() == R.id.action_contact){
            startActivity(new Intent(about.this,contact.class));
        }
       else if (item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.action_faq){
            startActivity(new Intent(about.this,faq.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
