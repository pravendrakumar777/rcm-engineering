package com.niiran.software.solutions.resource.rest;

import com.niiran.software.solutions.domain.Account;
import com.niiran.software.solutions.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

//@RestController
//@RequestMapping("/api/accounts")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<Account>> create(@RequestBody Account account) {
        log.info("REST Request to create: {}", account);
        return CompletableFuture
                .supplyAsync(() ->
                        accountService.createAccount(account))
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/update/{id}")
    public CompletableFuture<ResponseEntity<Account>> update(@PathVariable Long id, @RequestBody Account account) {
        log.info("REST Request to update: {}", id);
        return CompletableFuture.supplyAsync(() ->
                        accountService.updateAccount(id, account))
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/fetch/{id}")
    public CompletableFuture<ResponseEntity<Account>> getById(@PathVariable Long id) {
        log.info("REST Request to getById: {}", id);
        return CompletableFuture.supplyAsync(() ->
                        accountService.getAccountById(id))
                .thenApply(ResponseEntity::ok);
    }


    @GetMapping("/fetch-all")
    public CompletableFuture<ResponseEntity<List<Account>>> getAll() {
        log.info("REST Request to getAll");
        return accountService.getAllAccountsAsync()
                .thenApply(accounts -> ResponseEntity.ok(accounts));
    }


    @DeleteMapping("/delete/{id}")
    public CompletableFuture<ResponseEntity<String>> delete(@PathVariable Long id) {
        log.info("REST Request to delete: {}", id);
        return CompletableFuture.runAsync(() ->
                        accountService.deleteAccount(id))
                .thenApply(v -> ResponseEntity.ok("Account deleted successfully"));
    }

    @PutMapping("/update/{id}/balance")
    public CompletableFuture<ResponseEntity<Account>> updateBalance(@PathVariable Long id, @RequestParam Double amount) {
        log.info("REST Request to updateBalance: {}", id);
        return CompletableFuture.supplyAsync(() ->
                        accountService.updateBalance(id, amount))
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/pay/{id}/pay-salary")
    public CompletableFuture<ResponseEntity<Account>> paySalary(@PathVariable Long id, @RequestParam Double netPay) {
        log.info("REST Request to paySalary: {}", id);
        return CompletableFuture.supplyAsync(() ->
                        accountService.markSalaryPaid(id, netPay))
                .thenApply(ResponseEntity::ok);
    }
}
