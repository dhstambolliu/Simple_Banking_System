package banking;

import java.sql.*;

public class Connect {

    static Connection connect() {
        Connection conn = null;
        String url = "jdbc:sqlite:card.s3db";
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    static void createTable() {
        String addAccount = "CREATE TABLE IF NOT EXISTS card ("
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "	number TEXT,"
                + "	pin TEXT,"
                + " balance INTEGER DEFAULT 0"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(addAccount);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static boolean isThereAClient(String recipientCardNumber) {
        String findID = "SELECT id FROM card WHERE number = '" + recipientCardNumber + "';";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(findID)){

            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    static boolean isThereACardNumber(String card, String PIN){
        String findCard = "SELECT id FROM card WHERE number = '" + card + "' AND pin = '" + PIN + "';";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(findCard)){

            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    static void decreaseBalance(int amount, String cardNum) {
        String updateAccount = "UPDATE card SET balance = balance - " + amount + " WHERE number = '" + cardNum + "'";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(updateAccount);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static void increaseBalance(int amount, String cardNum) {
        String updateAccount = "UPDATE card SET balance = balance + " + amount + " WHERE number = '" + cardNum + "'";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(updateAccount);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static int getBalance(String cardNum) {
        String checkMoney = "SELECT balance FROM card WHERE number = '" + cardNum + "';";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(checkMoney)){
            while (rs.next()) {
                return rs.getInt("balance");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    static void insert(String cardNum, String pin) {
        String sql = "INSERT INTO card(number,pin,balance) VALUES(?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNum);
            pstmt.setString(2, pin);
            pstmt.setInt(3, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static void removeAccount(String cardNum) {
        String deletingAccount = "DELETE FROM card WHERE number = '" + cardNum + "';";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(deletingAccount);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}