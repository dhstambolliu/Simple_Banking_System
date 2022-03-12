package banking;

import java.util.*;

public class Bank {
    static Scanner sc = new Scanner(System.in);

    static void chooseAct() {

        System.out.println();
        System.out.println("1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");
        int action = sc.nextInt();
        switch (action) {
            case 0:
                System.out.println();
                System.out.println("Bye!");
                break;
            case 1:
                createCard();
                chooseAct();
                break;
            case 2:
                logIntoAccount();
                break;
            default:
                System.out.println("Invalid input provided.");
                break;
        }
        System.out.println();
    }

    static void createCard() {
        Random rnd = new Random();
        String cardNum = String.format((Locale)null,"400000%02d%04d%04d",
                rnd.nextInt(10),
                rnd.nextInt(10000),
                rnd.nextInt(10000));

        String pin = String.format((Locale)null, "%04d",
                rnd.nextInt(10000));

        if (checkLuhn(cardNum)) {
            System.out.println();
            Connect.createTable();
            Connect.insert(cardNum, pin);
            System.out.println("Your card has been created");
            System.out.println("Your card number:" + "\n" + cardNum);
            System.out.println("Your card PIN:" + "\n" + pin);
        } else {
            createCard();
        }
    }

    static boolean checkLuhn(String cardNum) {
        int nDigits = cardNum.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {
            int j = cardNum.charAt(i) - '0';
            if (isSecond) {
                j = j * 2;
            }

            nSum += j / 10;
            nSum += j % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    static void logIntoAccount() {
        System.out.println();
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your card number:");
        String checkCardNum = sc.nextLine();

        System.out.println("Enter your PIN:");
        String checkPin = sc.nextLine();

        if (!Connect.isThereACardNumber(String.valueOf(checkCardNum), String.valueOf(checkPin))) {
            System.out.println("Wrong card number or PIN!");
            chooseAct();
        } else {
            System.out.println();
            System.out.println("You have successfully logged in!");
            logIn(String.valueOf(checkCardNum));
        }
    }

    static void addIncome(String cardNum) {
        System.out.println();
        System.out.println("Enter income:");
        Scanner sc = new Scanner(System.in);
        int amount  = sc.nextInt();
        Connect.increaseBalance(amount, cardNum);
        System.out.println("Income was added!");
    }

    static void sendMoney(String cardNum) {
        System.out.println();
        System.out.print("Transfer\n" +
                "Enter card number:\n");
        Scanner sc = new Scanner(System.in);

        String recipientCardNumber = sc.next();
        if (!checkLuhn(recipientCardNumber)) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return;
        } else if (!Connect.isThereAClient(recipientCardNumber)) {
            System.out.println("Such a card does not exist.");
            return;
        } else if (recipientCardNumber.equals(cardNum)) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        int amount = sc.nextInt();
        if (Connect.getBalance(cardNum) > amount) {
            Connect.decreaseBalance(amount, cardNum);
            Connect.increaseBalance(amount, recipientCardNumber);
            System.out.println("Success!");
        } else {
            System.out.println("Not enough money!");
        }
    }

    static void logIn(String cardNum) {
        System.out.println();
        System.out.print("1. Balance\n" +
                "2. Add income\n" +
                "3. Do Transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit\n");
        int action = sc.nextInt();
        switch (action) {
            case 1:
                System.out.println();
                System.out.print("Balance: " + Connect.getBalance(cardNum) + "\n");
                logIn(cardNum);
                break;
            case 2:
                addIncome(cardNum);
                logIn(cardNum);
                break;
            case 3:
                sendMoney(cardNum);
                logIn(cardNum);
                break;
            case 4:
                closeAccount(cardNum);
                chooseAct();
                break;
            case 5:
                System.out.println("You have successfully logged out!");
                chooseAct();
                break;
            case 0:
                System.out.println("Bye!");
                System.exit(0);
            default:
                System.out.println("Invalid input provided.");
                break;
        }
    }

    static void closeAccount(String cardNum) {
        Connect.removeAccount(cardNum);
        System.out.println("The account has been closed!");
    }
}