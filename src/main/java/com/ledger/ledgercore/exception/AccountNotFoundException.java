package com.ledger.ledgercore.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long id) {
        super("Could not find account with id " + id);
    }
}
