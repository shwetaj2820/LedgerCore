package com.ledger.ledgercore;

import com.ledger.ledgercore.exception.AccountNotFoundException;
import jakarta.transaction.Transactional;
import java.time.Instant;
import org.springframework.stereotype.Service;
import com.ledger.ledgercore.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    public AccountService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository; //constructor dependency injection
        this.transactionRepository = transactionRepository;
    }

    public Account createAccount(BigDecimal initialBalance) {

        if(initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException();
        }

        Account account = new Account(initialBalance);
        return accountRepository.save(account); //persist the account; and postgreSQL will generate id
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Transactional
    public Account deposit(Long id, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.scale() > 2) {
            throw new InvalidAmountException();
        }

        Account account = accountRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        account.deposit(amount);

        Transaction transaction = new Transaction(
                account.getId(),
                TransactionType.DEPOSIT,
                amount,
                Instant.now()
        );

        transactionRepository.save(transaction);

        return accountRepository.save(account);
    }

    @Transactional
    public Account withdraw(Long id, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.scale() > 2) {
            throw new InvalidAmountException();
        }

        Account account = accountRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        account.withdraw(amount);

        Transaction transaction = new Transaction(
                account.getId(),
                TransactionType.WITHDRAWAL,
                amount,
                Instant.now()
        );

        transactionRepository.save(transaction);

        return accountRepository.save(account);
    }
}
