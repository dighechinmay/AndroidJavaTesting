package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NegativeNumberValidatorTest {


    NegativeNumberValidator SUT;


    @Before
    public void setup(){ SUT = new NegativeNumberValidator();}


    @Test
    public void test1(){
        Boolean result = SUT.isNegative(-1);
        assertThat(result,is(true));
    }

    @Test
    public void test2(){
        Boolean result = SUT.isNegative(1);
        assertThat(result,is(false));
    }

    @Test
    public void test3(){
        Boolean result = SUT.isNegative(0);
        assertThat(result,is(false));
    }


}