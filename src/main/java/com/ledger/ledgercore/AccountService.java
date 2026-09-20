package com.ledger.ledgercore;

import com.ledger.ledgercore.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository; //constructor dependency injection
    }

    public Account createAccount(BigDecimal initialBalance) {
        Account account = new Account(initialBalance);
        return accountRepository.save(account); //persist the account; and postgreSQL will generate id
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Account deposit(Long id, BigDecimal amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        account.deposit(amount);
        return accountRepository.save(account);
    }
}
