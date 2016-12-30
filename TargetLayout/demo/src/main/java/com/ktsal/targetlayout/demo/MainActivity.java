package com.ktsal.targetlayout.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ktsal.targetlayout.TargetLayout;

public class MainActivity extends AppCompatActivity {

    private TargetLayout targetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        targetLayout = (TargetLayout) findViewById(R.id.targetLayout);
        findViewById(R.id.increment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetLayout.increment();
            }
        });
        findViewById(R.id.decrement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetLayout.decrement();
            }
        });
    }
}
