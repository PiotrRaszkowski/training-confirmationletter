package com.example.domain;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BatchTotalTest {

    private static final BigDecimal CREDIT_COUNTER_VALUE_FOR_DEBIT = new BigDecimal("34234.12");
    private static final BigDecimal CREDIT_VALUE = new BigDecimal("894.123");

    @InjectMocks
    private BatchTotal batchTotal;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        batchTotal.setCreditValue(CREDIT_VALUE);
        batchTotal.setCreditCounterValueForDebit(CREDIT_COUNTER_VALUE_FOR_DEBIT);
    }

    @Test
    public void getValueForSignGivenCredit() throws Exception {
        //GIVEN

        //WHEN
        BigDecimal value = batchTotal.getValueForSign("cReDiT");

        //THEN
        assertEquals(CREDIT_VALUE, value);
    }

    @Test
    public void getValueForSignGivenDebit() throws Exception {
        //GIVEN

        //WHEN
        BigDecimal value = batchTotal.getValueForSign("DEBit");

        //THEN
        assertEquals(CREDIT_COUNTER_VALUE_FOR_DEBIT, value);
    }

    @Test
    public void getValueForSignGivenNotSupportedSignThenThrowsException() throws Exception {
        //GIVEN

        //WHEN
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Sign value is not supported! Valid are: CREDIT, DEBIT.");

        batchTotal.getValueForSign("some strange value");

        //THEN
    }
}