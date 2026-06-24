package model;

import java.time.LocalDate;

public abstract class Account {
    protected String firstName;
    protected String lastName;
    protected String nin;
    protected String email;
    protected String phone;
    protected String pin;
    protected LocalDate dob;
    protected String branch;
    protected double openingDeposit;
    protected String accountNumber;

    public Account(String firstName, String lastName, String nin, String email,
                   String phone, String pin, LocalDate dob, String branch,
                   double openingDeposit, String accountNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nin = nin;
        this.email = email;
        this.phone = phone;
        this.pin = pin;
        this.dob = dob;
        this.branch = branch;
        this.openingDeposit = openingDeposit;
        this.accountNumber = accountNumber;
    }

    public abstract int minimumDeposit();
    public abstract String getAccountTypeName();

    // Polymorphic extra validation (return list of errors)
    public java.util.List<String> validateExtra() {
        return new java.util.ArrayList<>();
    }

    // Getters for database insertion
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getNin() { return nin; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPin() { return pin; }
    public LocalDate getDob() { return dob; }
    public String getBranch() { return branch; }
    public double getOpeningDeposit() { return openingDeposit; }
    public String getAccountNumber() { return accountNumber; }
}