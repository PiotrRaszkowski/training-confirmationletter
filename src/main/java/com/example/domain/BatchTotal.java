package com.example.domain;

import java.math.BigDecimal;

import com.example.record.service.impl.Constants;

public class BatchTotal {

    private BigDecimal creditValue = BigDecimal.ZERO;

    private BigDecimal creditCounterValueForDebit = BigDecimal.ZERO;

    public BigDecimal getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(BigDecimal creditValue) {
        this.creditValue = creditValue;
    }

    public BigDecimal getCreditCounterValueForDebit() {
        return creditCounterValueForDebit;
    }

    public void setCreditCounterValueForDebit(BigDecimal creditCounterValueForDebit) {
        this.creditCounterValueForDebit = creditCounterValueForDebit;
    }

    public BigDecimal getValueForSign(String sign) {
        if (Constants.CREDIT.equalsIgnoreCase(sign)) {
            return creditValue;
        } else if (Constants.DEBIT.equalsIgnoreCase(sign)) {
            return creditCounterValueForDebit;
        } else {
            throw new IllegalArgumentException("Sign value is not supported! Valid are: CREDIT, DEBIT.");
        }

    }
}
