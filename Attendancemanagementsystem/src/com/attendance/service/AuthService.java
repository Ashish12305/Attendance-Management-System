package com.attendance.service;

import com.attendance.util.AppPaths;
import com.attendance.util.CsvUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class AuthService {
    public boolean hasUsers() throws IOException {
        AppPaths.ensureDataFiles();
        for (String line : Files.readAllLines(AppPaths.USERS)) {
            if (!line.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean login(String username, String password) throws IOException {
        AppPaths.ensureDataFiles();
        for (String line : Files.readAllLines(AppPaths.USERS)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            List<String> cols = CsvUtil.parse(line);
            if (cols.size() >= 2 && cols.get(0).equals(username) && cols.get(1).equals(password)) {
                return true;
            }
        }
        return false;
    }

    public String findPassword(String username) throws IOException {
        AppPaths.ensureDataFiles();
        for (String line : Files.readAllLines(AppPaths.USERS)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            List<String> cols = CsvUtil.parse(line);
            if (cols.size() >= 2 && cols.get(0).equalsIgnoreCase(username)) {
                return cols.get(1);
            }
        }
        return null;
    }

    public boolean resetPassword(String username, String newPassword) throws IOException {
        AppPaths.ensureDataFiles();
        List<String> lines = Files.readAllLines(AppPaths.USERS);
        boolean updated = false;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().isEmpty()) {
                continue;
            }
            List<String> cols = CsvUtil.parse(lines.get(i));
            if (cols.size() >= 2 && cols.get(0).equalsIgnoreCase(username)) {
                cols.set(1, newPassword);
                lines.set(i, CsvUtil.line(cols.toArray(new String[0])));
                updated = true;
                break;
            }
        }
        if (updated) {
            Files.write(AppPaths.USERS, lines);
        }
        return updated;
    }

    public boolean userExists(String username) throws IOException {
        AppPaths.ensureDataFiles();
        for (String line : Files.readAllLines(AppPaths.USERS)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            List<String> cols = CsvUtil.parse(line);
            if (!cols.isEmpty() && cols.get(0).equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public void register(String username, String password, String email) throws IOException {
        AppPaths.ensureDataFiles();
        Files.writeString(
                AppPaths.USERS,
                CsvUtil.line(username, password, email) + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
}
