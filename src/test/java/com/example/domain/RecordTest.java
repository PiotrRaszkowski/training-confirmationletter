package com.example.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RecordTest {

    @InjectMocks
    private Record record;

    @Test
    public void isNoCounterTransferRecordGiven1ThenTrue() {
        //GIVEN
        record.setCounterTransferRecord(1);

        //WHEN
        boolean result = record.isNoCounterTransferRecord();

        //THEN
        assertFalse(result);
    }

    @Test
    public void isNoCounterTransferRecordGiven0ThenFalse() {
        //GIVEN
        record.setCounterTransferRecord(0);

        //WHEN
        boolean result = record.isNoCounterTransferRecord();

        //THEN
        assertTrue(result);
    }

    @Test
    public void hasNoFeeGiven1ThenTrue() throws Exception {
        //GIVEN
        record.setFeeRecord(1);

        //WHEN
        boolean result = record.hasNoFee();

        //THEN
        assertFalse(result);
    }

    @Test
    public void hasNoFeeGiven0ThenFalse() throws Exception {
        //GIVEN
        record.setFeeRecord(0);

        //WHEN
        boolean result = record.hasNoFee();

        //THEN
        assertTrue(result);
    }
}