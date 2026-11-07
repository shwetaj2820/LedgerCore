package com.ledger.ledgercore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.ledger.ledgercore.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void createAccount() {
        Account account = accountService.createAccount(
                new BigDecimal("500.00")
        );

        Account savedAccount = accountRepository
                .findById(account.getId())
                .orElseThrow();

        assertNotNull(savedAccount.getId());
        assertEquals(
                new BigDecimal("500.00"),
                savedAccount.getBalance()
        );
    }

    @Test
    void zeroBalanceAccount() {
        Account account = accountService.createAccount(
                BigDecimal.ZERO
        );

        Account savedAccount = accountRepository
                .findById(account.getId())
                .orElseThrow();

        assertNotNull(savedAccount.getId());
        assertEquals(
                0,
                savedAccount.getBalance().compareTo(BigDecimal.ZERO)
        );
    }

    @Test
    void negativeBalanceAccount() {
        long accountsBefore = accountRepository.count();
        assertThrows(InvalidAmountException.class, () ->
                accountService.createAccount(
                        new BigDecimal("-500.00")
                )
        );
        long accountsAfter = accountRepository.count();
        assertEquals(accountsBefore, accountsAfter);
    }

    @Test
    void getExistingAccount() {
        Account account = accountService.createAccount(
                new BigDecimal("500.00")
        );

        Account retrievedAccount = accountService.getAccount(account.getId());

        assertEquals(account.getId(), retrievedAccount.getId());
        assertEquals(
                new BigDecimal("500.00"),
                retrievedAccount.getBalance()
        );
    }

    @Test
    void getNonExistingAccount() {
        Long invalidId = -1L;

        assertThrows(AccountNotFoundException.class, () ->
                accountService.getAccount(invalidId)
        );
    }
}
