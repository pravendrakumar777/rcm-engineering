package com.niiran.software.solutions.resource;

import com.niiran.software.solutions.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountViewController {
    private final AccountService accountService;

    public AccountViewController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/leave-managements")
    public String accountsPage() {
        return "accounts";
    }
}
