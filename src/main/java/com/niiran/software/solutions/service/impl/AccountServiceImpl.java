package com.niiran.software.solutions.service.impl;

import com.niiran.software.solutions.domain.Account;
import com.niiran.software.solutions.exceptions.ResourceNotFoundException;
import com.niiran.software.solutions.repository.AccountRepository;
import com.niiran.software.solutions.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Account account) {
        log.info("Service Request to createAccount: {}", account);
        account.setCreatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Long accountId, Account updatedAccount) {
        log.info("Service Request to updateAccount: {}", accountId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        account.setBankName(updatedAccount.getBankName());
        account.setBranchName(updatedAccount.getBranchName());
        account.setIfscCode(updatedAccount.getIfscCode());
        account.setBankAccountNumber(updatedAccount.getBankAccountNumber());
        account.setUpiId(updatedAccount.getUpiId());
        account.setAccountType(updatedAccount.getAccountType());
        account.setBasicSalary(updatedAccount.getBasicSalary());
        account.setAllowances(updatedAccount.getAllowances());
        account.setDeductions(updatedAccount.getDeductions());
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long accountId) {
        log.info("Service Request to getAccountById: {}", accountId);
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    @Async
    @Override
    public CompletableFuture<List<Account>> getAllAccountsAsync() {
        log.info("Service Request to getAllAccountsAsync: {}");
        return CompletableFuture.supplyAsync(accountRepository::findAll);
    }

    @Override
    public void deleteAccount(Long accountId) {
        log.info("Service Request to deleteAccount: {}", accountId);
        Account account = getAccountById(accountId);
        accountRepository.delete(account);
    }

    @Override
    public Account updateBalance(Long accountId, Double amount) {
        log.info("Service Request to updateBalance: {}", accountId);
        Account account = getAccountById(accountId);
        account.setCurrentBalance(account.getCurrentBalance() + amount);
        return accountRepository.save(account);
    }

    @Override
    public Account markSalaryPaid(Long accountId, Double netPay) {
        log.info("Service Request to markSalaryPaid: {}", accountId);
        Account account = getAccountById(accountId);
        account.setNetPay(netPay);
        account.setLastPaidDate(LocalDate.now());
        account.setCurrentBalance(account.getCurrentBalance() - netPay);
        return accountRepository.save(account);
    }
}
