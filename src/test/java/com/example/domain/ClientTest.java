package com.example.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ClientTest {

    @InjectMocks
    private Client client;

    @Test
    public void isBalancedGivenCounterTransferAsYes() throws Exception {
        //GIVEN
        client.setCounterTransfer("yES");

        //WHEN
        boolean balanced = client.isBalanced();

        //THEN
        assertTrue(balanced);
    }

    @Test
    public void isBalancedGivenCounterTransferAsNo() throws Exception {
        //GIVEN
        client.setCounterTransfer("no");

        //WHEN
        boolean balanced = client.isBalanced();

        //THEN
        assertFalse(balanced);
    }

    @Test
    public void isBalancedGivenCounterTransferAsOther() throws Exception {
        //GIVEN
        client.setCounterTransfer("otHER");

        //WHEN
        boolean balanced = client.isBalanced();

        //THEN
        assertFalse(balanced);
    }
}