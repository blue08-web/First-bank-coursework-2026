package model;
import java.time.LocalDate;

public class FixedDepositAccount extends Account {
    public FixedDepositAccount(String firstName, String lastName, String nin, String email,
                               String phone, String pin, LocalDate dob, String branch,
                               double openingDeposit, String accountNumber) {
        super(firstName, lastName, nin, email, phone, pin, dob, branch, openingDeposit, accountNumber);
    }
    @Override
    public int minimumDeposit() { return 1000000; }
    @Override
    public String getAccountTypeName() { return "Fixed Deposit"; }
}