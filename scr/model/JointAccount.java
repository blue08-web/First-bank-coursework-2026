package model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JointAccount extends Account {
    private String secondNin;

    public JointAccount(String firstName, String lastName, String nin, String email,
                        String phone, String pin, LocalDate dob, String branch,
                        double openingDeposit, String accountNumber, String secondNin) {
        super(firstName, lastName, nin, email, phone, pin, dob, branch, openingDeposit, accountNumber);
        this.secondNin = secondNin;
    }

    @Override
    public int minimumDeposit() { return 100000; }
    @Override
    public String getAccountTypeName() { return "Joint"; }

    public String getSecondNin() { return secondNin; }

    @Override
    public List<String> validateExtra() {
        List<String> errors = new ArrayList<>();
        if (secondNin == null || secondNin.trim().isEmpty()) {
            errors.add("Second NIN is required for Joint account.");
        } else if (!secondNin.matches("[A-Z0-9]{14}")) {
            errors.add("Second NIN must be exactly 14 uppercase alphanumeric characters.");
        }
        return errors;
    }
}