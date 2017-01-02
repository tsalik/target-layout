package com.ktsal.targetlayout.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ktsal.targetlayout.TargetLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TargetLayout.OnLevelChangedListener {

    private TargetLayout targetLayout;
    private List<ProgrammerLevel> programmerLevels;
    private TextView programmerLevelTextView;

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
        programmerLevels = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            programmerLevels.add(new ProgrammerLevel(i, "level: " + i));
        }
        programmerLevelTextView = (TextView) findViewById(R.id.programmerLevel);
        targetLayout.setOnLevelChangedListener(this);
        ArrayAdapter<ProgrammerLevel> programmerLevelArrayAdapter = new ArrayAdapter<>();
        targetLayout.setAdapter(programmerLevelArrayAdapter);
        programmerLevelArrayAdapter.updateItems(programmerLevels);
        targetLayout.setPosition(1);
    }


    @Override
    public void onLevelChanged(int position) {
        ProgrammerLevel programmerLevel = programmerLevels.get(position);
        programmerLevelTextView.setText(programmerLevel.getTitle());
    }

}
