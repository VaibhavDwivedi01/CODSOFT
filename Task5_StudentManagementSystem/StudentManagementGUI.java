import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StudentManagementGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private StudentManager manager = new StudentManager();

    private JTextField nameField, rollField, emailField, phoneField, gradeField;
    private JTextField dobField, classField, parentNameField, parentPhoneField, totalFeeField, paidFeeField;
    private JComboBox<String> genderBox;

    private DefaultTableModel tableModel;
    private JTable studentTable;

    private JTextField searchField, deleteRollField;
    private JTextArea searchResultArea;

    public StudentManagementGUI() {
        setTitle("Student Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createMainMenuPanel(), "menu");
        mainPanel.add(createAddStudentPanel(), "add");
        mainPanel.add(createViewStudentPanel(), "view");
        mainPanel.add(createSearchPanel(), "search");
        mainPanel.add(createDeletePanel(), "delete");
        mainPanel.add(createFeeSummaryPanel(), "feesummary");
        mainPanel.add(createUpdateStudentSearchPanel(), "updatesearch");

        add(mainPanel);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        null, "Are you sure you want to exit?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Welcome to Student Management System", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(heading, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        String[] labels = {
            "Add Student", "View Students", "Search Student",
            "Remove Student", "Fee Summary", "Update Student", "Exit"
        };
        for (String label : labels) {
            JButton btn = new JButton(label);
            buttonPanel.add(btn);
            btn.addActionListener(e -> {
                switch (label) {
                    case "Add Student" -> cardLayout.show(mainPanel, "add");
                    case "View Students" -> {
                        loadStudentTable();
                        cardLayout.show(mainPanel, "view");
                    }
                    case "Search Student" -> {
                        searchField.setText("");
                        searchResultArea.setText("");
                        cardLayout.show(mainPanel, "search");
                    }
                    case "Remove Student" -> {
                        deleteRollField.setText("");
                        cardLayout.show(mainPanel, "delete");
                    }
                    case "Fee Summary" -> showFeeSummary();
                    case "Update Student" -> cardLayout.show(mainPanel, "updatesearch");
                    case "Exit" -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                }
            });
        }

        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }
        private JPanel createAddStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Add New Student", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(13, 2, 10, 10));
        nameField = new JTextField();
        rollField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        dobField = new JTextField();
        classField = new JTextField();
        gradeField = new JTextField();
        parentNameField = new JTextField();
        parentPhoneField = new JTextField();
        totalFeeField = new JTextField();
        paidFeeField = new JTextField();

        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Roll No:")); form.add(rollField);
        form.add(new JLabel("Email:")); form.add(emailField);
        form.add(new JLabel("Phone:")); form.add(phoneField);
        form.add(new JLabel("Gender:")); form.add(genderBox);
        form.add(new JLabel("DOB:")); form.add(dobField);
        form.add(new JLabel("Class:")); form.add(classField);
        form.add(new JLabel("Grade:")); form.add(gradeField);
        form.add(new JLabel("Parent Name:")); form.add(parentNameField);
        form.add(new JLabel("Parent Phone:")); form.add(parentPhoneField);
        form.add(new JLabel("Total Fee:")); form.add(totalFeeField);
        form.add(new JLabel("Paid Fee:")); form.add(paidFeeField);

        JPanel btnPanel = new JPanel();
        JButton submit = new JButton("Submit");
        JButton back = new JButton("Back");

        submit.addActionListener(e -> addStudent());
        back.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        btnPanel.add(submit);
        btnPanel.add(back);

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void addStudent() {
        try {
            Student s = new Student(
                    nameField.getText(),
                    Integer.parseInt(rollField.getText()),
                    emailField.getText(),
                    phoneField.getText(),
                    genderBox.getSelectedItem().toString(),
                    dobField.getText(),
                    classField.getText(),
                    gradeField.getText().toUpperCase(),
                    parentNameField.getText(),
                    parentPhoneField.getText(),
                    Double.parseDouble(totalFeeField.getText()),
                    Double.parseDouble(paidFeeField.getText())
            );
            manager.addStudent(s);
            JOptionPane.showMessageDialog(this, "Student added successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check all fields.");
        }

        nameField.setText(""); rollField.setText(""); emailField.setText(""); phoneField.setText("");
        dobField.setText(""); classField.setText(""); gradeField.setText("");
        parentNameField.setText(""); parentPhoneField.setText("");
        totalFeeField.setText(""); paidFeeField.setText("");
    }
        private JPanel createViewStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("All Students", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(heading, BorderLayout.NORTH);

        String[] cols = {"Name", "Roll", "Email", "Phone", "Gender", "DOB", "Class", "Grade",
                         "Parent Name", "Parent Phone", "Total Fee", "Paid Fee", "Due", "Status"};
        tableModel = new DefaultTableModel(cols, 0);
        studentTable = new JTable(tableModel);
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    private void loadStudentTable() {
        tableModel.setRowCount(0);
        for (Student s : manager.getStudents()) {
            tableModel.addRow(s.toObjectArray());
        }
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Search Student", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(heading, BorderLayout.NORTH);

        JPanel input = new JPanel();
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        input.add(new JLabel("Enter Name or Roll No:"));
        input.add(searchField);
        input.add(searchBtn);

        searchResultArea = new JTextArea(10, 60);
        searchResultArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(searchResultArea);

        searchBtn.addActionListener(e -> {
            String key = searchField.getText();
            try {
                int roll = Integer.parseInt(key);
                Student s = manager.searchByRoll(roll);
                searchResultArea.setText(s != null ? s.toString() : "No student found.");
            } catch (NumberFormatException ex) {
                List<Student> list = manager.searchByName(key);
                if (list.isEmpty()) searchResultArea.setText("No student found.");
                else {
                    StringBuilder sb = new StringBuilder();
                    for (Student s : list) sb.append(s).append("\n");
                    searchResultArea.setText(sb.toString());
                }
            }
        });

        JButton back = new JButton("Back");
        back.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        panel.add(input, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createDeletePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Remove Student", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(heading, BorderLayout.NORTH);

        JPanel input = new JPanel();
        deleteRollField = new JTextField(10);
        JButton deleteBtn = new JButton("Remove");
        input.add(new JLabel("Enter Roll No:"));
        input.add(deleteRollField);
        input.add(deleteBtn);

        deleteBtn.addActionListener(e -> {
            try {
                int roll = Integer.parseInt(deleteRollField.getText());
                manager.removeStudentByRoll(roll);
                JOptionPane.showMessageDialog(this, "Student removed if existed.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid roll.");
            }
        });

        JButton back = new JButton("Back");
        back.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        panel.add(input, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }
    private JPanel createFeeSummaryPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    JLabel heading = new JLabel("Fee Summary", JLabel.CENTER);
    heading.setFont(new Font("Arial", Font.BOLD, 16));
    panel.add(heading, BorderLayout.NORTH);

    JTextArea summaryArea = new JTextArea(3, 50);
    summaryArea.setEditable(false);

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(new JScrollPane(summaryArea), BorderLayout.NORTH);

    JPanel unpaidListPanel = new JPanel();
    unpaidListPanel.setLayout(new BoxLayout(unpaidListPanel, BoxLayout.Y_AXIS));
    unpaidListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    List<Student> students = manager.getStudents();
    double total = 0, paid = 0;

    for (Student s : students) {
        total += s.getTotalFee();
        paid += s.getPaidFee();
        if (s.getDueFee() > 0) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

            String info = s.getName() +
                    " (Roll: " + s.getRoll() +
                    ", Class: " + s.getStudentClass() +
                    ", Parent: " + s.getParentName() +
                    ", DOB: " + s.getDob() +
                    ", Paid: Rs. " + s.getPaidFee() +
                    ", Total: Rs. " + s.getTotalFee() +
                    ", Due: Rs. " + s.getDueFee() + ")";

            JLabel infoLabel = new JLabel(info);
            JButton payBtn = new JButton("Pay Fee");

            payBtn.addActionListener(e -> {
                String input = JOptionPane.showInputDialog(this, "Enter amount to pay for " + s.getName());
                try {
                    double amt = Double.parseDouble(input);
                    if (amt > 0 && amt <= s.getDueFee()) {
                        s.setPaidFee(amt);
                        manager.updateStudent(s);
                        JOptionPane.showMessageDialog(this, "Payment successful. Remaining due: Rs. " + s.getDueFee());
                        showFeeSummary(); // refresh
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid amount.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number.");
                }
            });

            row.add(infoLabel, BorderLayout.CENTER);
            row.add(payBtn, BorderLayout.EAST);
            unpaidListPanel.add(row);
        }
    }

    summaryArea.setText("Total Fee Assigned: Rs. " + total +
            "\nTotal Fee Collected: Rs. " + paid +
            "\nTotal Due: Rs. " + (total - paid));

    JScrollPane unpaidScroll = new JScrollPane(unpaidListPanel);
    centerPanel.add(unpaidScroll, BorderLayout.CENTER);

    panel.add(centerPanel, BorderLayout.CENTER);

    JButton back = new JButton("Back");
    back.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
    panel.add(back, BorderLayout.SOUTH);

    return panel;
}


    private void showFeeSummary() {
        mainPanel.remove(mainPanel.getComponentCount() - 1);
        mainPanel.add(createFeeSummaryPanel(), "feesummary");
        cardLayout.show(mainPanel, "feesummary");
    }
        private JPanel createUpdateStudentSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel heading = new JLabel("Update Student Details", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(heading, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        JTextField rollInput = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        inputPanel.add(new JLabel("Enter Roll No:"));
        inputPanel.add(rollInput);
        inputPanel.add(searchBtn);

        panel.add(inputPanel, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {
            try {
                int roll = Integer.parseInt(rollInput.getText());
                Student s = manager.searchByRoll(roll);
                if (s == null) {
                    JOptionPane.showMessageDialog(this, "No student found with Roll No: " + roll);
                } else {
                    showUpdateStudentForm(s);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid roll number.");
            }
        });

        JButton back = new JButton("Back");
        back.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        panel.add(back, BorderLayout.SOUTH);

        return panel;
    }

    private void showUpdateStudentForm(Student s) {
        JPanel updatePanel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Update Details for Roll No: " + s.getRoll(), JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        updatePanel.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(11, 2, 10, 10));
        JTextField name = new JTextField(s.getName());
        JTextField email = new JTextField(s.getEmail());
        JTextField phone = new JTextField(s.getPhone());
        JComboBox<String> gender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        gender.setSelectedItem(s.getGender());
        JTextField dob = new JTextField(s.getDob());
        JTextField stdClass = new JTextField(s.getStudentClass());
        JTextField grade = new JTextField(s.getGrade());
        JTextField parent = new JTextField(s.getParentName());
        JTextField parentPhone = new JTextField(s.getParentPhone());
        JTextField paidFee = new JTextField(String.valueOf(s.getPaidFee()));

        form.add(new JLabel("Name:")); form.add(name);
        form.add(new JLabel("Email:")); form.add(email);
        form.add(new JLabel("Phone:")); form.add(phone);
        form.add(new JLabel("Gender:")); form.add(gender);
        form.add(new JLabel("DOB:")); form.add(dob);
        form.add(new JLabel("Class:")); form.add(stdClass);
        form.add(new JLabel("Grade:")); form.add(grade);
        form.add(new JLabel("Parent Name:")); form.add(parent);
        form.add(new JLabel("Parent Phone:")); form.add(parentPhone);
        form.add(new JLabel("Paid Fee:")); form.add(paidFee);

        updatePanel.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        save.addActionListener(e -> {
            try {
                double feeDiff = Double.parseDouble(paidFee.getText()) - s.getPaidFee();
                s.setPaidFee(feeDiff);

                Student updated = new Student(
                        name.getText(),
                        s.getRoll(),
                        email.getText(),
                        phone.getText(),
                        gender.getSelectedItem().toString(),
                        dob.getText(),
                        stdClass.getText(),
                        grade.getText().toUpperCase(),
                        parent.getText(),
                        parentPhone.getText(),
                        s.getTotalFee(),
                        s.getPaidFee()
                );
                manager.updateStudent(updated);
                JOptionPane.showMessageDialog(this, "Student updated successfully.");
                cardLayout.show(mainPanel, "menu");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving student. Please check fields.");
            }
        });

        cancel.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        btnPanel.add(save);
        btnPanel.add(cancel);

        updatePanel.add(btnPanel, BorderLayout.SOUTH);
        mainPanel.add(updatePanel, "updateform");
        cardLayout.show(mainPanel, "updateform");
    }
        public static void main(String[] args) {
        new StudentManagementGUI();
    }
}


