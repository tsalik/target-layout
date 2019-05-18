package com.tsalik.targetlayout.demo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.tsalik.targetlayout.TargetLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TargetLayout.OnLevelChangedListener {

    private TargetLayout targetLayout;
    private List<ProgrammerLevel> programmerLevels;
    private TextView programmerLevelTextView;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        targetLayout = findViewById(R.id.targetLayout);
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
        for (int i = 0; i < 4; i++) {
            programmerLevels.add(new ProgrammerLevel(i, "level: " + i));
        }
        programmerLevelTextView = findViewById(R.id.programmerLevel);
        targetLayout.setOnLevelChangedListener(this);
        ArrayAdapter<ProgrammerLevel> programmerLevelArrayAdapter = new ArrayAdapter<>();
        targetLayout.setAdapter(programmerLevelArrayAdapter);
        programmerLevelArrayAdapter.updateItems(programmerLevels);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){});
        gestureDetector.setOnDoubleTapListener(tapListener);
        targetLayout.setPosition(1);
        targetLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }


    @Override
    public void onLevelChanged(int position) {
        ProgrammerLevel programmerLevel = programmerLevels.get(position);
        programmerLevelTextView.setText(programmerLevel.getTitle());
    }

    private GestureDetector.OnDoubleTapListener tapListener = new GestureDetector.OnDoubleTapListener() {

        private boolean grow = true;

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (targetLayout.hasReachedMaxLevel())
                grow = false;
            else if (targetLayout.hasReachedMinimumLevel())
                grow = true;

            if (grow)
                targetLayout.increment();
            else
                targetLayout.decrement();

            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    };

}
