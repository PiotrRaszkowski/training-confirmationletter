package com.example.confirmationletter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.dao.CurrencyDao;
import com.example.domain.AmountAndRecordsPerBank;
import com.example.domain.Client;
import com.example.domain.ConfirmationLetter;
import com.example.domain.Currency;
import com.example.domain.HashBatchRecordsBalance;
import com.example.domain.Record;
import com.example.record.command.FileUploadCommand;
import com.example.record.domain.FaultRecord;
import com.example.record.domain.TempRecord;
import com.example.record.parser.FileExtension;
import com.example.record.service.impl.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfirmationLetterGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ConfirmationLetterGenerator.class);

    private String crediting;
    private String debiting;
    private String debit;
    private String credit;
    private String type;
    private LetterSelector letterSelector;
    private CurrencyDao currencyDao;

    public byte[] generate(FileUploadCommand fileUploadCommand, Client client, HashBatchRecordsBalance hashBatchRecordsBalance, String branchName, List<AmountAndRecordsPerBank> bankMap, List<FaultRecord> faultyRecords, FileExtension extension, List<Record> records, List<TempRecord> faultyAccountNumberRecordList, List<TempRecord> sansDuplicateFaultRecordsList) {
        ConfirmationLetter letter = buildConfirmationLetter(fileUploadCommand, client, hashBatchRecordsBalance, branchName, bankMap, faultyRecords, extension, records, faultyAccountNumberRecordList, sansDuplicateFaultRecordsList);

        return convertLetterToPdf(client, letter);
    }

    private byte[] convertLetterToPdf(Client client, ConfirmationLetter letter) {
        OurOwnByteArrayOutputStream arrayOutputStream = letterSelector.generateLetter(client.getCreditDebit(), letter);

        return arrayOutputStream.toByteArray();
    }

    private ConfirmationLetter buildConfirmationLetter(FileUploadCommand fileUploadCommand, Client client, HashBatchRecordsBalance hashBatchRecordsBalance, String branchName, List<AmountAndRecordsPerBank> bankMap, List<FaultRecord> faultyRecords, FileExtension extension, List<Record> records, List<TempRecord> faultyAccountNumberRecordList, List<TempRecord> sansDuplicateFaultRecordsList) {
        ConfirmationLetter letter = new ConfirmationLetter();
        letter.setCurrency(records.get(0).getCurrency());
        letter.setExtension(extension);
        letter.setCreditingErrors(faultyRecords);
        letter.setClient(client);
        letter.setBranchName(branchName);
        letter.setBanks(bankMap);
        letter.setTotalRetrievedRecords(fileUploadCommand.getTotalRecords());

        letter.setTransferType(hashBatchRecordsBalance.getCollectionType());
        letter.setHashTotalCredit(hashBatchRecordsBalance.getHashTotalCredit().toString());
        letter.setHashTotalDebit(hashBatchRecordsBalance.getHashTotalDebit().toString());
        letter.setTotalProcessedRecords(hashBatchRecordsBalance.getRecordsTotal().toString());

        letter.setBatchTotalDebit(hashBatchRecordsBalance.computeBatchTotals(client.getAmountDivider(), Constants.DEBIT).toString());
        letter.setBatchTotalCredit(hashBatchRecordsBalance.computeBatchTotals(client.getAmountDivider(), Constants.CREDIT).toString());

        letter.setTransactionCost(computeTransactionCost(fileUploadCommand, hashBatchRecordsBalance));

        Map<String, BigDecimal> retrievedAmounts = calculateRetrieveAmounts(records, client, faultyAccountNumberRecordList, sansDuplicateFaultRecordsList);
        letter.setRetrievedAmountEur(retrievedAmounts.get(Constants.CURRENCY_EURO));
        letter.setRetrievedAmountFL(retrievedAmounts.get(Constants.CURRENCY_FL));
        letter.setRetrievedAmountUsd(retrievedAmounts.get(Constants.CURRENCY_FL));

        return letter;
    }

    private String computeTransactionCost(FileUploadCommand fileUploadCommand, HashBatchRecordsBalance hashBatchRecordsBalance) {
        String transactionCost = "";
        if (fileUploadCommand.hasFee()) {
            transactionCost = hashBatchRecordsBalance.getTotalFee().toString();
        }
        return transactionCost;
    }

    // Calculate sum amount from faultyAccountnumber list

    private class RetrieveAmountsContainer {
        BigDecimal recordAmount = BigDecimal.ZERO;

        BigDecimal recordAmountDebit = BigDecimal.ZERO;
        BigDecimal recordAmountCredit = BigDecimal.ZERO;

        BigDecimal amountSansDebit = BigDecimal.ZERO;
        BigDecimal amountSansCredit = BigDecimal.ZERO;

        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;
    }

    private Map<String, RetrieveAmountsContainer> calculateRetrieveAmounts(List<Record> records, Client client, List<TempRecord> faultyAccountNumberRecordList, List<TempRecord> sansDuplicateFaultRecordsList) {

        Map<String, RetrieveAmountsContainer> retrievedAmounts = new HashMap<>();

        BigDecimal recordAmountFL = BigDecimal.ZERO;
        BigDecimal recordAmountUSD = BigDecimal.ZERO;
        BigDecimal recordAmountEUR = BigDecimal.ZERO;

        BigDecimal recordAmountDebitFL = BigDecimal.ZERO;
        BigDecimal recordAmountDebitEUR = BigDecimal.ZERO;
        BigDecimal recordAmountDebitUSD = BigDecimal.ZERO;

        BigDecimal recordAmountCreditFL = BigDecimal.ZERO;
        BigDecimal recordAmountCreditEUR = BigDecimal.ZERO;
        BigDecimal recordAmountCreditUSD = BigDecimal.ZERO;

        BigDecimal amountSansDebitFL = BigDecimal.ZERO;
        BigDecimal amountSansDebitUSD = BigDecimal.ZERO;
        BigDecimal amountSansDebitEUR = BigDecimal.ZERO;

        BigDecimal amountSansCreditFL = BigDecimal.ZERO;
        BigDecimal amountSansCreditUSD = BigDecimal.ZERO;
        BigDecimal amountSansCreditEUR = BigDecimal.ZERO;

        BigDecimal totalDebitFL = BigDecimal.ZERO;
        BigDecimal totalDebitUSD = BigDecimal.ZERO;
        BigDecimal totalDebitEUR = BigDecimal.ZERO;

        BigDecimal totalCreditFL = BigDecimal.ZERO;
        BigDecimal totalCreditUSD = BigDecimal.ZERO;
        BigDecimal totalCreditEUR = BigDecimal.ZERO;

        if (client.isBalanced()) {
            countRecordsForBalancedClient(records, retrievedAmounts);
        } else {
            countNotCounterTransferRecordsForNotBalanced(records, retrievedAmounts);
            countSansDuplicateTempRecords(client, sansDuplicateFaultRecordsList, retrievedAmounts);

            Map<String, BigDecimal> retrievedAccountNumberAmounts = calculateAmountsFaultyAccountNumber(faultyAccountNumberRecordList, client);

            RetrieveAmountsContainer container = retrievedAmounts.get(Constants.CURRENCY_FL);
            countTotals(container, retrievedAccountNumberAmounts.get("FaultyAccDebitFL"), retrievedAccountNumberAmounts.get("FaultyAccCreditFL"));

            container = retrievedAmounts.get(Constants.CURRENCY_USD);
            countTotals(container, retrievedAccountNumberAmounts.get("FaultyAccDebitUSD"), retrievedAccountNumberAmounts.get("FaultyAccCreditUSD"));

            container = retrievedAmounts.get(Constants.CURRENCY_EURO);
            countTotals(container, retrievedAccountNumberAmounts.get("FaultyAccDebitEUR"), retrievedAccountNumberAmounts.get("FaultyAccCreditEUR"));

            recordAmountFL = totalDebitFL.subtract(totalCreditFL).abs();
            recordAmountUSD = totalDebitUSD.subtract(totalCreditUSD).abs();
            recordAmountEUR = totalDebitEUR.subtract(totalCreditEUR).abs();

            retrievedAmounts.put(Constants.CURRENCY_EURO, recordAmountEUR);
            retrievedAmounts.put(Constants.CURRENCY_FL, recordAmountUSD);
            retrievedAmounts.put(Constants.CURRENCY_FL, recordAmountFL);

        }

        return retrievedAmounts;
    }

    private void countTotals(RetrieveAmountsContainer container, BigDecimal faultAccountDebit, BigDecimal faultAccountCredit) {
        container.totalDebit = container.recordAmountDebit.add(container.amountSansDebit).subtract(faultAccountDebit);
        container.totalCredit = container.recordAmountCredit.add(container.amountSansCredit).subtract(faultAccountCredit);
    }

    private void countSansDuplicateTempRecords(Client client, List<TempRecord> sansDuplicateFaultRecordsList, Map<String, RetrieveAmountsContainer> retrievedAmounts) {
        for (TempRecord tempRecord : sansDuplicateFaultRecordsList) {
            resolveTempRecordSignIfUnset(tempRecord, client);
            resolveTempRecordCurrencyIfUnset(tempRecord, client);

            String currencyIsoCode = resolveCurrencyByCode(tempRecord.getCurrencyCode());

            RetrieveAmountsContainer container = retrievedAmounts.get(currencyIsoCode);

            if (tempRecord.isDebitTempRecord()) {
                container.amountSansDebit = container.amountSansDebit.add(new BigDecimal(tempRecord.getAmount()));
            } else {
                container.amountSansCredit = container.amountSansCredit.add(new BigDecimal(tempRecord.getAmount()));
            }
        }
    }

    private void resolveTempRecordCurrencyIfUnset(TempRecord tempRecord, Client client) {
        if (tempRecord.getCurrencyCode() == null) {
            String currencyId = currencyDao.retrieveCurrencyDefault(client.getProfile());
            Currency currency = currencyDao.retrieveCurrencyOnId(new Integer(currencyId));
            tempRecord.setCurrencycode(currency.getCode());
        }
    }

    private void resolveTempRecordSignIfUnset(TempRecord tempRecord, Client client) {
        if (tempRecord.getSign() == null) {
            String sign = client.getCreditDebit();
            tempRecord.setSign(sign);
        }
    }

    private boolean hasEurCurrency(Integer currencyCode) {
        return currencyCode.equals(Constants.EUR_CURRENCY_CODE);
    }

    private boolean hasUsdCurrency(Integer currencyCode) {
        return currencyCode.equals(Constants.USD_CURRENCY_CODE);
    }

    private boolean hasFlCurrency(Integer currencyCode) {
        return currencyCode.equals(Constants.FL_CURRENCY_CODE) || currencyCode.equals(Constants.FL_CURRENCY_CODE_FOR_WEIRD_BANK);
    }

    private void countNotCounterTransferRecordsForNotBalanced(List<Record> records, Map<String, RetrieveAmountsContainer> retrievedAmounts) {
        for (Record record : records) {
            LOG.debug("COUNTERTRANSFER [" + record.getIsCounterTransferRecord() + "] FEERECORD [" + record.getFeeRecord() + "]");
            if (record.isNoCounterTransferRecord() && record.hasNoFee()) {
                String currencyIsoCode = resolveCurrencyByCode(record.getCurrencyCode());
                RetrieveAmountsContainer container = retrievedAmounts.get(currencyIsoCode);

                if (record.isCredit()) {
                    container.recordAmountCredit = container.recordAmountCredit.add(record.getAmount());
                }
                if (record.isDebit()) {
                    container.recordAmountDebit = container.recordAmountDebit.add(record.getAmount());
                }
            }
        }
    }

    private void countRecordsForBalancedClient(List<Record> records, Map<String, RetrieveAmountsContainer> retrievedAmounts) {
        for (Record record : records) {
            if (record.getFeeRecord() != 1 && record.isDebit()) {
                addAmountToTotal(retrievedAmounts, record);
            }
        }
    }

    private void addAmountToTotal(Map<String, RetrieveAmountsContainer> retrievedAmounts, Record record) {
        String currencyIsoCode = resolveCurrencyByCode(record.getCurrencyCode());

        RetrieveAmountsContainer container = retrievedAmounts.get(currencyIsoCode);

        container.recordAmount = container.recordAmount.add(record.getAmount());
    }

    private String resolveCurrencyByCode(Integer currencyCode) {
        if (Constants.FL_CURRENCY_CODE.equals(currencyCode) || Constants.FL_CURRENCY_CODE_FOR_WEIRD_BANK.equals(currencyCode)) {
            return Constants.CURRENCY_FL;
        } else if (Constants.USD_CURRENCY_CODE.equals(currencyCode)) {
            return Constants.CURRENCY_USD;
        } else if (Constants.EUR_CURRENCY_CODE.equals(currencyCode)) {
            return Constants.CURRENCY_EURO;
        } else {
            throw new IllegalArgumentException("Not supported currency code! Valid currencies are: FL(3), USD(1), EUR(2).");
        }
    }

    private Map<String, BigDecimal> calculateAmountsFaultyAccountNumber(List<TempRecord> faultyAccountNumberRecordList, Client client) {
        Map<String, BigDecimal> retrievedAmountsFaultyAccountNumber = new HashMap<String, BigDecimal>();

        BigDecimal faultyAccRecordAmountCreditFL = new BigDecimal(0);
        BigDecimal faultyAccRecordAmountCreditUSD = new BigDecimal(0);
        BigDecimal faultyAccRecordAmountCreditEUR = new BigDecimal(0);

        BigDecimal faultyAccRecordAmountDebitFL = new BigDecimal(0);
        BigDecimal faultyAccRecordAmountDebitUSD = new BigDecimal(0);
        BigDecimal faultyAccRecordAmountDebitEUR = new BigDecimal(0);

        for (TempRecord faultyAccountNumberRecord : faultyAccountNumberRecordList) {
            if (StringUtils.isBlank(faultyAccountNumberRecord.getSign())) {
                faultyAccountNumberRecord.setSign(client.getCreditDebit());
            }

            resolveTempRecordCurrencyIfUnset(faultyAccountNumberRecord, client);

            if (hasFlCurrency(faultyAccountNumberRecord.getCurrencyCode())) {
                if (faultyAccountNumberRecord.isDebitTempRecord()) {
                    faultyAccRecordAmountDebitFL = new BigDecimal(faultyAccountNumberRecord.getAmount()).add(faultyAccRecordAmountDebitFL);
                } else {
                    faultyAccRecordAmountCreditFL = new BigDecimal(faultyAccountNumberRecord.getAmount()).add(faultyAccRecordAmountCreditFL);
                }
            }
            if (hasUsdCurrency(faultyAccountNumberRecord.getCurrencyCode())) {
                if (faultyAccountNumberRecord.isDebitTempRecord()) {
                    faultyAccRecordAmountDebitUSD = new BigDecimal(faultyAccountNumberRecord.getAmount()).add(faultyAccRecordAmountDebitUSD);
                } else {
                    faultyAccRecordAmountCreditUSD = new BigDecimal(faultyAccountNumberRecord.getAmount()).add(faultyAccRecordAmountCreditUSD);
                }
            }
            if (hasEurCurrency(faultyAccountNumberRecord.getCurrencyCode())) {
                if (faultyAccountNumberRecord.isDebitTempRecord()) {
                    faultyAccRecordAmountDebitEUR = new BigDecimal(faultyAccountNumberRecord.getAmount()).add(faultyAccRecordAmountDebitEUR);
                } else {
                    faultyAccRecordAmountCreditEUR = new BigDecimal(faultyAccountNumberRecord.getAmount()).add(faultyAccRecordAmountCreditEUR);
                }
            }

            retrievedAmountsFaultyAccountNumber.put("FaultyAccDebitFL", faultyAccRecordAmountDebitFL);
            retrievedAmountsFaultyAccountNumber.put("FaultyAccDebitUSD", faultyAccRecordAmountDebitUSD);
            retrievedAmountsFaultyAccountNumber.put("FaultyAccDebitEUR", faultyAccRecordAmountDebitEUR);

            retrievedAmountsFaultyAccountNumber.put("FaultyAccCreditFL", faultyAccRecordAmountCreditFL);
            retrievedAmountsFaultyAccountNumber.put("FaultyAccCreditUSD", faultyAccRecordAmountCreditUSD);
            retrievedAmountsFaultyAccountNumber.put("FaultyAccCreditEUR", faultyAccRecordAmountCreditEUR);

        }
        return retrievedAmountsFaultyAccountNumber;
    }

    private List<AmountAndRecordsPerBank> amountAndRecords(List<Record> records, String transactionType) {
        List<AmountAndRecordsPerBank> list = new ArrayList<AmountAndRecordsPerBank>();
        String typeOfTransaction = transactionType.equalsIgnoreCase(crediting) ? crediting : debiting;
        type = typeOfTransaction.equalsIgnoreCase(crediting) ? credit : debit;
        if (transactionType.equalsIgnoreCase(typeOfTransaction)) {
            for (Record record : records) {
                getAmountAndRecords(record, list, transactionType);
            }
        }
        return list;
    }

    private List<AmountAndRecordsPerBank> getAmountAndRecords(Record record, List<AmountAndRecordsPerBank> list, String transactionType) {
        Map<String, String> map = new HashMap<String, String>();
        if (record.getFeeRecord().compareTo(0) == 0 && !map.containsKey(record.getBeneficiaryName())) {

            if (transactionType.equalsIgnoreCase(Constants.CREDITING)) {

                if (record.getBeneficiaryName() != null && !record.getBeneficiaryName().equalsIgnoreCase(Constants.RBTT_BANK_ALTERNATE)) {
                    Boolean newList = true;
                    if (list.size() == 0 && record.getSign().equalsIgnoreCase(type)) {
                        // logger.info("bank gegevens: "+record.getSign()+" : "+record.getBank().getName()+" : "+record.getBeneficiaryName());
                        AmountAndRecordsPerBank aARPB = new AmountAndRecordsPerBank();
                        aARPB.setBankName(record.getBank().getName());
                        aARPB.setTotalRecord(1);
                        aARPB.setAmount(record.getAmount());
                        aARPB.setCurrencyType(record.getCurrency().getCurrencyType());
                        aARPB.setAccountNumber(record.getBeneficiaryAccountNumber());
                        list.add(aARPB);
                        newList = false;
                    }
                    if (newList && record.getSign().equalsIgnoreCase(type)) {
                        // logger.info("bank gegevens: "+record.getSign()+" : "+record.getBank().getName()+" : "+record.getBeneficiaryName());
                        Boolean newRecord = true;
                        for (AmountAndRecordsPerBank object : list) {
                            if (object.getBankName().equalsIgnoreCase(record.getBank().getName()) && object.getCurrencyType().equalsIgnoreCase(record.getCurrency().getCurrencyType())) {
                                object.setAmount(object.getAmount().add(record.getAmount()));
                                object.setTotalRecord(object.getTotalRecord() + 1);
                                newRecord = false;
                            }
                        }
                        if (newRecord) {
                            AmountAndRecordsPerBank aARPB = new AmountAndRecordsPerBank();
                            aARPB.setBankName(record.getBank().getName());
                            aARPB.setTotalRecord(1);
                            aARPB.setAmount(record.getAmount());
                            aARPB.setCurrencyType(record.getCurrency().getCurrencyType());
                            aARPB.setAccountNumber(record.getBeneficiaryAccountNumber());
                            list.add(aARPB);
                        }
                    }
                }
            }

            // del begin
            if (transactionType.equalsIgnoreCase(Constants.DEBITING)) {

                if (record.getBeneficiaryName() == null) {
                    Boolean newList = true;
                    if (list.size() == 0 && record.getSign().equalsIgnoreCase(type)) {
                        // logger.info("bank gegevens: "+record.getSign()+" : "+record.getBank().getName()+" : "+record.getBeneficiaryName());
                        AmountAndRecordsPerBank aARPB = new AmountAndRecordsPerBank();
                        aARPB.setBankName(record.getBank().getName());
                        aARPB.setTotalRecord(1);
                        aARPB.setAmount(record.getAmount());
                        aARPB.setCurrencyType(record.getCurrency().getCurrencyType());
                        aARPB.setAccountNumber(record.getBeneficiaryAccountNumber());
                        list.add(aARPB);
                        newList = false;
                    }
                    if (newList && record.getSign().equalsIgnoreCase(type)) {
                        // logger.info("bank gegevens: "+record.getSign()+" : "+record.getBank().getName()+" : "+record.getBeneficiaryName());
                        Boolean newRecord = true;
                        for (AmountAndRecordsPerBank object : list) {
                            if (object.getBankName().equalsIgnoreCase(record.getBank().getName()) && object.getCurrencyType().equalsIgnoreCase(record.getCurrency().getCurrencyType())) {
                                object.setAmount(object.getAmount().add(record.getAmount()));
                                object.setTotalRecord(object.getTotalRecord() + 1);
                                newRecord = false;
                            }
                        }
                        if (newRecord) {
                            AmountAndRecordsPerBank aARPB = new AmountAndRecordsPerBank();
                            aARPB.setBankName(record.getBank().getName());
                            aARPB.setTotalRecord(1);
                            aARPB.setAmount(record.getAmount());
                            aARPB.setCurrencyType(record.getCurrency().getCurrencyType());
                            aARPB.setAccountNumber(record.getBeneficiaryAccountNumber());
                            list.add(aARPB);
                        }
                    }
                }
            }
            // del end
        }
        return list;
    }

    public CurrencyDao getCurrencyDao() {
        return currencyDao;
    }

    public void setCurrencyDao(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    public void setCrediting(String crediting) {
        this.crediting = crediting;
    }

    public void setDebiting(String debiting) {
        this.debiting = debiting;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public void setLetterSelector(LetterSelector letterSelector) {
        this.letterSelector = letterSelector;
    }

}