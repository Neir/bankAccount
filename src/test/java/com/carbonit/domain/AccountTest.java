package com.carbonit.domain;

import com.carbonit.time.IClock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountTest {

    private static final BigDecimal ZERO = new BigDecimal(0);
    private static final BigDecimal FIFTY = new BigDecimal(50);
    private static final BigDecimal HUNDRED = new BigDecimal(100);
    
    @Mock
    private PrintStream printer;

    @Mock
    private IClock clock;

    private Date today;

    @Before
    public void setup() {
        Calendar cal = Calendar.getInstance();
        cal.set(1991, Calendar.MARCH, 9, 12, 0, 0);
        today = cal.getTime();
        when(clock.now()).thenReturn(today);
    }

    @Test
    public void Account_without_balance_should_create_an_empty_account() throws Exception {
        Account account = new Account(clock);

        assertThat(new Operation(OperationType.ACCOUNT_CREATION, today, ZERO, ZERO))
                .isEqualToComparingFieldByField(account.lastOperation());
    }

    @Test
    public void Account_with_initial_balance_should_create_an_account_with_given_balance() throws Exception {
        Account account = new Account(HUNDRED, clock);

        assertThat(new Operation(OperationType.ACCOUNT_CREATION, today, HUNDRED, HUNDRED))
                .isEqualToComparingFieldByField(account.lastOperation());
    }

    @Test
    public void deposit_should_increase_account_balance() throws Exception {
        Account account = new Account(ZERO, clock);

        Operation actualOperation = account.deposit(FIFTY);

        assertThat(new Operation(OperationType.DEPOSIT, today, FIFTY, FIFTY))
                .isEqualToComparingFieldByField(actualOperation);
    }

    @Test
    public void withdrawal_should_decrease_account_balance_if_balance_allows_it() throws Exception {
        Account account = new Account(HUNDRED, clock);

        Operation actualOperation = account.withdrawal(HUNDRED);

        assertThat(new Operation(OperationType.WITHDRAWAL, today, HUNDRED, ZERO))
                .isEqualToComparingFieldByField(actualOperation);
    }

    @Test
    public void withdrawal_should_not_decrease_account_balance_if_amount_is_upper_than_withdrawal() {
        Account account = new Account(FIFTY, clock);

        assertThatThrownBy(() -> account.withdrawal(HUNDRED)).isInstanceOf(WithdrawalImpossibleException.class)
                .hasMessageContaining("Retrait impossible : solde insuffisant");
    }

    @Test
    public void empty_should_set_balance_to_zero() throws Exception {
        Account account = new Account(HUNDRED, clock);

        Operation actualOperation = account.empty();

        assertThat(new Operation(OperationType.WITHDRAWAL, today, HUNDRED, ZERO))
                .isEqualToComparingFieldByField(actualOperation);
    }

    @Test
    public void printHistory_print_each_account_operation_done() throws Exception {
        Account account = new Account(clock);

        account.deposit(new BigDecimal(500));
        account.withdrawal(new BigDecimal(150));
        account.deposit(HUNDRED);
        account.empty();
        account.printHistory(printer);

        verify(printer).println(
                "Account creation : 0 => solde : 0 (Sat Mar 09 12:00:00 CET 1991)\n" +
                "Deposit : 500 => solde : 500 (Sat Mar 09 12:00:00 CET 1991)\n" +
                "Withdrawal : 150 => solde : 350 (Sat Mar 09 12:00:00 CET 1991)\n" +
                "Deposit : 100 => solde : 450 (Sat Mar 09 12:00:00 CET 1991)\n" +
                "Withdrawal : 450 => solde : 0 (Sat Mar 09 12:00:00 CET 1991)\n");
    }
}
