package com.attendance.service;

import com.attendance.model.Student;
import com.attendance.util.AppPaths;
import com.attendance.util.CsvUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
    public List<Student> findAll() throws IOException {
        AppPaths.ensureDataFiles();
        List<Student> students = new ArrayList<>();
        List<String> lines = Files.readAllLines(AppPaths.STUDENTS);
        for (int i = 1; i < lines.size(); i++) {
            List<String> row = CsvUtil.parse(lines.get(i));
            while (row.size() < 6) {
                row.add("");
            }
            students.add(new Student(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5)));
        }
        return students;
    }

    public void saveAll(List<Student> students) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(CsvUtil.line("Name", "Roll Number", "Semester", "Course", "Mobile", "Email"));
        for (Student student : students) {
            lines.add(CsvUtil.line(student.getName(), student.getRollNumber(), student.getSemester(), student.getCourse(),
                    student.getMobile(), student.getEmail()));
        }
        Files.write(AppPaths.STUDENTS, lines);
    }

    public void add(Student student) throws IOException {
        if (isBlank(student.getName()) || isBlank(student.getRollNumber()) || isBlank(student.getSemester())
                || isBlank(student.getCourse()) || isBlank(student.getMobile()) || isBlank(student.getEmail())) {
            throw new IllegalArgumentException("Pehle student ki saari detail bharo.");
        }
        List<Student> students = findAll();
        if (findByRoll(student.getRollNumber()) != null) {
            throw new IllegalArgumentException("Ye roll number pehle se hai.");
        }
        students.add(student);
        saveAll(students);
    }

    public void update(Student updated) throws IOException {
        List<Student> students = findAll();
        boolean found = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getRollNumber().equalsIgnoreCase(updated.getRollNumber())) {
                students.set(i, updated);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Student nahi mil raha.");
        }
        saveAll(students);
    }

    public void delete(String rollNumber) throws IOException {
        List<Student> students = findAll();
        students.removeIf(student -> student.getRollNumber().equalsIgnoreCase(rollNumber));
        saveAll(students);
    }

    public Student findByRoll(String rollNumber) throws IOException {
        for (Student student : findAll()) {
            if (student.getRollNumber().equalsIgnoreCase(rollNumber)) {
                return student;
            }
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
