package com.tsalik.targetlayout;


import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import static org.junit.Assert.assertThat;

public class LevelTest {

    private Target.Level level;

    @Before
    public void setUpLevel() {
        this.level = new Target.Level(0, 0.1f);
    }

    @Test
    public void Level_EqualsWithSelf_ReturnsTrue() {
        assertThat(level, equalTo(level));
    }

    @Test
    public void Level_EqualsWithNull_ReturnsFalse() {
        assertThat(level, not(equalTo(null)));
    }

    @Test
    public void Level_EqualsWithSameLevelValues_ReturnsTrue() {
        Target.Level other = new Target.Level(0, 0.1f);
        assertThat(level, equalTo(other));
    }

    @Test
    public void Level_EqualsWithDifferentPosition_ReturnsFalse() {
        Target.Level other = new Target.Level(1, 0.1f);
        assertThat(level, not(equalTo(other)));
    }

    @Test
    public void Level_EqualsWithDifferentSizePercent_ReturnsFalse() {
        Target.Level other = new Target.Level(0, 0.2f);
        assertThat(level, not(equalTo(other)));
    }

    @Test
    public void Level_EqualsCheckSymmetry_ReturnsTrue() {
        Target.Level other = new Target.Level(0, 0.1f);
        assertThat(level, equalTo(other));
        assertThat(other, equalTo(level));
    }

    @Test
    public void Level_EqualsCheckTransitivity_ReturnsTrue() {
        Target.Level level1 = new Target.Level(0, 0.1f);
        Target.Level level2 = new Target.Level(0, 0.1f);
        assertThat(level, equalTo(level1));
        assertThat(level, equalTo(level2));
        assertThat(level1, equalTo(level2));
    }

    @Test
    public void Level_HashCodeWithSaveValues_ReturnsSameHashCodes() {
        Target.Level other = new Target.Level(0, 0.1f);
        assertThat(level.hashCode(), equalTo(other.hashCode()));
    }

    @Test
    public void Level_HashCodeWithDifferentValues_ReturnsDifferentHashCodes() {
        Target.Level other = new Target.Level(1, 0.2f);
        assertThat(level.hashCode(), not(equalTo(other.hashCode())));
    }

}
