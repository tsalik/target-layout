package com.ktsal.targetlayout;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TargetLayout extends FrameLayout implements TargetAction {

    private static final String TAG = TargetLayout.class.getSimpleName();

    private static final float DEFAULT_CENTER_PERCENT = 0F;
    private static final float DEFAULT_STEP_PERCENT = 0F;
    private static final int DEFAULT_MAX_NUMBER_OF_LEVELS = 0;

    private Target target;
    private float centerPercent;
    private float stepPercent;
    private int maxNumberOfLevels;
    private LevelListDrawable levelListDrawable;
    private Rect drawingBounds = new Rect();

    public TargetLayout(Context context) {
        super(context);
        init(context, null);
    }

    public TargetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        fixStepPercentIfNeeded(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int canvasWidth = canvas.getWidth();

        int canvasHeight = canvas.getHeight();

        Log.i(TAG, String.format(Locale.getDefault(), "canvas width: %d, height: %d", canvasWidth, canvasHeight));
        Target.Level current = target.getCurrentLevel();
        if (current != null) {
            for (int position = current.getPosition(); position < maxNumberOfLevels; position++) {
                // draw always from the first drawable to the last
                levelListDrawable.setLevel(position - current.getPosition());
                Drawable drawable = levelListDrawable.getCurrent();
                computeDrawingBounds(canvasWidth, canvasHeight, target.getLevelAt(position).getSizePercent());
                drawable.setBounds(drawingBounds);
                drawable.draw(canvas);
            }
        }
    }

    @Override
    public void increment() {
        target.increment();
        invalidate();
    }

    @Override
    public void setPosition(int position) {
        target.setPosition(position);
        invalidate();
    }

    @Override
    public void decrement() {
        target.decrement();
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {

        setWillNotDraw(false);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TargetLayout);
            centerPercent = ta.getFloat(R.styleable.TargetLayout_center_percent, DEFAULT_CENTER_PERCENT);
            stepPercent = ta.getFloat(R.styleable.TargetLayout_step_percent, DEFAULT_STEP_PERCENT);
            maxNumberOfLevels = ta.getInteger(R.styleable.TargetLayout_max_number_of_levels, DEFAULT_MAX_NUMBER_OF_LEVELS);
            try {
                levelListDrawable = (LevelListDrawable) ta.getDrawable(R.styleable.TargetLayout_levelDrawable);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("a LevelListDrawable must be passed");
            }
            updateTarget();
            ta.recycle();
        }
    }

    private void updateTarget() {
        List<Target.Level> levels = new ArrayList<>();
        float sizePercent = centerPercent;
        for (int position = 0; position < maxNumberOfLevels; position++) {
            levels.add(new Target.Level(position, sizePercent));
            sizePercent += stepPercent;
        }
        target = new Target(levels);
    }

    private void computeDrawingBounds(int width, int height, float percent) {
        int centerX = width / 2;
        int centerY = height / 2;

        int size = (width < height) ? width : height;
        int radius = Math.round((size * percent) / 2);

        drawingBounds.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        Log.i(TAG, String.format(Locale.getDefault(), "drawing width: %d, height: %d", drawingBounds.width(), drawingBounds.height()));
    }

    private void fixStepPercentIfNeeded(int width, int height) {

        Target.Level maxLevel = target.getLevelAt(maxNumberOfLevels - 1);
        if (maxLevel != null) {
            computeDrawingBounds(width, height, maxLevel.getSizePercent());
            int maxLevelSize = drawingBounds.width();
            int maxViewSize = Math.min(width, height);
            if (maxLevelSize > maxViewSize) {
                stepPercent = (float) (maxLevelSize - maxViewSize) / (maxViewSize * (maxNumberOfLevels - 1));
                updateTarget();
            }
        }
    }

}
