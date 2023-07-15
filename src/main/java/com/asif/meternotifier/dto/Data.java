package com.asif.meternotifier.dto;

public class Data {
    private String accountNo;
    private String meterNo;
    private double balance;
    private String currentMonthConsumption;
    private String readingTime;

    public Data() {
    }

    public Data(String accountNo,
                String meterNo,
                double balance,
                String currentMonthConsumption,
                String readingTime) {
        this.accountNo = accountNo;
        this.meterNo = meterNo;
        this.balance = balance;
        this.currentMonthConsumption = currentMonthConsumption;
        this.readingTime = readingTime;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrentMonthConsumption() {
        return currentMonthConsumption;
    }

    public void setCurrentMonthConsumption(String currentMonthConsumption) {
        this.currentMonthConsumption = currentMonthConsumption;
    }

    public String getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(String readingTime) {
        this.readingTime = readingTime;
    }

    @Override
    public String toString() {
        return "Data{" +
                "accountNo='" + accountNo + '\'' +
                ", meterNo='" + meterNo + '\'' +
                ", balance='" + balance + '\'' +
                ", currentMonthConsumption='" + currentMonthConsumption + '\'' +
                ", readingTime='" + readingTime + '\'' +
                '}';
    }
}
