package com.attendance.ui;

import com.attendance.model.AttendanceRecord;
import com.attendance.model.Student;
import com.attendance.service.AttendanceService;
import com.attendance.service.BackupService;
import com.attendance.service.ReportService;
import com.attendance.service.StudentService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame {
    private final StudentService studentService = new StudentService();
    private final AttendanceService attendanceService = new AttendanceService();
    private final ReportService reportService = new ReportService();
    private final BackupService backupService = new BackupService();
    private final JPanel content = new JPanel(new CardLayout());
    private final JLabel clock = new JLabel();

    public MainFrame() {
        setTitle("Attendance Management System");
        setSize(1180, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        build();
        showPanel("Dashboard");
    }

    private void build() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);
        root.add(topMarquee(), BorderLayout.NORTH);
        root.add(sidebar(), BorderLayout.WEST);
        content.setBackground(Theme.BG);
        content.add(dashboardPanel(), "Dashboard");
        content.add(studentPanel(), "Students");
        content.add(attendancePanel(), "Attendance");
        content.add(dataPanel(), "Data View");
        content.add(reportPanel(), "Reports");
        content.add(analyticsPanel(), "Analytics");
        content.add(backupPanel(), "Backup");
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel topMarquee() {
        JPanel top = new JPanel(new GridLayout(2, 1, 0, 2));
        top.setBackground(new Color(15, 23, 42));
        top.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(34, 211, 238)),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        top.setPreferredSize(new java.awt.Dimension(0, 78));

        JLabel title = new TechMarqueeLabel(">>>  ATTENDANCE MANAGEMENT SYSTEM  <<<");
        title.setForeground(new Color(103, 232, 249));
        title.setFont(new Font("Consolas", Font.BOLD, 25));
        title.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(20, 184, 166), 1),
                BorderFactory.createEmptyBorder(3, 12, 3, 12)
        ));

        JLabel subtitle = new MarqueeLabel("Developed by Ashish Behera     |     Copyright 2026 Attendance Management System. All Rights Reserved.");
        subtitle.setForeground(new Color(250, 204, 21));
        subtitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        top.add(title);
        top.add(subtitle);
        return top;
    }

    private JPanel sidebar() {
        JPanel sidebar = new JPanel(new GridLayout(9, 1, 0, 6));
        sidebar.setBackground(Theme.SIDEBAR);
        sidebar.setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 10));
        sidebar.setPreferredSize(new java.awt.Dimension(260, 0));
        for (String name : new String[]{"Dashboard", "Students", "Attendance", "Data View", "Reports", "Analytics", "Backup"}) {
            JButton button = sidebarButton(name, SidebarIcon.Type.fromLabel(name), Theme.INFO);
            button.addActionListener(e -> showPanel(name));
            sidebar.add(button);
        }
        JButton logout = sidebarButton("Logout", SidebarIcon.Type.LOGOUT, Theme.BAD);
        logout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        sidebar.add(logout);
        JButton exit = sidebarButton("Exit", SidebarIcon.Type.EXIT, Theme.BAD);
        exit.addActionListener(e -> System.exit(0));
        sidebar.add(exit);
        return sidebar;
    }

    private JButton sidebarButton(String text, SidebarIcon.Type iconType, Color color) {
        JButton button = Theme.button(text, color);
        button.setIcon(new SidebarIcon(iconType));
        button.setIconTextGap(10);
        button.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        return button;
    }

    private void showPanel(String name) {
        if ("Dashboard".equals(name)) {
            content.remove(0);
            content.add(dashboardPanel(), "Dashboard", 0);
        }
        if ("Analytics".equals(name)) {
            content.remove(5);
            content.add(analyticsPanel(), "Analytics", 5);
        }
        ((CardLayout) content.getLayout()).show(content, name);
    }

    private JPanel dashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(18, 18));
        panel.setBackground(new Color(205, 14, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Dashboard");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(28f));
        header.add(title, BorderLayout.WEST);
        clock.setForeground(Color.WHITE);
        clock.setFont(clock.getFont().deriveFont(18f));
        new Timer(1000, e -> clock.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss")))).start();
        header.add(clock, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(2, 1, 16, 16));
        body.setOpaque(false);

        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 14));
        cards.setOpaque(false);
        try {
            int total = studentService.findAll().size();
            long present = attendanceService.countByStatus("Present");
            long absent = attendanceService.countByStatus("Absent");
            double percent = total == 0 ? 0 : (present * 100.0 / total);
            cards.add(dashboardMetric("Total Students", String.valueOf(total), Theme.ACCENT));
            cards.add(dashboardMetric("Present Today", String.valueOf(present), Theme.GOOD));
            cards.add(dashboardMetric("Absent Today", String.valueOf(absent), Theme.BAD));
            cards.add(dashboardMetric("Attendance %", String.format("%.1f%%", percent), Theme.WARNING));
        } catch (Exception e) {
            cards.add(dashboardMetric("Error", e.getMessage(), Theme.BAD));
        }
        body.add(cards);

        JPanel quick = new JPanel(new GridLayout(1, 4, 14, 14));
        quick.setOpaque(false);
        quick.add(dashboardActionCard("Students", "Manage student records", "Open", "Students"));
        quick.add(dashboardActionCard("Attendance", "Mark daily attendance", "Open", "Attendance"));
        quick.add(dashboardActionCard("Reports", "Export Excel and PDF", "Open", "Reports"));
        quick.add(dashboardActionCard("Analytics", "View charts and summary", "Open", "Analytics"));
        body.add(quick);

        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private JPanel metric(String title, String value, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        Theme.panel(card);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color, 2), BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Theme.MUTED);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(Theme.TEXT);
        valueLabel.setFont(valueLabel.getFont().deriveFont(22f));
        card.add(titleLabel);
        card.add(valueLabel);
        return card;
    }

    private JPanel dashboardMetric(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 3),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setIcon(dashboardMetricIcon(title));
        titleLabel.setIconTextGap(10);
        titleLabel.setForeground(new Color(71, 85, 105));
        titleLabel.setFont(titleLabel.getFont().deriveFont(16f));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(new Color(15, 23, 42));
        valueLabel.setFont(valueLabel.getFont().deriveFont(28f));
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private Icon dashboardMetricIcon(String title) {
        if ("Total Students".equals(title)) {
            return new DashboardIcon(DashboardIcon.Type.STUDENT);
        }
        if ("Present Today".equals(title)) {
            return new DashboardIcon(DashboardIcon.Type.PRESENT);
        }
        if ("Absent Today".equals(title)) {
            return new DashboardIcon(DashboardIcon.Type.ABSENT);
        }
        return new DashboardIcon(DashboardIcon.Type.PERCENT);
    }

    private JPanel dashboardActionCard(String title, String subtitle, String action, String panelName) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(new Color(255, 245, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255), 2),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setIcon(dashboardIcon(panelName));
        titleLabel.setIconTextGap(10);
        titleLabel.setForeground(new Color(127, 29, 29));
        titleLabel.setFont(titleLabel.getFont().deriveFont(20f));
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setForeground(new Color(71, 85, 105));
        JButton button = Theme.infoButton(action);
        button.addActionListener(e -> showPanel(panelName));
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(subtitleLabel, BorderLayout.CENTER);
        card.add(button, BorderLayout.SOUTH);
        return card;
    }

    private Icon dashboardIcon(String panelName) {
        if ("Students".equals(panelName)) {
            return new DashboardIcon(DashboardIcon.Type.STUDENT);
        }
        if ("Attendance".equals(panelName)) {
            return new DashboardIcon(DashboardIcon.Type.ATTENDANCE);
        }
        if ("Reports".equals(panelName)) {
            return new DashboardIcon(DashboardIcon.Type.REPORT);
        }
        return new DashboardIcon(DashboardIcon.Type.ANALYTICS);
    }

    private JPanel studentPanel() {
        JPanel panel = page("Student Management");
        DefaultTableModel model = model(new String[]{"Name", "Roll Number", "Year / Semester", "Course", "Mobile", "Email"});
        JTable table = table(model);
        loadStudents(model);

        JTextField name = new JTextField(12);
        JTextField roll = new JTextField(9);
        JTextField semester = new JTextField(9);
        JTextField course = new JTextField(9);
        JTextField mobile = new JTextField(9);
        JTextField email = new JTextField(12);

        JPanel form = formPanel();
        addField(form, "Name", name);
        addField(form, "Roll", roll);
        addField(form, "Year/Sem", semester);
        addField(form, "Course", course);
        addField(form, "Mobile", mobile);
        addField(form, "Email", email);

        JButton add = Theme.successButton("Add");
        JButton update = Theme.warningButton("Update");
        JButton delete = Theme.dangerButton("Delete");
        JButton clear = Theme.neutralButton("Clear");
        form.add(add);
        form.add(update);
        form.add(delete);
        form.add(clear);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int m = table.convertRowIndexToModel(row);
                name.setText(model.getValueAt(m, 0).toString());
                roll.setText(model.getValueAt(m, 1).toString());
                semester.setText(model.getValueAt(m, 2).toString());
                course.setText(model.getValueAt(m, 3).toString());
                mobile.setText(model.getValueAt(m, 4).toString());
                email.setText(model.getValueAt(m, 5).toString());
            }
        });

        add.addActionListener(e -> run(() -> {
            if (isBlank(name, roll, semester, course, mobile, email)) {
                message("Pehle student ki saari detail bharo.");
                return;
            }
            studentService.add(new Student(name.getText().trim(), roll.getText().trim(), semester.getText().trim(), course.getText().trim(), mobile.getText().trim(), email.getText().trim()));
            loadStudents(model);
            message("Student safalta se add ho gaya.");
        }));
        update.addActionListener(e -> run(() -> {
            studentService.update(new Student(name.getText(), roll.getText(), semester.getText(), course.getText(), mobile.getText(), email.getText()));
            loadStudents(model);
            message("Student ki detail update ho gayi.");
        }));
        delete.addActionListener(e -> run(() -> {
            if (model.getRowCount() == 0) {
                message("Abhi koi student record nahi hai.");
                return;
            }
            int row = table.getSelectedRow();
            if (row < 0) {
                message("Delete karne ke liye pehle table se student select karo.");
                return;
            }
            int m = table.convertRowIndexToModel(row);
            String selectedRoll = model.getValueAt(m, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Kya aap selected student ko delete karna chahte hain?",
                    "Delete ki pushti",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            studentService.delete(selectedRoll);
            loadStudents(model);
            clear(name, roll, semester, course, mobile, email);
            message("Student delete ho gaya.");
        }));
        clear.addActionListener(e -> clear(name, roll, semester, course, mobile, email));

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel attendancePanel() {
        JPanel panel = page("Attendance Module");
        DefaultTableModel model = model(new String[]{"Roll Number", "Name", "Year", "Status"});
        JTable table = table(model);
        JComboBox<String> year = new JComboBox<>(new String[]{"1st Year", "2nd Year"});
        JButton refresh = Theme.infoButton("Load Students");
        JButton present = Theme.successButton("Mark Present");
        JButton absent = Theme.dangerButton("Mark Absent");
        JButton saveAllPresent = Theme.successButton("All Present");

        JPanel bar = formPanel();
        addField(bar, "Year", year);
        bar.add(refresh);
        bar.add(present);
        bar.add(absent);
        bar.add(saveAllPresent);

        refresh.addActionListener(e -> loadAttendanceStudents(model, year.getSelectedItem().toString()));
        year.addActionListener(e -> loadAttendanceStudents(model, year.getSelectedItem().toString()));
        present.addActionListener(e -> markSelected(table, model, "Present"));
        absent.addActionListener(e -> markSelected(table, model, "Absent"));
        saveAllPresent.addActionListener(e -> {
            if (model.getRowCount() == 0) {
                message("Selected year me abhi koi student nahi hai.");
                return;
            }
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt("Present", i, 3);
                int index = i;
                run(() -> attendanceService.mark(model.getValueAt(index, 0).toString(), model.getValueAt(index, 1).toString(), model.getValueAt(index, 2).toString(), "Present"));
            }
            message("Attendance save ho gayi.");
        });

        loadAttendanceStudents(model, year.getSelectedItem().toString());
        panel.add(bar, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel dataPanel() {
        JPanel panel = page("JTable Data View");
        DefaultTableModel model = model(new String[]{"Date", "Time", "Roll Number", "Name", "Year", "Status"});
        JTable table = table(model);
        loadAttendance(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        JTextField search = new JTextField(24);
        search.getDocument().addDocumentListener(simpleDocumentListener(() -> sorter.setRowFilter(RowFilter.regexFilter("(?i)" + search.getText()))));
        JComboBox<String> status = new JComboBox<>(new String[]{"Present", "Absent"});
        JButton edit = Theme.warningButton("Edit Selected Status");
        edit.addActionListener(e -> run(() -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                message("Pehle table se ek record select karo.");
                return;
            }
            int m = table.convertRowIndexToModel(row);
            AttendanceRecord record = new AttendanceRecord(model.getValueAt(m, 0).toString(), model.getValueAt(m, 1).toString(),
                    model.getValueAt(m, 2).toString(), model.getValueAt(m, 3).toString(), model.getValueAt(m, 4).toString(), status.getSelectedItem().toString());
            attendanceService.update(m, record);
            loadAttendance(model);
            message("Attendance update ho gayi.");
        }));
        JPanel bar = formPanel();
        addField(bar, "Search", search);
        addField(bar, "Status", status);
        bar.add(edit);
        panel.add(bar, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel reportPanel() {
        JPanel panel = page("Excel and PDF Reports");
        JPanel actions = new JPanel(new GridLayout(2, 2, 12, 12));
        actions.setOpaque(false);
        JButton attendanceExcel = Theme.successButton("Export Attendance Excel");
        JButton studentsExcel = Theme.successButton("Export Students Excel");
        JButton attendancePdf = Theme.warningButton("Export Attendance PDF");
        JButton studentsPdf = Theme.warningButton("Export Student Summary PDF");
        actions.add(attendanceExcel);
        actions.add(studentsExcel);
        actions.add(attendancePdf);
        actions.add(studentsPdf);

        attendanceExcel.addActionListener(e -> run(() -> {
            reportService.exportAttendanceExcel(attendanceService.findAll(), Path.of("attendance_report.xlsx"));
            message("Export complete ho gaya: attendance_report.xlsx");
        }));
        studentsExcel.addActionListener(e -> run(() -> {
            reportService.exportStudentsExcel(studentService.findAll(), Path.of("students_report.xlsx"));
            message("Export complete ho gaya: students_report.xlsx");
        }));
        attendancePdf.addActionListener(e -> run(() -> {
            reportService.exportAttendancePdf(attendanceService.findAll(), Path.of("attendance_report.pdf"));
            message("Export complete ho gaya: attendance_report.pdf");
        }));
        studentsPdf.addActionListener(e -> run(() -> {
            reportService.exportStudentSummaryPdf(studentService.findAll(), Path.of("student_summary_report.pdf"));
            message("Export complete ho gaya: student_summary_report.pdf");
        }));
        panel.add(actions, BorderLayout.NORTH);
        return panel;
    }

    private JPanel analyticsPanel() {
        JPanel panel = page("Analytics");
        JPanel charts = new JPanel(new GridLayout(1, 2, 12, 12));
        charts.setOpaque(false);
        try {
            List<AttendanceRecord> records = attendanceService.findAll();
            long present = records.stream().filter(r -> r.getStatus().equalsIgnoreCase("Present")).count();
            long absent = records.stream().filter(r -> r.getStatus().equalsIgnoreCase("Absent")).count();
            DefaultPieDataset<String> pieDataset = new DefaultPieDataset<>();
            pieDataset.setValue("Present", present);
            pieDataset.setValue("Absent", absent);
            JFreeChart pie = ChartFactory.createPieChart("Present vs Absent", pieDataset, true, true, false);
            charts.add(new ChartPanel(pie));

            DefaultCategoryDataset barData = new DefaultCategoryDataset();
            Map<String, Integer> yearCounts = new LinkedHashMap<>();
            for (AttendanceRecord record : records) {
                yearCounts.merge(record.getYear() + " " + record.getStatus(), 1, Integer::sum);
            }
            yearCounts.forEach((key, value) -> barData.addValue(value, "Attendance", key));
            JFreeChart bar = ChartFactory.createBarChart("Year-wise Report", "Year", "Count", barData);
            charts.add(new ChartPanel(bar));
        } catch (Exception e) {
            charts.add(metric("Error", e.getMessage(), Theme.BAD));
        }
        panel.add(charts, BorderLayout.CENTER);
        return panel;
    }

    private JPanel backupPanel() {
        JPanel panel = page("Backup System");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setOpaque(false);
        JButton backup = Theme.successButton("Backup CSV Files");
        JButton restore = Theme.warningButton("Restore Backup");
        actions.add(backup);
        actions.add(restore);
        backup.addActionListener(e -> run(() -> message("Backup ban gaya: " + backupService.backup())));
        restore.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("backup");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                run(() -> {
                    backupService.restore(chooser.getSelectedFile().toPath());
                    message("Backup restore ho gaya.");
                });
            }
        });
        panel.add(actions, BorderLayout.NORTH);
        return panel;
    }

    private JPanel page(String title) {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(Theme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JLabel label = new JLabel(title);
        label.setForeground(Theme.TEXT);
        label.setFont(label.getFont().deriveFont(24f));
        panel.add(label, BorderLayout.WEST);
        return panel;
    }

    private JPanel formPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setOpaque(false);
        return panel;
    }

    private void addField(JPanel panel, String label, Component field) {
        JLabel jLabel = new JLabel(label);
        jLabel.setForeground(Theme.TEXT);
        panel.add(jLabel);
        panel.add(field);
    }

    private JTable table(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(26);
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(143, 172, 241));
        table.setSelectionBackground(new Color(81, 234, 208));
        table.setSelectionForeground(Color.BLUE);
        table.setGridColor(new Color(203, 213, 225));
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(218, 188, 93));
        header.setForeground(Color.BLUE);
        header.setFont(header.getFont().deriveFont(java.awt.Font.BOLD));
        return table;
    }

    private DefaultTableModel model(String[] columns) {
        return new DefaultTableModel(columns, 0);
    }

    private void loadStudents(DefaultTableModel model) {
        run(() -> {
            model.setRowCount(0);
            for (Student student : studentService.findAll()) {
                model.addRow(new Object[]{student.getName(), student.getRollNumber(), student.getSemester(), student.getCourse(),
                        student.getMobile(), student.getEmail()});
            }
        });
    }

    private void loadAttendance(DefaultTableModel model) {
        run(() -> {
            model.setRowCount(0);
            for (AttendanceRecord record : attendanceService.findAll()) {
                model.addRow(new Object[]{record.getDate(), record.getTime(), record.getRollNumber(), record.getStudentName(), record.getYear(), record.getStatus()});
            }
        });
    }

    private void loadAttendanceStudents(DefaultTableModel model, String selectedYear) {
        run(() -> {
            model.setRowCount(0);
            for (Student student : studentService.findAll()) {
                if (isStudentInYear(student, selectedYear)) {
                    model.addRow(new Object[]{student.getRollNumber(), student.getName(), selectedYear, "Absent"});
                }
            }
        });
    }

    private boolean isStudentInYear(Student student, String selectedYear) {
        String value = student.getSemester() == null ? "" : student.getSemester().trim().toLowerCase();
        String compact = value.replace(" ", "").replace("-", "");
        if ("1st Year".equals(selectedYear)) {
            return value.contains("first") || compact.contains("1styear") || compact.contains("1styr")
                    || value.equals("1") || value.equals("2") || compact.equals("sem1") || compact.equals("semester1")
                    || compact.equals("sem2") || compact.equals("semester2");
        }
        if ("2nd Year".equals(selectedYear)) {
            return value.contains("second") || compact.contains("2ndyear") || compact.contains("2ndyr")
                    || value.equals("3") || value.equals("4") || compact.equals("sem3") || compact.equals("semester3")
                    || compact.equals("sem4") || compact.equals("semester4");
        }
        return false;
    }

    private void markSelected(JTable table, DefaultTableModel model, String status) {
        int row = table.getSelectedRow();
        if (row < 0) {
            message("Pehle table se student select karo.");
            return;
        }
        int m = table.convertRowIndexToModel(row);
        model.setValueAt(status, m, 3);
        run(() -> {
            attendanceService.mark(model.getValueAt(m, 0).toString(), model.getValueAt(m, 1).toString(), model.getValueAt(m, 2).toString(), status);
            message("Attendance save ho gayi.");
        });
    }

    private void chooseFile(JTextField target, int mode) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(mode);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            target.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void clear(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private boolean isBlank(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void message(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void run(CheckedRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Galti", JOptionPane.ERROR_MESSAGE);
        }
    }

    private javax.swing.event.DocumentListener simpleDocumentListener(Runnable runnable) {
        return new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                runnable.run();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                runnable.run();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                runnable.run();
            }
        };
    }

    private interface CheckedRunnable {
        void run() throws Exception;
    }

    private static class MarqueeLabel extends JLabel {
        private final String marqueeText;
        private int textX = -1;
        private boolean initialized = false;

        MarqueeLabel(String text) {
            this.marqueeText = text;
            setOpaque(false);
            new Timer(35, e -> {
                textX -= 2;
                java.awt.FontMetrics metrics = getFontMetrics(getFont());
                if (textX < -metrics.stringWidth(marqueeText)) {
                    textX = getWidth();
                }
                repaint();
            }).start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(getFont());
            g2.setColor(getForeground());
            java.awt.FontMetrics metrics = g2.getFontMetrics();
            if (!initialized) {
                textX = getWidth();
                initialized = true;
            }
            int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            g2.drawString(marqueeText, textX, y);
            g2.dispose();
        }
    }

    private static class TechMarqueeLabel extends JLabel {
        private final String marqueeText;
        private int textX = -1;
        private boolean initialized = false;

        TechMarqueeLabel(String text) {
            this.marqueeText = text;
            setOpaque(false);
            new Timer(30, e -> {
                textX -= 3;
                java.awt.FontMetrics metrics = getFontMetrics(getFont());
                if (textX < -metrics.stringWidth(marqueeText)) {
                    textX = getWidth();
                }
                repaint();
            }).start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(getFont());
            java.awt.FontMetrics metrics = g2.getFontMetrics();
            if (!initialized) {
                textX = getWidth();
                initialized = true;
            }

            int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            g2.setColor(new Color(6, 182, 212, 80));
            g2.drawString(marqueeText, textX - 2, y - 1);
            g2.drawString(marqueeText, textX + 2, y + 1);
            g2.setColor(new Color(14, 165, 233));
            g2.drawString(marqueeText, textX + 1, y + 1);
            g2.setColor(getForeground());
            g2.drawString(marqueeText, textX, y);
            g2.setColor(new Color(255, 255, 255, 130));
            g2.drawLine(textX, y + 5, textX + metrics.stringWidth(marqueeText), y + 5);
            g2.dispose();
        }
    }

    private static class SidebarIcon implements Icon {
        enum Type {
            DASHBOARD, STUDENT, ATTENDANCE, DATA, REPORT, ANALYTICS, BACKUP, LOGOUT, EXIT;

            static Type fromLabel(String label) {
                if ("Dashboard".equals(label)) {
                    return DASHBOARD;
                }
                if ("Students".equals(label)) {
                    return STUDENT;
                }
                if ("Attendance".equals(label)) {
                    return ATTENDANCE;
                }
                if ("Data View".equals(label)) {
                    return DATA;
                }
                if ("Reports".equals(label)) {
                    return REPORT;
                }
                if ("Analytics".equals(label)) {
                    return ANALYTICS;
                }
                return BACKUP;
            }
        }

        private static final int SIZE = 22;
        private final Type type;

        SidebarIcon(Type type) {
            this.type = type;
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            if (type == Type.DASHBOARD) {
                g2.drawRoundRect(3, 3, 6, 6, 2, 2);
                g2.drawRoundRect(13, 3, 6, 6, 2, 2);
                g2.drawRoundRect(3, 13, 6, 6, 2, 2);
                g2.drawRoundRect(13, 13, 6, 6, 2, 2);
            } else if (type == Type.STUDENT) {
                g2.drawOval(8, 3, 7, 7);
                g2.drawArc(4, 11, 14, 12, 20, 140);
                g2.drawLine(5, 19, 17, 19);
            } else if (type == Type.ATTENDANCE) {
                g2.drawRoundRect(4, 4, 14, 15, 3, 3);
                g2.drawLine(7, 2, 7, 6);
                g2.drawLine(15, 2, 15, 6);
                g2.drawLine(6, 9, 16, 9);
                g2.drawLine(7, 14, 10, 17);
                g2.drawLine(10, 17, 16, 12);
            } else if (type == Type.DATA) {
                g2.drawRoundRect(3, 4, 16, 14, 3, 3);
                g2.drawLine(3, 9, 19, 9);
                g2.drawLine(3, 14, 19, 14);
                g2.drawLine(9, 4, 9, 18);
                g2.drawLine(14, 4, 14, 18);
            } else if (type == Type.REPORT) {
                g2.drawRoundRect(5, 3, 12, 16, 2, 2);
                g2.drawLine(8, 8, 14, 8);
                g2.drawLine(8, 12, 14, 12);
                g2.drawLine(8, 16, 12, 16);
            } else if (type == Type.ANALYTICS) {
                g2.drawLine(5, 18, 5, 12);
                g2.drawLine(11, 18, 11, 7);
                g2.drawLine(17, 18, 17, 4);
                g2.drawLine(3, 19, 19, 19);
            } else if (type == Type.BACKUP) {
                g2.drawOval(5, 3, 12, 5);
                g2.drawLine(5, 6, 5, 15);
                g2.drawLine(17, 6, 17, 15);
                g2.drawOval(5, 12, 12, 5);
                g2.drawLine(11, 8, 11, 14);
                g2.drawLine(8, 11, 11, 8);
                g2.drawLine(14, 11, 11, 8);
            } else if (type == Type.LOGOUT) {
                g2.drawRoundRect(4, 4, 9, 14, 2, 2);
                g2.drawLine(12, 11, 19, 11);
                g2.drawLine(16, 8, 19, 11);
                g2.drawLine(16, 14, 19, 11);
            } else {
                g2.drawArc(6, 6, 10, 10, 35, 290);
                g2.drawLine(11, 3, 11, 11);
            }

            g2.dispose();
        }
    }

    private static class DashboardIcon implements Icon {
        enum Type {
            STUDENT, ATTENDANCE, REPORT, ANALYTICS, PRESENT, ABSENT, PERCENT
        }

        private static final int SIZE = 34;
        private final Type type;

        DashboardIcon(Type type) {
            this.type = type;
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);
            g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(37, 99, 235));
            g2.fillRoundRect(0, 0, SIZE, SIZE, 10, 10);
            g2.setColor(Color.WHITE);

            if (type == Type.STUDENT) {
                g2.drawOval(12, 7, 10, 10);
                g2.drawArc(7, 18, 20, 16, 15, 150);
                g2.drawLine(8, 28, 26, 28);
            } else if (type == Type.ATTENDANCE || type == Type.PRESENT || type == Type.ABSENT) {
                g2.drawRoundRect(8, 7, 18, 20, 4, 4);
                g2.drawLine(12, 5, 12, 9);
                g2.drawLine(22, 5, 22, 9);
                g2.drawLine(11, 14, 23, 14);
                if (type == Type.ABSENT) {
                    g2.drawLine(13, 19, 22, 26);
                    g2.drawLine(22, 19, 13, 26);
                } else {
                    g2.drawLine(12, 21, 16, 25);
                    g2.drawLine(16, 25, 24, 17);
                }
            } else if (type == Type.REPORT) {
                g2.drawRoundRect(9, 6, 17, 22, 3, 3);
                g2.drawLine(13, 13, 22, 13);
                g2.drawLine(13, 18, 22, 18);
                g2.drawLine(13, 23, 19, 23);
            } else if (type == Type.ANALYTICS) {
                g2.drawLine(9, 26, 9, 17);
                g2.drawLine(16, 26, 16, 11);
                g2.drawLine(23, 26, 23, 7);
                g2.drawLine(7, 27, 27, 27);
            } else {
                g2.setFont(new Font("Segoe UI", Font.BOLD, 17));
                g2.drawString("%", 9, 23);
            }

            g2.dispose();
        }
    }
}
