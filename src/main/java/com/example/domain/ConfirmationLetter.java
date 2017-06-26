package com.example.domain;

import java.math.BigDecimal;
import java.util.List;

import com.example.record.domain.FaultRecord;
import com.example.record.parser.FileExtension;

public class ConfirmationLetter {

    private Currency currency;

    private FileExtension extension;

    private String hashTotalCredit;

    private String hashTotalDebit;

    private String batchTotalDebit;

    private String batchTotalCredit;

    private String totalProcessedRecords;

    private String transactionCost;

    private List<FaultRecord> creditingErrors;

    private Client client;

    private String branchName;

    private BigDecimal retrievedAmountEur;

    private BigDecimal retrievedAmountFL;

    private BigDecimal retrievedAmountUsd;

    private String totalRetrievedRecords;

    private List<AmountAndRecordsPerBank> banks;

    private String transferType;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public FileExtension getExtension() {
        return extension;
    }

    public void setExtension(FileExtension extension) {
        this.extension = extension;
    }

    public String getHashTotalCredit() {
        return hashTotalCredit;
    }

    public void setHashTotalCredit(String hashTotalCredit) {
        this.hashTotalCredit = hashTotalCredit;
    }

    public String getHashTotalDebit() {
        return hashTotalDebit;
    }

    public void setHashTotalDebit(String hashTotalDebit) {
        this.hashTotalDebit = hashTotalDebit;
    }

    public String getBatchTotalDebit() {
        return batchTotalDebit;
    }

    public void setBatchTotalDebit(String batchTotalDebit) {
        this.batchTotalDebit = batchTotalDebit;
    }

    public String getBatchTotalCredit() {
        return batchTotalCredit;
    }

    public void setBatchTotalCredit(String batchTotalCredit) {
        this.batchTotalCredit = batchTotalCredit;
    }

    public String getTotalProcessedRecords() {
        return totalProcessedRecords;
    }

    public void setTotalProcessedRecords(String totalProcessedRecords) {
        this.totalProcessedRecords = totalProcessedRecords;
    }

    public String getTransactionCost() {
        return transactionCost;
    }

    public void setTransactionCost(String transactionCost) {
        this.transactionCost = transactionCost;
    }

    public List<AmountAndRecordsPerBank> getBanks() {
        return banks;
    }

    public void setBanks(List<AmountAndRecordsPerBank> banks) {
        this.banks = banks;
    }

    public List<FaultRecord> getCreditingErrors() {
        return creditingErrors;
    }

    public void setCreditingErrors(List<FaultRecord> creditingErrors) {
        this.creditingErrors = creditingErrors;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public BigDecimal getRetrievedAmountEur() {
        return retrievedAmountEur;
    }

    public void setRetrievedAmountEur(BigDecimal retrievedAmountEur) {
        this.retrievedAmountEur = retrievedAmountEur;
    }

    public BigDecimal getRetrievedAmountFL() {
        return retrievedAmountFL;
    }

    public void setRetrievedAmountFL(BigDecimal retrievedAmountFL) {
        this.retrievedAmountFL = retrievedAmountFL;
    }

    public BigDecimal getRetrievedAmountUsd() {
        return retrievedAmountUsd;
    }

    public void setRetrievedAmountUsd(BigDecimal retrievedAmountUsd) {
        this.retrievedAmountUsd = retrievedAmountUsd;
    }

    public String getTotalRetrievedRecords() {
        return totalRetrievedRecords;
    }

    public void setTotalRetrievedRecords(String totalRetrievedRecords) {
        this.totalRetrievedRecords = totalRetrievedRecords;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
}

