package com.rcm.engineering.service;

import com.rcm.engineering.domain.Account;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AccountService {

    Account createAccount(Account account);
    Account updateAccount(Long accountId, Account account);
    Account getAccountById(Long accountId);
    CompletableFuture<List<Account>> getAllAccountsAsync();
    void deleteAccount(Long accountId);
    Account updateBalance(Long accountId, Double amount);
    Account markSalaryPaid(Long accountId, Double netPay);
}
