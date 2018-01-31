package com.carbonit.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Operation {
    private final OperationType type;
    private final Date date;
    private final BigDecimal amount;
    private final BigDecimal balanceResult;

    public Operation(OperationType type, Date date) {
        this.type = type;
        this.date = date;
        this.amount = new BigDecimal(0);
        this.balanceResult = new BigDecimal(0);
    }

    public Operation(OperationType type, Date date, BigDecimal amount, BigDecimal balanceResult) {
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.balanceResult = balanceResult;
    }

    public BigDecimal getBalanceResult() {
        return balanceResult;
    }

    @Override
    public String toString() {
        return type.toString() + " : " + amount + " => solde : " + balanceResult + " (" + date + ")";
    }
}
