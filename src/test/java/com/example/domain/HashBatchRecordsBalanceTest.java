package com.example.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.record.service.impl.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HashBatchRecordsBalanceTest {

    @Spy
    @InjectMocks
    private HashBatchRecordsBalance hashBatchRecordsBalance;

    @Test
    public void computeBatchTotalsGivenDebit() throws Exception {
        //GIVEN
        BatchTotal batchTotal1 = new BatchTotal();
        batchTotal1.setCreditCounterValueForDebit(BigDecimal.ONE);

        BatchTotal batchTotal2 = new BatchTotal();
        batchTotal2.setCreditCounterValueForDebit(BigDecimal.ZERO);

        BatchTotal batchTotal3 = new BatchTotal();
        batchTotal3.setCreditValue(BigDecimal.TEN);

        when(hashBatchRecordsBalance.getBatchTotalsValues()).thenReturn(Arrays.asList(batchTotal1, batchTotal2, batchTotal3));

        //WHEN
        BigDecimal debitBatchTotal = hashBatchRecordsBalance.computeBatchTotals("1", Constants.DEBIT);

        //THEN
        assertEquals(new BigDecimal("1"), debitBatchTotal);
    }

    @Test
    public void computeBatchTotalsGivenCredit() throws Exception {
        //GIVEN
        BatchTotal batchTotal1 = new BatchTotal();
        batchTotal1.setCreditCounterValueForDebit(BigDecimal.TEN);

        BatchTotal batchTotal2 = new BatchTotal();
        batchTotal2.setCreditValue(BigDecimal.ZERO);

        BatchTotal batchTotal3 = new BatchTotal();
        batchTotal3.setCreditValue(BigDecimal.TEN);

        when(hashBatchRecordsBalance.getBatchTotalsValues()).thenReturn(Stream.of(batchTotal1, batchTotal2, batchTotal3).collect(Collectors.toList()));

        //WHEN
        BigDecimal creditBatchTotal = hashBatchRecordsBalance.computeBatchTotals("1", Constants.CREDIT);

        //THEN
        assertEquals(new BigDecimal("10"), creditBatchTotal);
    }
}