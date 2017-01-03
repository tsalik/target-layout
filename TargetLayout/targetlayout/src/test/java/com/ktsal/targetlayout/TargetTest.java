package com.ktsal.targetlayout;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

public class TargetTest {

    private Target target;
    private static final Target.Level[] LEVELS = new Target.Level[] {
        new Target.Level(0, 0.1f), new Target.Level(1, 0.2f), new Target.Level(2, 0.3f),
        new Target.Level(3, 0.4f)
    };

    @Before
    public void setUpTarget() {
        target = new Target(Arrays.asList(LEVELS));
    }

    @Test
    public void Target_AfterInitialization_LevelIsZero() {
        Target.Level currentLevel = target.getCurrentLevel();
        assertThat(currentLevel, equalTo(LEVELS[0]));
    }

    @Test
    public void Target_IncrementWithPositionInBounds_IncrementsLevel() {
        target.increment();
        Target.Level currentLevel = target.getCurrentLevel();
        assertThat(currentLevel, equalTo(LEVELS[1]));
    }

    @Test
    public void Target_IncrementWithPositionOutOfBounds_LevelStaysTheSame() {
        target.setPosition(3);
        target.increment();
        Target.Level currentLevel = target.getCurrentLevel();
        assertThat(currentLevel, equalTo(LEVELS[3]));
    }

    @Test
    public void Target_DecrementWithPositionInBounds() {
        target.setPosition(2);
        target.decrement();
        Target.Level currentLevel = target.getCurrentLevel();
        assertThat(currentLevel, equalTo(LEVELS[1]));
    }

    @Test
    public void Target_DecrementWithPositionOutOfBounds_LevelStaysTheSame() {
        target.decrement();
        Target.Level currentLevel = target.getCurrentLevel();
        assertThat(currentLevel, equalTo(LEVELS[0]));
    }

    @Test
    public void Target_SetPositionWithPositionInBounds_ChangesLevelToPosition() {
        target.setPosition(2);
        Target.Level currentLevel = target.getCurrentLevel();
        assertThat(currentLevel, equalTo(LEVELS[2]));
    }

    @Test
    public void Target_SetPositionWithLargerThanLevelSize_LevelStaysTheSame() {
        target.setPosition(10);
        Target.Level currentLevel = target.getCurrentLevel();
        assertThat(currentLevel, equalTo(LEVELS[0]));
    }

    @Test
    public void Target_SetPositionWithNegativeLevelPosition_LevelStaysTheSame() {
        target.setPosition(-1);
        Target.Level currentLevel = target.getCurrentLevel();
        assertThat(currentLevel, equalTo(LEVELS[0]));
    }

}