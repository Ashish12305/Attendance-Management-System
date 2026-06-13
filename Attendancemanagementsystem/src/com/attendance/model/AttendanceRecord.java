package com.attendance.model;

public class AttendanceRecord {
    private String date;
    private String time;
    private String rollNumber;
    private String studentName;
    private String year;
    private String status;

    public AttendanceRecord(String date, String time, String rollNumber, String studentName, String year, String status) {
        this.date = date;
        this.time = time;
        this.rollNumber = rollNumber;
        this.studentName = studentName;
        this.year = year;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
