package model;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class StudentAccount extends Account {
    public StudentAccount(String firstName, String lastName, String nin, String email,
                          String phone, String pin, LocalDate dob, String branch,
                          double openingDeposit, String accountNumber) {
        super(firstName, lastName, nin, email, phone, pin, dob, branch, openingDeposit, accountNumber);
    }
    @Override
    public int minimumDeposit() { return 10000; }
    @Override
    public String getAccountTypeName() { return "Student"; }

    @Override
    public List<String> validateExtra() {
        List<String> errors = new ArrayList<>();
        int age = Period.between(dob, LocalDate.now()).getYears();
        if (age < 18 || age > 25) {
            errors.add("Student account requires age between 18 and 25. Current age: " + age);
        }
        return errors;
    }
}