package com.example.domain;

import com.example.record.service.impl.Constants;

public class Client {

    private String profile;

    private String counterTransfer;

    private String amountDivider;

    private String creditDebit;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCounterTransfer() {
        return counterTransfer;
    }

    public void setCounterTransfer(String counterTransfer) {
        this.counterTransfer = counterTransfer;
    }

    public String getAmountDivider() {
        return amountDivider;
    }

    public void setAmountDivider(String amountDivider) {
        this.amountDivider = amountDivider;
    }

    public String getCreditDebit() {
        return creditDebit;
    }

    public void setCreditDebit(String creditDebit) {
        this.creditDebit = creditDebit;
    }

    public boolean isBalanced() {
        return getCounterTransfer().equalsIgnoreCase(Constants.TRUE);
    }
}
