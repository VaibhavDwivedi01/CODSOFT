public class Student {
    private String name;
    private int roll;
    private String email;
    private String phone;
    private String gender;
    private String dob;
    private String studentClass;
    private String grade;
    private String parentName;
    private String parentPhone;
    private double totalFee;
    private double paidFee;
    private double dueFee;
    private String feeStatus;

    public Student(String name, int roll, String email, String phone, String gender,
                   String dob, String studentClass, String grade,
                   String parentName, String parentPhone,
                   double totalFee, double paidFee) {
        this.name = name;
        this.roll = roll;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.studentClass = studentClass;
        this.grade = grade;
        this.parentName = parentName;
        this.parentPhone = parentPhone;
        this.totalFee = totalFee;
        this.paidFee = paidFee;
        this.dueFee = totalFee - paidFee;
        this.feeStatus = dueFee <= 0 ? "Paid" : "Unpaid";
    }

    public int getRoll() { return roll; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getDob() { return dob; }
    public String getStudentClass() { return studentClass; }
    public String getGrade() { return grade; }
    public String getParentName() { return parentName; }
    public String getParentPhone() { return parentPhone; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public double getTotalFee() { return totalFee; }
    public double getPaidFee() { return paidFee; }
    public double getDueFee() { return totalFee - paidFee; }
    public String getFeeStatus() { return feeStatus; }

    public void setPaidFee(double newPaid) {
        this.paidFee += newPaid;
        this.dueFee = totalFee - paidFee;
        this.feeStatus = dueFee <= 0 ? "Paid" : "Unpaid";
    }

    public Object[] toObjectArray() {
        return new Object[] {
            name, roll, email, phone, gender, dob, studentClass, grade,
            parentName, parentPhone, totalFee, paidFee, dueFee, feeStatus
        };
    }

    public String toCSV() {
        return String.join(",", name, String.valueOf(roll), email, phone, gender,
                dob, studentClass, grade, parentName, parentPhone,
                String.valueOf(totalFee), String.valueOf(paidFee));
    }

    public static Student fromCSV(String line) {
        String[] p = line.split(",");
        return new Student(
            p[0], Integer.parseInt(p[1]), p[2], p[3], p[4],
            p[5], p[6], p[7], p[8], p[9],
            Double.parseDouble(p[10]), Double.parseDouble(p[11])
        );
    }

    @Override
    public String toString() {
        return name + " (Roll " + roll + ", Class " + studentClass + ", DOB " + dob + ") - Rs. " + paidFee + " paid, Rs. " + getDueFee() + " due.";
    }
}
