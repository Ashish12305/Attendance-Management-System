package com.attendance.service;

import com.attendance.model.AttendanceRecord;
import com.attendance.util.AppPaths;
import com.attendance.util.CsvUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AttendanceService {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public List<AttendanceRecord> findAll() throws IOException {
        AppPaths.ensureDataFiles();
        List<AttendanceRecord> records = new ArrayList<>();
        List<String> lines = Files.readAllLines(AppPaths.ATTENDANCE);
        for (int i = 1; i < lines.size(); i++) {
            List<String> row = CsvUtil.parse(lines.get(i));
            while (row.size() < 6) {
                row.add("");
            }
            records.add(new AttendanceRecord(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5)));
        }
        return records;
    }

    public void saveAll(List<AttendanceRecord> records) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(CsvUtil.line("Date", "Time", "Roll Number", "Name", "Year", "Status"));
        for (AttendanceRecord record : records) {
            lines.add(CsvUtil.line(record.getDate(), record.getTime(), record.getRollNumber(),
                    record.getStudentName(), record.getYear(), record.getStatus()));
        }
        Files.write(AppPaths.ATTENDANCE, lines);
    }

    public void mark(String rollNumber, String name, String year, String status) throws IOException {
        List<AttendanceRecord> records = findAll();
        String today = LocalDate.now().toString();
        String now = LocalTime.now().format(TIME_FORMAT);
        for (AttendanceRecord record : records) {
            if (record.getDate().equals(today)
                    && record.getRollNumber().equalsIgnoreCase(rollNumber)
                    && record.getYear().equalsIgnoreCase(year)) {
                record.setStatus(status);
                record.setTime(now);
                saveAll(records);
                return;
            }
        }
        records.add(new AttendanceRecord(today, now, rollNumber, name, year, status));
        saveAll(records);
    }

    public void update(int index, AttendanceRecord updated) throws IOException {
        List<AttendanceRecord> records = findAll();
        if (index < 0 || index >= records.size()) {
            throw new IllegalArgumentException("Attendance record nahi mil raha.");
        }
        records.set(index, updated);
        saveAll(records);
    }

    public long countByStatus(String status) throws IOException {
        return findAll().stream().filter(record -> record.getDate().equals(LocalDate.now().toString()))
                .filter(record -> record.getStatus().equalsIgnoreCase(status)).count();
    }
}
