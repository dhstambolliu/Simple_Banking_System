package banking;

public class Main {
    public static void main(String[] args) {
        Connect.connect();
        Connect.createTable();
        Bank.chooseAct();
    }
}