package com.attendance.service;

import  com.attendance.util.AppPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupService {
    public Path backup() throws IOException {
        AppPaths.ensureDataFiles();
        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path folder = AppPaths.BACKUP_DIR.resolve("backup_" + stamp);
        Files.createDirectories(folder);
        Files.copy(AppPaths.USERS, folder.resolve("users.txt"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(AppPaths.STUDENTS, folder.resolve("students.csv"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(AppPaths.ATTENDANCE, folder.resolve("attendance.csv"), StandardCopyOption.REPLACE_EXISTING);
        return folder;
    }

    public void restore(Path folder) throws IOException {
        Files.copy(folder.resolve("users.txt"), AppPaths.USERS, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(folder.resolve("students.csv"), AppPaths.STUDENTS, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(folder.resolve("attendance.csv"), AppPaths.ATTENDANCE, StandardCopyOption.REPLACE_EXISTING);
    }
}
