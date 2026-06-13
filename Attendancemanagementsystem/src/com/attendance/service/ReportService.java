package com.attendance.service;

import com.attendance.model.AttendanceRecord;
import com.attendance.model.Student;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ReportService {
    public void exportAttendanceExcel(List<AttendanceRecord> records, Path output) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream out = new FileOutputStream(output.toFile())) {
            Sheet sheet = workbook.createSheet("Attendance");
            String[] headers = {"Date", "Time", "Roll Number", "Name", "Year", "Status"};
            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }
            for (int i = 0; i < records.size(); i++) {
                AttendanceRecord record = records.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(record.getDate());
                row.createCell(1).setCellValue(record.getTime());
                row.createCell(2).setCellValue(record.getRollNumber());
                row.createCell(3).setCellValue(record.getStudentName());
                row.createCell(4).setCellValue(record.getYear());
                row.createCell(5).setCellValue(record.getStatus());
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
        }
    }

    public void exportStudentsExcel(List<Student> students, Path output) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream out = new FileOutputStream(output.toFile())) {
            Sheet sheet = workbook.createSheet("Students");
            String[] headers = {"Name", "Roll Number", "Year / Semester", "Course", "Mobile", "Email"};
            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(student.getName());
                row.createCell(1).setCellValue(student.getRollNumber());
                row.createCell(2).setCellValue(student.getSemester());
                row.createCell(3).setCellValue(student.getCourse());
                row.createCell(4).setCellValue(student.getMobile());
                row.createCell(5).setCellValue(student.getEmail());
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
        }
    }

    public void exportAttendancePdf(List<AttendanceRecord> records, Path output) throws IOException {
        try (PdfWriter writer = new PdfWriter(output.toString());
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument)) {
            document.add(new Paragraph("Attendance Report").setFontSize(18));
            Table table = new Table(6);
            for (String header : new String[]{"Date", "Time", "Roll", "Name", "Year", "Status"}) {
                table.addHeaderCell(header);
            }
            for (AttendanceRecord record : records) {
                table.addCell(record.getDate());
                table.addCell(record.getTime());
                table.addCell(record.getRollNumber());
                table.addCell(record.getStudentName());
                table.addCell(record.getYear());
                table.addCell(record.getStatus());
            }
            document.add(table);
        }
    }

    public void exportStudentSummaryPdf(List<Student> students, Path output) throws IOException {
        try (PdfWriter writer = new PdfWriter(output.toString());
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument)) {
            document.add(new Paragraph("Student Summary Report").setFontSize(18));
            document.add(new Paragraph("Total Students: " + students.size()));
            Table table = new Table(4);
            for (String header : new String[]{"Roll", "Name", "Year / Semester", "Course"}) {
                table.addHeaderCell(header);
            }
            for (Student student : students) {
                table.addCell(student.getRollNumber());
                table.addCell(student.getName());
                table.addCell(student.getSemester());
                table.addCell(student.getCourse());
            }
            document.add(table);
        }
    }
}
