package com.carbonit.domain;

public enum OperationType {
    ACCOUNT_CREATION, DEPOSIT, WITHDRAWAL;

    @Override
    public String toString() {
        switch (this) {
            case ACCOUNT_CREATION:
                return "Account creation";
            case DEPOSIT:
                return "Deposit";
            case WITHDRAWAL:
                return "Withdrawal";
            default:
                return "Error";
        }
    }
}
