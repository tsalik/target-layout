package com.ktsal.targetlayout;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class TargetLayout extends FrameLayout implements TargetAction {

    private static final String TAG = TargetLayout.class.getSimpleName();

    private static final float DEFAULT_CENTER_PERCENT = 0F;
    private static final float DEFAULT_STEP_PERCENT = 0F;
    private static final int DEFAULT_MAX_NUMBER_OF_LEVELS = 0;
    private static final int DEFAULT_CENTER_VIEW_MARGIN = 0;

    private Target target;
    private float centerPercent;
    private float stepPercent;
    private int maxNumberOfLevels;
    private LevelListDrawable levelListDrawable;
    private Rect drawingBounds = new Rect();
    private View centerView;
    private float centerViewMargin;
    private Interpolator centerViewInterpolator = new BounceInterpolator();

    public TargetLayout(Context context) {
        super(context);
        init(context, null);
    }

    public TargetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onFinishInflate() {

        if (getChildCount() > 1)
            throw new IllegalStateException("TargetLayout can have exactly one child");

        centerView = getChildAt(0);
        if (centerView != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) centerView.getLayoutParams();
            lp.gravity = Gravity.CENTER;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (centerView != null) {

            Target.Level level = target.getLevelAt(0);
            float sizePercent = level.getSizePercent();
            int targetLayoutSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
            int centerViewSize = Math.round((targetLayoutSize * sizePercent) - centerViewMargin);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) centerView.getLayoutParams();
            lp.width = centerViewSize;
            lp.height = centerViewSize;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int canvasWidth = canvas.getWidth();

        int canvasHeight = canvas.getHeight();

        Target.Level current = target.getCurrentLevel();
        if (current != null) {
            fixStepPercentIfNeeded(canvasWidth, canvasHeight);
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
        animateLevelTransition();
    }

    @Override
    public void setPosition(int position) {
        target.setPosition(position);
        invalidate();
        animateLevelTransition();
    }

    @Override
    public void decrement() {
        target.decrement();
        invalidate();
        animateLevelTransition();
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
            centerViewMargin = ta.getDimensionPixelSize(R.styleable.TargetLayout_center_view_margin, DEFAULT_CENTER_VIEW_MARGIN) * 4;
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
    }

    private void fixStepPercentIfNeeded(int width, int height) {
        int maxViewSize = Math.min(width, height);
        Target.Level maxLevel = target.getLevelAt(maxNumberOfLevels - 1);
        Target.Level centerLevel = target.getLevelAt(0);
        if (maxLevel != null && centerLevel != null) {
            computeDrawingBounds(width, height, maxLevel.getSizePercent());
            int maxLevelSize = drawingBounds.width();
            computeDrawingBounds(width, height, centerLevel.getSizePercent());
            int centerSize = drawingBounds.width();
            if (maxLevelSize > maxViewSize) {
                stepPercent = (float) (maxViewSize - centerSize) / (maxViewSize * (maxNumberOfLevels - 1));
                updateTarget();
            }
        }
    }

    private void animateLevelTransition() {
        float scaleFrom = centerView.getScaleX();
        Target.Level currentLevel = target.getCurrentLevel();
        float scaleTo = currentLevel.getSizePercent() / target.getLevelAt(0).getSizePercent();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(centerView, "scaleX", scaleFrom, scaleTo);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(centerView, "scaleY", scaleFrom, scaleTo);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(centerViewInterpolator);
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.start();
    }

}
