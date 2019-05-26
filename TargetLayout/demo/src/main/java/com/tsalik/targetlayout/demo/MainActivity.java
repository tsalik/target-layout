package com.tsalik.targetlayout.demo;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tsalik.targetlayout.TargetLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TargetLayout.OnLevelChangedListener {

    private TargetLayout targetLayout;
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
        programmerLevelTextView = findViewById(R.id.programmerLevel);
        targetLayout.setOnLevelChangedListener(this);
        targetLayout.setMaxNumberOfLevels(5);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {});
        gestureDetector.setOnDoubleTapListener(tapListener);
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
        programmerLevelTextView.setText(String.format(Locale.getDefault(), "Level: %d", position));
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
