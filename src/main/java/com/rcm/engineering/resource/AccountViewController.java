package com.rcm.engineering.resource;

import com.rcm.engineering.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountViewController {
    private final AccountService accountService;

    public AccountViewController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public String accountsPage() {
        return "accounts";
    }
}
