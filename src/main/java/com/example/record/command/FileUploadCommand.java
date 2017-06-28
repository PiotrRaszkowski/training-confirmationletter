package com.example.record.command;

import com.example.record.service.impl.Constants;

public class FileUploadCommand {

    private String fee;

    private String totalRecords;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public boolean hasFee() {
        return Constants.YES.equalsIgnoreCase(getFee());
    }
}
