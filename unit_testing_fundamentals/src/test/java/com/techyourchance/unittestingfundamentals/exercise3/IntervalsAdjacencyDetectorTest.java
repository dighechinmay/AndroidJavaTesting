package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new IntervalsAdjacencyDetector();
    }



    // interval1 adjacent before interval2 ->

    @Test
    public void isAdjacent_interval1BeforeInterval2_returnTrue() {
        Interval interval1 = new Interval(-5,1);
        Interval interval2 = new Interval(1,9);
        Boolean result = SUT.isAdjacent(interval1,interval2);
        assertThat(result, is(true));
    }

    //interval 1 adjacent after interval 2
    @Test
    public void isAdjacent_interval1AfterInterval2_returnTrue() {
        Interval interval1 = new Interval(1,10);
        Interval interval2 = new Interval(-50,1);
        Boolean result = SUT.isAdjacent(interval1,interval2);
        assertThat(result, is(true));
    }

    // interval 1 overlaps interval 2 before
    @Test
    public void isAdjacent_interval1OverlapsBeforeInterval2_returnFalse() {
        Interval interval1 = new Interval(1,10);
        Interval interval2 = new Interval(8,90);
        Boolean result = SUT.isAdjacent(interval1,interval2);
        assertThat(result, is(false));
    }

    //interval 1 overlaps interval 2 after
    @Test
    public void isAdjacent_interval1OverlapsAfterInterval2_returnFalse() {
        Interval interval1 = new Interval(-80,-20);
        Interval interval2 = new Interval(-500,-70);
        Boolean result = SUT.isAdjacent(interval1,interval2);
        assertThat(result, is(false));
    }

    // interval 1 inside interval 2
    @Test
    public void isAdjacent_interval1InsideInterval2_returnFalse() {
        Interval interval1 = new Interval(5,40);
        Interval interval2 = new Interval(1,100);
        Boolean result = SUT.isAdjacent(interval1,interval2);
        assertThat(result, is(false));
    }

    //interval 2 inside interval 1
    @Test
    public void isAdjacent_interval2InsideInterval1_returnFalse() {
        Interval interval1 = new Interval(-5,20);
        Interval interval2 = new Interval(7,10);
        Boolean result = SUT.isAdjacent(interval1,interval2);
        assertThat(result, is(false));
    }


    //interval 1 equals interval 2
    @Test
    public void isAdjacent_interval1EqualsInterval2_returnFalse() {
        Interval interval1 = new Interval(-5,20);
        Interval interval2 = new Interval(-5,20);
        Boolean result = SUT.isAdjacent(interval1,interval2);
        assertThat(result, is(false));
    }


}