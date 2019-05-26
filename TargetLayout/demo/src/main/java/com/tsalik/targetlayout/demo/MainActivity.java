package com.tsalik.targetlayout.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ramotion.fluidslider.FluidSlider;
import com.tsalik.targetlayout.TargetLayout;

import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity implements TargetLayout.OnLevelChangedListener {

    private TargetLayout targetLayout;
    private TextView programmerLevelTextView;
    private GestureDetector gestureDetector;

    @SuppressLint("ClickableViewAccessibility")
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
        targetLayout.setMaxNumberOfLevels(4);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {});
        gestureDetector.setOnDoubleTapListener(tapListener);
        targetLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });
        setupSlider();
    }

    private void setupSlider() {
        final FluidSlider fluidSlider = findViewById(R.id.fluidSlider);
        fluidSlider.setPositionListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float position) {
                float stepPercent = position * targetLayout.getMaxStepPercent();
                fluidSlider.setBubbleText(String.format(Locale.getDefault(), "%.1f%%", stepPercent * 100));
                targetLayout.setStepPercent(stepPercent);
                return null;
            }
        });
        fluidSlider.setStartText("0");
        targetLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fluidSlider.setEndText(String.format(Locale.getDefault(), "%.1f%%", targetLayout.getMaxStepPercent() * 100));
                targetLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
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
