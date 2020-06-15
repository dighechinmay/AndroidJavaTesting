package com.techyourchance.unittestingfundamentals.exercise2;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StringDuplicatorTest {

    StringDuplicator SUT;

    @Before
    public void setUp() throws Exception {

        SUT = new StringDuplicator();
    }


    @Test
    public void duplicate_emptyString_emptyStringReturned() {
        String result = SUT.duplicate("");
        assertThat(result,is(""));
    }


    @Test
    public void duplicate_singleChar_doubleCharReturned() {
        String result = SUT.duplicate("a");
        assertThat(result,is("aa"));
    }

    @Test
    public void duplicate_regString_regStringReturned() {
        String result = SUT.duplicate("chinmay");
        assertThat(result,is("chinmaychinmay"));
    }
}