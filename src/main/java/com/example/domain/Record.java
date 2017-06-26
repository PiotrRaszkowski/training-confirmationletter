package com.example.domain;

import java.math.BigDecimal;

public class Record {

    private Currency currency;

    private Integer feeRecord;

    private String sign;

    private BigDecimal amount;

    private Integer isCounterTransferRecord;

    private String beneficiaryName;

    private Bank bank;

    public class Bank {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private String beneficiaryAccountNumber;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Integer getFeeRecord() {
        return feeRecord;
    }

    public void setFeeRecord(Integer feeRecord) {
        this.feeRecord = feeRecord;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getIsCounterTransferRecord() {
        return isCounterTransferRecord;
    }

    public void setCounterTransferRecord(Integer counterTransferRecord) {
        isCounterTransferRecord = counterTransferRecord;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getBeneficiaryAccountNumber() {
        return beneficiaryAccountNumber;
    }

    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }
}
