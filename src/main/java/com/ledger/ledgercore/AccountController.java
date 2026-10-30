package com.ledger.ledgercore;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public Account createAccount(@RequestBody BigDecimal initialBalance) {
        return accountService.createAccount(initialBalance);
    }

    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable Long id){
        return accountService.getAccount(id);
    }

    @PostMapping("/accounts/{id}/deposit")
    public Account deposit(
            @PathVariable Long id,
            @RequestBody BigDecimal amount) {
        return accountService.deposit(id, amount);
    }

    @PostMapping("/accounts/{id}/withdraw")
    public Account withdraw(
            @PathVariable Long id,
            @RequestBody BigDecimal amount) {
        return accountService.withdraw(id, amount);
    }
}
