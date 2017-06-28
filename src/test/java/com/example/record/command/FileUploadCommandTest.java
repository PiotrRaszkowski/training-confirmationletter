package com.example.record.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FileUploadCommandTest {

    @InjectMocks
    private FileUploadCommand fileUploadCommand;

    @Test
    public void hasFeeGivenNull() throws Exception {
        //GIVEN
        fileUploadCommand.setFee(null);

        //WHEN
        boolean result = fileUploadCommand.hasFee();

        //THEN
        assertFalse(result);
    }

    @Test
    public void hasFeeGivenEmpty() throws Exception {
        //GIVEN
        fileUploadCommand.setFee("");

        //WHEN
        boolean result = fileUploadCommand.hasFee();

        //THEN
        assertFalse(result);
    }

    @Test
    public void hasFeeGivenFalseString() throws Exception {
        //GIVEN
        fileUploadCommand.setFee("False");

        //WHEN
        boolean result = fileUploadCommand.hasFee();

        //THEN
        assertFalse(result);
    }

    @Test
    public void hasFeeGivenYesString() throws Exception {
        //GIVEN
        fileUploadCommand.setFee("TrUe");

        //WHEN
        boolean result = fileUploadCommand.hasFee();

        //THEN
        assertTrue(result);
    }
}