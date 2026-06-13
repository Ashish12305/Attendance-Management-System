package com.attendance.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AppPaths {
    public static final Path DATA_DIR = Path.of("data");
    public static final Path USERS = DATA_DIR.resolve("users.txt");
    public static final Path STUDENTS = DATA_DIR.resolve("students.csv");
    public static final Path ATTENDANCE = DATA_DIR.resolve("attendance.csv");
    public static final Path BACKUP_DIR = Path.of("backup");

    private AppPaths() {
    }

    public static void ensureDataFiles() throws IOException {
        Files.createDirectories(DATA_DIR);
        Files.createDirectories(BACKUP_DIR);
        if (Files.notExists(USERS)) {
            Files.createFile(USERS);
        }
        if (Files.notExists(STUDENTS)) {
            Files.writeString(STUDENTS, CsvUtil.line("Name", "Roll Number", "Year / Semester", "Course", "Mobile", "Email") + System.lineSeparator());
        }
        if (Files.notExists(ATTENDANCE)) {
            Files.writeString(ATTENDANCE, CsvUtil.line("Date", "Time", "Roll Number", "Name", "Year", "Status") + System.lineSeparator());
        }
    }
}
