package gui;

import model.*;
import database.DatabaseHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class AccountForm extends JFrame {
    // Fields
    private JTextField txtFirstName, txtLastName, txtNIN, txtEmail, txtConfirmEmail;
    private JTextField txtPhone, txtPIN, txtConfirmPIN, txtOpeningDeposit;
    private JTextField txtSecondNIN;
    private JComboBox<Integer> cmbYear, cmbMonth, cmbDay;
    private JComboBox<String> cmbAccountType, cmbBranch;
    private JTextArea txtSummary;
    private JButton btnSubmit, btnReset;

    // Error labels
    private JLabel lblFirstNameError, lblLastNameError, lblNINError, lblEmailError;
    private JLabel lblConfirmEmailError, lblPhoneError, lblPINError, lblConfirmPINError;
    private JLabel lblDOBError, lblAccountTypeError, lblBranchError, lblDepositError;
    private JLabel lblSecondNINError;

    public AccountForm() {
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("First Bank Uganda - New Account Opening");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // --- Personal Details ---
        addSectionLabel(mainPanel, gbc, "Personal Details", row++);

        txtFirstName = addField(mainPanel, gbc, "First Name:", row++, lblFirstNameError = new JLabel());
        txtLastName = addField(mainPanel, gbc, "Last Name:", row++, lblLastNameError = new JLabel());
        txtNIN = addField(mainPanel, gbc, "National ID (NIN):", row++, lblNINError = new JLabel());

        // Date of Birth
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Date of Birth:"), gbc);
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        cmbYear = new JComboBox<>();
        for (int y = 1940; y <= LocalDate.now().getYear() - 18; y++) cmbYear.addItem(y);
        cmbMonth = new JComboBox<>();
        for (int m = 1; m <= 12; m++) cmbMonth.addItem(m);
        cmbDay = new JComboBox<>();
        updateDays();
        cmbYear.addActionListener(e -> updateDays());
        cmbMonth.addActionListener(e -> updateDays());
        dobPanel.add(cmbYear);
        dobPanel.add(new JLabel("-"));
        dobPanel.add(cmbMonth);
        dobPanel.add(new JLabel("-"));
        dobPanel.add(cmbDay);
        gbc.gridx = 1; gbc.gridwidth = 3;
        mainPanel.add(dobPanel, gbc);
        gbc.gridx = 4; gbc.gridwidth = 1;
        lblDOBError = new JLabel();
        lblDOBError.setForeground(Color.RED);
        mainPanel.add(lblDOBError, gbc);
        row++;

        // --- Contact Details ---
        addSectionLabel(mainPanel, gbc, "Contact Details", row++);

        txtEmail = addField(mainPanel, gbc, "Email:", row++, lblEmailError = new JLabel());
        txtConfirmEmail = addField(mainPanel, gbc, "Confirm Email:", row++, lblConfirmEmailError = new JLabel());
        txtPhone = addField(mainPanel, gbc, "Phone (+256XXXXXXXXX):", row++, lblPhoneError = new JLabel());

        // --- Security ---
        addSectionLabel(mainPanel, gbc, "Security", row++);

        txtPIN = addPasswordField(mainPanel, gbc, "PIN:", row++, lblPINError = new JLabel());
        txtConfirmPIN = addPasswordField(mainPanel, gbc, "Confirm PIN:", row++, lblConfirmPINError = new JLabel());

        // --- Account Selection ---
        addSectionLabel(mainPanel, gbc, "Account Selection", row++);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Account Type:"), gbc);
        cmbAccountType = new JComboBox<>(new String[]{"--Select--", "Savings", "Current", "Fixed Deposit", "Student", "Joint"});
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(cmbAccountType, gbc);
        gbc.gridx = 3; gbc.gridwidth = 1;
        lblAccountTypeError = new JLabel();
        lblAccountTypeError.setForeground(Color.RED);
        mainPanel.add(lblAccountTypeError, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Branch:"), gbc);
        cmbBranch = new JComboBox<>(new String[]{"--Select--", "Kampala", "Gulu", "Mbarara", "Jinja", "Mbale"});
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(cmbBranch, gbc);
        gbc.gridx = 3;
        lblBranchError = new JLabel();
        lblBranchError.setForeground(Color.RED);
        mainPanel.add(lblBranchError, gbc);
        row++;

        // Joint second NIN (initially hidden)
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        JLabel lblSecondNIN = new JLabel("Second NIN (Joint):");
        lblSecondNIN.setVisible(false);
        mainPanel.add(lblSecondNIN, gbc);
        txtSecondNIN = new JTextField(20);
        txtSecondNIN.setVisible(false);
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(txtSecondNIN, gbc);
        gbc.gridx = 3; gbc.gridwidth = 1;
        lblSecondNINError = new JLabel();
        lblSecondNINError.setForeground(Color.RED);
        lblSecondNINError.setVisible(false);
        mainPanel.add(lblSecondNINError, gbc);
        row++;

        // Opening Deposit
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Opening Deposit (UGX):"), gbc);
        txtOpeningDeposit = new JTextField(20);
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(txtOpeningDeposit, gbc);
        gbc.gridx = 3;
        lblDepositError = new JLabel();
        lblDepositError.setForeground(Color.RED);
        mainPanel.add(lblDepositError, gbc);
        row++;

        // Show/hide second NIN when Joint is selected
        cmbAccountType.addActionListener(e -> {
            boolean isJoint = "Joint".equals(cmbAccountType.getSelectedItem());
            lblSecondNIN.setVisible(isJoint);
            txtSecondNIN.setVisible(isJoint);
            lblSecondNINError.setVisible(isJoint);
            if (!isJoint) txtSecondNIN.setText("");
        });

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSubmit = new JButton("Submit");
        btnReset = new JButton("Reset");
        btnPanel.add(btnSubmit);
        btnPanel.add(btnReset);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4;
        mainPanel.add(btnPanel, gbc);
        row++;

        // Summary area
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4;
        mainPanel.add(new JLabel("Account Summary is Below:"), gbc);
        row++;
        txtSummary = new JTextArea(8, 50);
        txtSummary.setEditable(false);
        txtSummary.setLineWrap(true);
        txtSummary.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(txtSummary);
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(scroll, gbc);

        add(mainPanel);

        // Action listeners
        btnSubmit.addActionListener(e -> handleSubmit());
        btnReset.addActionListener(e -> resetForm());
    }

    private void updateDays() {
        if (cmbYear.getSelectedItem() == null || cmbMonth.getSelectedItem() == null) return;
        int year = (int) cmbYear.getSelectedItem();
        int month = (int) cmbMonth.getSelectedItem();
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        cmbDay.removeAllItems();
        for (int d = 1; d <= daysInMonth; d++) cmbDay.addItem(d);
    }

    private void addSectionLabel(JPanel panel, GridBagConstraints gbc, String text, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4;
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label, gbc);
    }

    private JTextField addField(JPanel panel, GridBagConstraints gbc, String label, int row, JLabel errorLabel) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        JTextField field = new JTextField(20);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(field, gbc);
        gbc.gridx = 3; gbc.gridwidth = 1;
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel, gbc);
        return field;
    }

    private JTextField addPasswordField(JPanel panel, GridBagConstraints gbc, String label, int row, JLabel errorLabel) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        JPasswordField field = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(field, gbc);
        gbc.gridx = 3; gbc.gridwidth = 1;
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel, gbc);
        // Hack: store as JTextField for unified getText()
        return field;
    }

    private void handleSubmit() {
        clearErrors();
        List<String> errors = new ArrayList<>();

        // Trim all text fields
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String nin = txtNIN.getText().trim().toUpperCase();
        String email = txtEmail.getText().trim();
        String confirmEmail = txtConfirmEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String pin = txtPIN.getText().trim();
        String confirmPin = txtConfirmPIN.getText().trim();
        String depositStr = txtOpeningDeposit.getText().trim();
        String secondNin = txtSecondNIN.getText().trim().toUpperCase();

        // Validate names
        if (!firstName.matches("[a-zA-Z]{2,30}"))
            setError(lblFirstNameError, "First name: letters only, 2-30 chars.");
        if (!lastName.matches("[a-zA-Z]{2,30}"))
            setError(lblLastNameError, "Last name: letters only, 2-30 chars.");

        // NIN
        if (!nin.matches("[A-Z0-9]{14}"))
            setError(lblNINError, "NIN: exactly 14 uppercase alphanumeric.");

        // Email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            setError(lblEmailError, "Invalid email format.");
        if (!email.equals(confirmEmail))
            setError(lblConfirmEmailError, "Emails do not match.");

        // Phone
        if (!phone.matches("^\\+256\\d{12}$")) // adjust if 9 digits
            setError(lblPhoneError, "Phone must be +256XXXXXXXXX (12 digits after +256).");

        // PIN
        if (!pin.matches("\\d{4,6}"))
            setError(lblPINError, "PIN: 4-6 digits.");
        else if (pin.chars().distinct().count() == 1)
            setError(lblPINError, "PIN cannot be all identical digits.");
        if (!pin.equals(confirmPin))
            setError(lblConfirmPINError, "PINs do not match.");

        // Date of Birth & Age
        LocalDate dob = null;
        if (cmbYear.getSelectedItem() != null && cmbMonth.getSelectedItem() != null && cmbDay.getSelectedItem() != null) {
            int y = (int) cmbYear.getSelectedItem();
            int m = (int) cmbMonth.getSelectedItem();
            int d = (int) cmbDay.getSelectedItem();
            dob = LocalDate.of(y, m, d);
            int age = Period.between(dob, LocalDate.now()).getYears();
            if (age < 18 || age > 75)
                setError(lblDOBError, "Age must be 18-75 (current: " + age + ").");
        } else {
            setError(lblDOBError, "Date of Birth required.");
        }

        // Account Type
        String accType = (String) cmbAccountType.getSelectedItem();
        if (accType == null || "--Select--".equals(accType))
            setError(lblAccountTypeError, "Select an account type.");

        // Branch
        String branch = (String) cmbBranch.getSelectedItem();
        if (branch == null || "--Select--".equals(branch))
            setError(lblBranchError, "Select a branch.");

        // Opening Deposit
        double deposit = 0;
        try {
            deposit = Double.parseDouble(depositStr);
            if (deposit < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            setError(lblDepositError, "Deposit must be a valid positive number.");
        }

        // If basic fields valid, check account-type specific
        if (accType != null && !"--Select--".equals(accType) && dob != null) {
            Account account;
            // Temporary account object for validation
            switch (accType) {
                case "Savings":
                    account = new SavingsAccount(firstName, lastName, nin, email, phone, pin, dob, branch, deposit, null);
                    break;
                case "Current":
                    account = new CurrentAccount(firstName, lastName, nin, email, phone, pin, dob, branch, deposit, null);
                    break;
                case "Fixed Deposit":
                    account = new FixedDepositAccount(firstName, lastName, nin, email, phone, pin, dob, branch, deposit, null);
                    break;
                case "Student":
                    account = new StudentAccount(firstName, lastName, nin, email, phone, pin, dob, branch, deposit, null);
                    break;
                case "Joint":
                    account = new JointAccount(firstName, lastName, nin, email, phone, pin, dob, branch, deposit, null, secondNin);
                    break;
                default:
                    account = null;
            }
            if (account != null) {
                // Minimum deposit
                if (deposit < account.minimumDeposit())
                    setError(lblDepositError, "Minimum deposit: " + account.minimumDeposit() + " UGX.");
                // Extra polymorphic validation
                List<String> extra = account.validateExtra();
                for (String e : extra) {
                    if (e.contains("Second NIN")) setError(lblSecondNINError, e);
                    else if (e.contains("age")) setError(lblDOBError, e);
                }
            }
        }

        // Gather all error messages
        for (Component comp : getErrorLabels()) {
            JLabel lbl = (JLabel) comp;
            if (!lbl.getText().isEmpty()) errors.add(lbl.getText());
        }

        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    String.join("\n", errors),
                    "Validation Errors",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // All valid – generate account number and insert
        try {
            String accountNumber = DatabaseHelper.generateAccountNumber(branch);
            Account finalAccount;
            switch (accType) {
                case "Savings":
                    finalAccount = new SavingsAccount(firstName, lastName, nin, email, phone, pin, dob, branch, deposit, accountNumber);
                    break;
                case "Current":
                    finalAccount = new CurrentAccount(firstName, lastName, nin, email, phone, pin, dob, branch, deposit, accountNumber);
                    break;
                case "Fixed Deposit":
                    finalAccount = new FixedDepositAccount(firstName, lastName, nin, email, phone, pin, dob, branch, deposit, accountNumber);
                    break;
                case "Student":
                    finalAccount = new StudentAccount(firstName, lastName, nin, email, phone, pin, dob, branch, deposit, accountNumber);
                    break;
                case "Joint":
                    finalAccount = new JointAccount(firstName, lastName, nin, email, phone, pin, dob, branch, deposit, accountNumber, secondNin);
                    break;
                default:
                    throw new IllegalStateException("Unexpected type");
            }
            DatabaseHelper.insertRecord(finalAccount);

            // Display summary
            String summary = String.format(
                "ACC: %s | %s %s | %s | %s | DOB %s | %s | Deposit %,.0f | %s",
                accountNumber, firstName, lastName, accType, branch,
                dob, phone, deposit, email
            );
            txtSummary.setText(summary);
            JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setError(JLabel label, String msg) {
        label.setText(msg);
    }

    private void clearErrors() {
        for (Component comp : getErrorLabels()) {
            ((JLabel) comp).setText("");
        }
    }

    private Component[] getErrorLabels() {
        return new Component[]{lblFirstNameError, lblLastNameError, lblNINError,
                lblEmailError, lblConfirmEmailError, lblPhoneError, lblPINError,
                lblConfirmPINError, lblDOBError, lblAccountTypeError, lblBranchError,
                lblDepositError, lblSecondNINError};
    }

    private void resetForm() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtNIN.setText("");
        txtEmail.setText("");
        txtConfirmEmail.setText("");
        txtPhone.setText("");
        txtPIN.setText("");
        txtConfirmPIN.setText("");
        txtOpeningDeposit.setText("");
        txtSecondNIN.setText("");
        cmbAccountType.setSelectedIndex(0);
        cmbBranch.setSelectedIndex(0);
        cmbYear.setSelectedIndex(0);
        cmbMonth.setSelectedIndex(0);
        updateDays();
        txtSummary.setText("");
        clearErrors();
    }
}