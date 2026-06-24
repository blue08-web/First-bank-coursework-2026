package database;

import java.sql.*;
import java.time.LocalDate;
import model.Account;
import model.JointAccount;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:ucanaccess://bank.accdb";

    static {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Generate account number BRANCHCODE-YYYY-xxxxxx
    public static String generateAccountNumber(String branchName) throws SQLException {
        String branchCode = getBranchCode(branchName);
        int year = java.time.Year.now().getValue();
        String prefix = branchCode + "-" + year + "-";

        String sql = "SELECT MAX(AccountNumber) FROM Accounts WHERE AccountNumber LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();
            int nextSeq = 1;
            if (rs.next()) {
                String maxNum = rs.getString(1);
                if (maxNum != null) {
                    // Extract last 6 digits
                    String seqStr = maxNum.substring(maxNum.length() - 6);
                    nextSeq = Integer.parseInt(seqStr) + 1;
                }
            }
            return prefix + String.format("%06d", nextSeq);
        }
    }

    public static void insertRecord(Account account) throws SQLException {
        String sql = "INSERT INTO Accounts (AccountNumber, FirstName, LastName, NIN, Email, Phone, PIN, DOB, AccountType, Branch, OpeningDeposit, SecondNIN) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getAccountNumber());
            ps.setString(2, account.getFirstName());
            ps.setString(3, account.getLastName());
            ps.setString(4, account.getNin());
            ps.setString(5, account.getEmail());
            ps.setString(6, account.getPhone());
            ps.setString(7, account.getPin());
            ps.setDate(8, Date.valueOf(account.getDob()));
            ps.setString(9, account.getAccountTypeName());
            ps.setString(10, account.getBranch());
            ps.setDouble(11, account.getOpeningDeposit());
            if (account instanceof JointAccount) {
                ps.setString(12, ((JointAccount) account).getSecondNin());
            } else {
                ps.setNull(12, Types.VARCHAR);
            }
            ps.executeUpdate();
        }
    }

    private static String getBranchCode(String branch) {
        switch (branch) {
            case "Kampala": return "KLA";
            case "Gulu": return "GUL";
            case "Mbarara": return "MBR";
            case "Jinja": return "JIN";
            case "Mbale": return "MBA";
            default: throw new IllegalArgumentException("Invalid branch: " + branch);
        }
    }
}