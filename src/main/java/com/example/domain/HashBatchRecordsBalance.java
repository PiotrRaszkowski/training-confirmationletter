package com.example.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HashBatchRecordsBalance {

    private BigDecimal hashTotalCredit;

    private BigDecimal hashTotalDebit;

    private Map<Integer, BatchTotal> batchTotals = new HashMap<>();

    private Long recordsTotal;

    private String collectionType;

    private String totalFee;

    public BigDecimal getHashTotalCredit() {
        return hashTotalCredit;
    }

    public void setHashTotalCredit(BigDecimal hashTotalCredit) {
        this.hashTotalCredit = hashTotalCredit;
    }

    public BigDecimal getHashTotalDebit() {
        return hashTotalDebit;
    }

    public void setHashTotalDebit(BigDecimal hashTotalDebit) {
        this.hashTotalDebit = hashTotalDebit;
    }

    public Map<Integer, BatchTotal> getBatchTotals() {
        return batchTotals;
    }

    public void setBatchTotals(Map<Integer, BatchTotal> batchTotals) {
        this.batchTotals = batchTotals;
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public Collection<BatchTotal> getBatchTotalsValues() {
        return batchTotals.values();
    }

    public BigDecimal computeBatchTotals(String amountDivider, String sign) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BatchTotal total : getBatchTotalsValues()) {
            BigDecimal value = total.getValueForSign(sign);
            sum = sum.add(value);
        }
        return sum.divide(new BigDecimal(amountDivider));
    }
}
