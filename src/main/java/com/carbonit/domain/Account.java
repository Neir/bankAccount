package com.carbonit.domain;

import com.carbonit.time.IClock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.io.PrintStream;

public class Account {

    private final ArrayList<Operation> history = new ArrayList<>();
    private final IClock clock;

    public Account(IClock clock) {
        this.clock = clock;
        history.add(new Operation(OperationType.ACCOUNT_CREATION, clock.now()));
    }

    public Account(BigDecimal balance, IClock clock) {
        this.clock = clock;
        history.add(new Operation(OperationType.ACCOUNT_CREATION, clock.now(), balance, balance));
    }

    public Operation deposit(BigDecimal amount) {
        BigDecimal balance = getBalance().add(amount);
        Operation operation = new Operation(OperationType.DEPOSIT, clock.now(), amount, balance);

        history.add(operation);
        return operation;
    }

    public Operation withdrawal(BigDecimal amount) throws WithdrawalImpossibleException {
        if(getBalance().compareTo(amount) == -1) {
            throw new WithdrawalImpossibleException();
        }
        Operation operation = new Operation(OperationType.WITHDRAWAL, clock.now(), amount, getBalance().subtract(amount));
        history.add(operation);
        return operation;
    }

    public Operation empty() {
        Operation operation = new Operation(OperationType.WITHDRAWAL, clock.now(), getBalance(), new BigDecimal(0));
        history.add(operation);
        return operation;
    }

    Operation lastOperation() {
        return history.get(history.size() - 1);
    }

    public void printHistory(PrintStream printer) {
        StringBuilder historyPrinted = new StringBuilder();
        for(Operation operation : history) {
            historyPrinted.append(operation.toString()).append("\n");
        }
        printer.println(historyPrinted.toString());
    }

    private BigDecimal getBalance() {
        return lastOperation().getBalanceResult();
    }
}
