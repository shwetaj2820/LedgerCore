package com.ledger.ledgercore.exception;

public class InvalidAmountException extends RuntimeException {

        public InvalidAmountException() {
            super("Amount must be provided, greater than zero and have at most 2 decimal places");
        }
}
