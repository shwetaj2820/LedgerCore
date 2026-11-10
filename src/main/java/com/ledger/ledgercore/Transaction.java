package com.ledger.ledgercore;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;
    private Instant createdAt;

    protected Transaction() {
    }

    public Transaction(
            Account account,
            TransactionType type,
            BigDecimal amount,
            Instant createdAt
    ) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
