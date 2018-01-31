package com.carbonit.domain;

public class WithdrawalImpossibleException extends Exception {
    public WithdrawalImpossibleException() {
        super("Retrait impossible : solde insuffisant");
    }
}
