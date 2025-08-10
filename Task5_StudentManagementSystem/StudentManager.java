import java.io.*;
import java.util.*;

public class StudentManager {
    private final String FILE_NAME = "students.txt";

    public List<Student> getStudents() {
        List<Student> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    list.add(Student.fromCSV(line));
                } catch (Exception e) {
                    // Skip bad lines
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addStudent(Student s) {
        List<Student> students = getStudents();
        students.add(s);
        sortStudents(students);
        saveAll(students);
    }

    public void updateStudent(Student updatedStudent) {
        List<Student> students = getStudents();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getRoll() == updatedStudent.getRoll()) {
                students.set(i, updatedStudent);
                break;
            }
        }
        sortStudents(students);
        saveAll(students);
    }

    public void removeStudentByRoll(int roll) {
        List<Student> students = getStudents();
        students.removeIf(s -> s.getRoll() == roll);
        saveAll(students);
    }

    public Student searchByRoll(int roll) {
        for (Student s : getStudents()) {
            if (s.getRoll() == roll) return s;
        }
        return null;
    }

    public List<Student> searchByName(String name) {
        List<Student> matches = new ArrayList<>();
        for (Student s : getStudents()) {
            if (s.getName().equalsIgnoreCase(name)) {
                matches.add(s);
            }
        }
        return matches;
    }

    private void saveAll(List<Student> students) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                writer.println(s.toCSV());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Proper class-wise + alphabetical sorting
    private void sortStudents(List<Student> students) {
        students.sort((s1, s2) -> {
            int class1 = extractClassNumber(s1.getStudentClass().trim());
            int class2 = extractClassNumber(s2.getStudentClass().trim());

            if (class1 != class2) {
                return Integer.compare(class1, class2);
            }
            return s1.getName().compareToIgnoreCase(s2.getName());
        });
    }

    private int extractClassNumber(String classStr) {
        try {
            return Integer.parseInt(classStr.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }
  public void sortAndSaveExistingStudents() {
    List<Student> students = getStudents();
    this.sortAndSaveExistingStudents();
    sortStudents(students);
    saveAll(students);
}

}
