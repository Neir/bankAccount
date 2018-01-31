package com.carbonit.main;

import com.carbonit.domain.WithdrawalImpossibleException;
import com.carbonit.time.Clock;
import com.carbonit.domain.Account;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Account account = new Account(new Clock());
        String amount;

        while (true) {
            System.out.println("Veuillez entrer une commande : ");
            System.out.println("1. Dépot");
            System.out.println("2. Retrait");
            System.out.println("3. Solder son compte");
            System.out.println("4. Consultation historique");
            System.out.println("5. Fin");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    System.out.print("Combien voulez-vous déposer : ");
                    amount = scanner.nextLine();
                    try {
                        System.out.println(account.deposit(parseToBigDecimal(amount)));
                    } catch (ParseException e) {
                        System.out.println("Dépôt impossible : veuillez entrer un nombre à virgule correct. (ex : 120,18)");
                    }
                    break;
                case "2":
                    System.out.print("Combien voulez-vous retirer : ");
                    amount = scanner.nextLine();
                    try {
                        System.out.println(account.withdrawal(parseToBigDecimal(amount)));
                    } catch (ParseException e) {
                        System.out.println("Retrait impossible : Veuillez entrer un nombre à virgule correct. (ex : 120,18)");
                    } catch (WithdrawalImpossibleException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "3":
                    System.out.println(account.empty());
                    break;
                case "4":
                    account.printHistory(System.out);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Je n'ai pas compris votre demande.");
            }
        }
    }

    private static BigDecimal parseToBigDecimal(String text) throws ParseException {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        String pattern = "#,#";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        return (BigDecimal) decimalFormat.parse(text);
    }
}
