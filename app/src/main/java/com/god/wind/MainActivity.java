package com.god.wind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click_a(View v){
        startActivity(new Intent(this, AActivity.class));
    }

    public void click_b(View v){
        startActivity(new Intent(this, BActivity.class));
    }

    public void click_c(View v){
        startActivity(new Intent(this, CActivity.class));
    }
}
