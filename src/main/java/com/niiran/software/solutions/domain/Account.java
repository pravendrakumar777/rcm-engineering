package com.niiran.software.solutions.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_details")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private Long employeeId;
    private String accountType; // EMPLOYEE, COMPANY, PAYROLL

    // Salary Details
    private Double basicSalary;
    private Double hra;
    private Double da;
    private Double allowances;
    private Double deductions;
    private Double netPay;
    private LocalDate lastPaidDate;

    // Banking Details
    private String bankName;
    private String branchName;
    private String ifscCode;
    private String bankAccountNumber;
    private String upiId;

    // Financial Metrics
    private Double openingBalance;
    private Double currentBalance;
    private Double pendingAmount;
    private LocalDate lastTransactionDate;

    // Challan Information
    private Integer totalChallansGenerated;
    private Double challanPaidAmount;
    private Double challanDueAmount;

    // Compliance
    private String panNumber;
    private Double tdsPercentage;

    // System Fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status; // ACTIVE, INACTIVE

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        status = "ACTIVE";
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Double getHra() {
        return hra;
    }

    public void setHra(Double hra) {
        this.hra = hra;
    }

    public Double getDa() {
        return da;
    }

    public void setDa(Double da) {
        this.da = da;
    }

    public Double getAllowances() {
        return allowances;
    }

    public void setAllowances(Double allowances) {
        this.allowances = allowances;
    }

    public Double getDeductions() {
        return deductions;
    }

    public void setDeductions(Double deductions) {
        this.deductions = deductions;
    }

    public Double getNetPay() {
        return netPay;
    }

    public void setNetPay(Double netPay) {
        this.netPay = netPay;
    }

    public LocalDate getLastPaidDate() {
        return lastPaidDate;
    }

    public void setLastPaidDate(LocalDate lastPaidDate) {
        this.lastPaidDate = lastPaidDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(Double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public LocalDate getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(LocalDate lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public Integer getTotalChallansGenerated() {
        return totalChallansGenerated;
    }

    public void setTotalChallansGenerated(Integer totalChallansGenerated) {
        this.totalChallansGenerated = totalChallansGenerated;
    }

    public Double getChallanPaidAmount() {
        return challanPaidAmount;
    }

    public void setChallanPaidAmount(Double challanPaidAmount) {
        this.challanPaidAmount = challanPaidAmount;
    }

    public Double getChallanDueAmount() {
        return challanDueAmount;
    }

    public void setChallanDueAmount(Double challanDueAmount) {
        this.challanDueAmount = challanDueAmount;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public Double getTdsPercentage() {
        return tdsPercentage;
    }

    public void setTdsPercentage(Double tdsPercentage) {
        this.tdsPercentage = tdsPercentage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
