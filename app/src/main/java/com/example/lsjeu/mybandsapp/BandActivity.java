package com.example.lsjeu.mybandsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class BandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band);

        Bundle bundle = getIntent().getExtras();
        String band = bundle.getString("band");

        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText(band);
    }
}
