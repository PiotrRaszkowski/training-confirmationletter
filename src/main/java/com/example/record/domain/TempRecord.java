package com.example.record.domain;

import com.example.record.service.impl.Constants;

public class TempRecord {

    private String sign;

    private Integer currencycode;

    private String amount;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getCurrencyCode() {
        return currencycode;
    }

    public void setCurrencycode(Integer currencycode) {
        this.currencycode = currencycode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isDebitTempRecord() {
        return getSign().equalsIgnoreCase(Constants.DEBIT);
    }
}
