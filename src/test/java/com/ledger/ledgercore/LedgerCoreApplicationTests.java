package com.ledger.ledgercore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.ledger.ledgercore.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ledger.ledgercore.Account;
import com.ledger.ledgercore.Transaction;

@SpringBootTest
class LedgerCoreApplicationTests {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @MockitoBean
    TransactionRepository transactionRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void depositRollback() {

        Account account = accountService.createAccount(
                new BigDecimal("500.00")
        );

        doThrow(new RuntimeException("Simulated failure"))
                .when(transactionRepository)
                .save(any(Transaction.class));

        assertThrows(RuntimeException.class, () ->
                accountService.deposit(
                        account.getId(),
                        new BigDecimal("100.00")
                )
        );

        Account updatedAccount = accountRepository
                .findById(account.getId())
                .orElseThrow();

        assertEquals(
                new BigDecimal("500.00"),
                updatedAccount.getBalance()
        );
    }

    @Test
    void withdrawRollback() {
        Account account = accountService.createAccount(
                new BigDecimal("500.00")
        );

        doThrow(new RuntimeException("Simulated failure"))
        .when(transactionRepository)
                .save(any(Transaction.class));

        assertThrows(RuntimeException.class, () ->
                accountService.withdraw(
                        account.getId(),
                        new BigDecimal("100.00")
                )
        );

        Account updatedAccount = accountRepository
                .findById(account.getId())
                .orElseThrow();

        assertEquals(
                new BigDecimal("500.00"),
                updatedAccount.getBalance()
        );
    }
}
