package com.tsalik.targetlayout;


import java.util.List;
import java.util.Locale;

class Target implements TargetAction {

    private List<Level> levels;
    private int currentPosition;

    Target(List<Level> levels) {
        this(levels, 0);
    }

    Target(List<Level> levels, int initialPosition) {
        this.levels = levels;
        currentPosition = initialPosition;
    }

    Level getCurrentLevel() {
        return levels.get(currentPosition);
    }

    Level getLevelAt(int position) {
        if (isWithinBounds(position))
            return levels.get(position);
        else
            return null;
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

    boolean hasUpperBoundAtCurrent() {
        return hasUpperBoundsAtPosition(currentPosition);
    }

    boolean hasUpperBoundsAtPosition(int position) {
        return position < levels.size() - 1;
    }

    boolean hasLowerBoundAtCurrent() {
        return hasLowerBoundAtPosition(currentPosition);
    }

    boolean hasLowerBoundAtPosition(int position) {
        return position > 0;
    }

    boolean isWithinBounds(int position) {
        return position >= 0 && position < levels.size();
    }

    float getBound(boolean grow) {
        Level current = getCurrentLevel();
        Level target = null;
        if (grow && hasUpperBoundAtCurrent()) {
            target = getLevelAt(currentPosition + 1);
        } else if (!grow & hasLowerBoundAtCurrent()) {
            target = getLevelAt(currentPosition - 1);
        }

        if (target != null)
            return ((target.getSizePercent() + current.getSizePercent())) / 2;
        else
            return 0;
    }

    static class Level {

        private final int position;
        private final float sizePercent;

        Level(int position, float sizePercent) {
            this.position = position;
            this.sizePercent = sizePercent;
        }

        int getPosition() {
            return position;
        }

        float getSizePercent() {
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
