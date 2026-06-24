package model;
import java.time.LocalDate;

public class CurrentAccount extends Account {
    public CurrentAccount(String firstName, String lastName, String nin, String email,
                          String phone, String pin, LocalDate dob, String branch,
                          double openingDeposit, String accountNumber) {
        super(firstName, lastName, nin, email, phone, pin, dob, branch, openingDeposit, accountNumber);
    }
    @Override
    public int minimumDeposit() { return 200000; }
    @Override
    public String getAccountTypeName() { return "Current"; }
}