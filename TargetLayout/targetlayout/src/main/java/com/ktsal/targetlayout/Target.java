package com.ktsal.targetlayout;


import java.util.List;
import java.util.Locale;

public class Target implements TargetAction {

    private List<Level> levels;
    private int currentPosition;

    public Target(List<Level> levels) {
        this(levels, 0);
    }

    public Target(List<Level> levels, int initialPosition) {
        this.levels = levels;
        currentPosition = initialPosition;
    }

    public Level getCurrentLevel() {
        return levels.get(currentPosition);
    }

    public Level getLevelAt(int position) {
        return levels.get(position);
    }

    @Override
    public void increment() {
        if (hasUpperBoundAtCurrent())
            currentPosition++;
    }

    @Override
    public void setPosition(int position) {
        if (isWithinBounds(position))
            currentPosition = position;
    }

    @Override
    public void decrement() {
        if (hasLowerBoundAtCurrent())
            currentPosition--;
    }

    public boolean hasUpperBoundAtCurrent() {
        return hasUpperBoundsAtPosition(currentPosition);
    }

    public boolean hasUpperBoundsAtPosition(int position) {
        return position < levels.size() - 1;
    }

    public boolean hasLowerBoundAtCurrent() {
        return hasLowerBoundAtPosition(currentPosition);
    }

    public boolean hasLowerBoundAtPosition(int position) {
        return position > 0;
    }

    public boolean isWithinBounds(int position) {
        return position >= 0 && position < levels.size();
    }

    public static class Level {

        private final int position;
        private final float sizePercent;

        public Level(int position, float sizePercent) {
            this.position = position;
            this.sizePercent = sizePercent;
        }

        public int getPosition() {
            return position;
        }

        public float getSizePercent() {
            return sizePercent;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Level))
                return false;
            if (this == object)
                return true;
            Level otherLevel = (Level) object;
            return (this.position == otherLevel.position
                    && this.sizePercent == otherLevel.sizePercent);
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + position;
            result = 31 * result + Float.floatToIntBits(sizePercent);
            return result;
        }

        @Override
        public String toString() {
            return String.format(Locale.getDefault(),
                    "Level(position: %d, sizePercent: %.5f)", position, sizePercent);
        }
    }
}
