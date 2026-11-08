package com.ledger.ledgercore;

import com.ledger.ledgercore.exception.InvalidAmountException;
import com.ledger.ledgercore.exception.InsufficientBalanceException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private BigDecimal balance;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> getTransactions() {
        return transactions;
    }

    protected Account() {
    } //hibernate uses this constructor

    public Account(BigDecimal balance) {
        this.balance = balance;
    } //application uses this constructor

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {

        if(balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }

        this.balance = this.balance.subtract(amount);
    }

}
