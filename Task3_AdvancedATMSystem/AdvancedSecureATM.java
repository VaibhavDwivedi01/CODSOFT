import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class ATMUser {
    private String name;
    private String accountNumber;
    private String pin;
    private String mobile;
    private double balance;

    public ATMUser(String name, String accountNumber, String pin, String mobile, double balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.mobile = mobile;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public boolean verifyCredentials(String accNo, String pin) {
        return this.accountNumber.equals(accNo) && this.pin.equals(pin);
    }

    public void changePin(String newPin) {
        this.pin = newPin;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

public class AdvancedSecureATM extends JFrame {
    private java.util.List<ATMUser> users = new ArrayList<>();
    private ATMUser loggedInUser = null;

    public AdvancedSecureATM() {
        setTitle("Advanced ATM System");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        showMainMenu();
    }

    private void showMainMenu() {
        getContentPane().removeAll();
        setLayout(new GridLayout(6, 1, 10, 10));

        JLabel title = new JLabel("Welcome to Secure ATM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton registerBtn = new JButton("Register New User");
        JButton loginBtn = new JButton("Login to Account");
        JButton adminBtn = new JButton("Admin Panel (View Users)");
        JButton exitBtn = new JButton("Exit");

        add(title);
        add(registerBtn);
        add(loginBtn);
        add(adminBtn);
        add(exitBtn);

        registerBtn.addActionListener(e -> registerUser());
        loginBtn.addActionListener(e -> loginUser());
        adminBtn.addActionListener(e -> showAdminPanel());
        exitBtn.addActionListener(e -> System.exit(0));

        revalidate();
        repaint();
    }

    private void registerUser() {
        JTextField nameField = new JTextField();
        JTextField accNoField = new JTextField();
        JPasswordField pinField = new JPasswordField();
        JTextField mobileField = new JTextField();
        JTextField balanceField = new JTextField();

        Object[] fields = {
            "Name:", nameField,
            "Account Number:", accNoField,
            "4-digit PIN:", pinField,
            "Mobile Number:", mobileField,
            "Initial Balance:", balanceField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Register User", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String acc = accNoField.getText();
            String pin = new String(pinField.getPassword());
            String mobile = mobileField.getText();

            try {
                double balance = Double.parseDouble(balanceField.getText());

                if (pin.length() == 4 && !name.isEmpty() && !acc.isEmpty() && !mobile.isEmpty()) {
                    users.add(new ATMUser(name, acc, pin, mobile, balance));
                    JOptionPane.showMessageDialog(this, "User registered successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid details. Please enter all fields correctly.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Enter a valid number for balance.");
            }
        }
    }

    private void loginUser() {
        JTextField accField = new JTextField();
        JPasswordField pinField = new JPasswordField();

        Object[] inputs = {
            "Enter Account Number:", accField,
            "Enter PIN:", pinField
        };

        int opt = JOptionPane.showConfirmDialog(this, inputs, "Login", JOptionPane.OK_CANCEL_OPTION);

        if (opt == JOptionPane.OK_OPTION) {
            String acc = accField.getText();
            String pin = new String(pinField.getPassword());

            for (ATMUser user : users) {
                if (user.verifyCredentials(acc, pin)) {
                    loggedInUser = user;
                    JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + user.getName());
                    showUserMenu();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid account or PIN.");
        }
    }

    private void showUserMenu() {
        String[] options = {"Deposit", "Withdraw", "Check Balance", "Change PIN", "Logout"};
        while (true) {
            int choice = JOptionPane.showOptionDialog(this,
                    "Choose an action:",
                    "Account: " + loggedInUser.getAccountNumber(),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                String amtStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
                try {
                    double amt = Double.parseDouble(amtStr);
                    if (loggedInUser.deposit(amt)) {
                        JOptionPane.showMessageDialog(this, "Rs. " + amt + " deposited successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid amount.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Enter a valid number.");
                }
            } else if (choice == 1) {
                String amtStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
                try {
                    double amt = Double.parseDouble(amtStr);
                    if (loggedInUser.withdraw(amt)) {
                        JOptionPane.showMessageDialog(this, "Rs. " + amt + " withdrawn successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid amount or insufficient balance.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Enter a valid number.");
                }
            } else if (choice == 2) {
                JOptionPane.showMessageDialog(this, "Current Balance: Rs. " + loggedInUser.getBalance());
            } else if (choice == 3) {
                String newPin = JOptionPane.showInputDialog(this, "Enter new 4-digit PIN:");
                if (newPin != null && newPin.length() == 4) {
                    loggedInUser.changePin(newPin);
                    JOptionPane.showMessageDialog(this, "PIN changed successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid PIN.");
                }
            } else {
                loggedInUser = null;
                showMainMenu();
                break;
            }
        }
    }

    private void showAdminPanel() {
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users registered yet.");
            return;
        }

        StringBuilder sb = new StringBuilder("Registered Users:\n\n");
        for (ATMUser user : users) {
            sb.append("Name: ").append(user.getName()).append("\n");
            sb.append("Account No: ").append(user.getAccountNumber()).append("\n");
            sb.append("Mobile: ").append(user.getMobile()).append("\n\n");
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(350, 250));

        JOptionPane.showMessageDialog(this, scroll, "Admin Panel", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdvancedSecureATM atm = new AdvancedSecureATM();
            atm.setVisible(true);
        });
    }
}
