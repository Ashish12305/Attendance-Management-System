package com.attendance.ui;

import com.attendance.service.AuthService;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.SwingUtilities;

public class LoginFrame extends JFrame {
    private final AuthService authService = new AuthService();
    private final JTextField usernameField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);

    public LoginFrame() {
        setTitle("Attendance Management System - Login");
        setSize(560, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        build();
        promptSignUpWhenNoUsers();
    }

    private void build() {
        JPanel root = new JPanel(new BorderLayout());
        Theme.panel(root);
        JLabel title = new JLabel("Login / Sign Up", JLabel.CENTER);
        title.setIcon(new LoginIcon(LoginIcon.Type.ADMIN));
        title.setIconTextGap(10);
        title.setForeground(Theme.TEXT);
        title.setFont(title.getFont().deriveFont(22f));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        Theme.panel(form);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addRow(form, gbc, 0, "Username", usernameField);
        addRow(form, gbc, 1, "Password", passwordField);

        JCheckBox showPassword = new JCheckBox("Show password");
        showPassword.setBackground(Theme.PANEL);
        showPassword.setForeground(Theme.TEXT);
        showPassword.addActionListener(e -> passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '*'));
        gbc.gridx = 1;
        gbc.gridy = 2;
        form.add(showPassword, gbc);

        JButton login = Theme.successButton("Login");
        login.setIcon(new LoginIcon(LoginIcon.Type.LOGIN));
        login.setIconTextGap(8);
        login.addActionListener(e -> login());
        JButton reset = Theme.warningButton("Reset Password");
        reset.setIcon(new LoginIcon(LoginIcon.Type.RESET));
        reset.setIconTextGap(8);
        reset.addActionListener(e -> resetPassword());
        JButton signUp = Theme.infoButton("Sign Up");
        signUp.setIcon(new LoginIcon(LoginIcon.Type.SIGN_UP));
        signUp.setIconTextGap(8);
        signUp.addActionListener(e -> signUp());
        JButton exit = Theme.dangerButton("Exit");
        exit.setIcon(new LoginIcon(LoginIcon.Type.EXIT));
        exit.setIconTextGap(8);
        exit.addActionListener(e -> dispose());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        actions.add(login);
        actions.add(reset);
        actions.add(signUp);
        actions.add(exit);
        gbc.gridx = 1;
        gbc.gridy = 3;
        form.add(actions, gbc);

        root.add(form, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void addRow(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        JLabel jLabel = new JLabel(label);
        jLabel.setIcon(new LoginIcon("Password".equals(label) ? LoginIcon.Type.PASSWORD : LoginIcon.Type.USER));
        jLabel.setIconTextGap(8);
        jLabel.setForeground(Theme.TEXT);
        gbc.gridx = 0;
        gbc.gridy = row;
        form.add(jLabel, gbc);
        gbc.gridx = 1;
        form.add(field, gbc);
    }

    private void login() {
        try {
            if (!authService.hasUsers()) {
                JOptionPane.showMessageDialog(this, "Pehle sign up karo, phir login karo.", "Sign Up zaruri hai", JOptionPane.WARNING_MESSAGE);
                signUp();
                return;
            }
            if (authService.login(usernameField.getText().trim(), new String(passwordField.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Login safalta se ho gaya.");
                new MainFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username ya password galat hai.", "Galti", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Galti", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetPassword() {
        try {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pehle username likho.", "Suchna", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JPasswordField newPasswordField = new JPasswordField(18);
            int choice = JOptionPane.showConfirmDialog(this, newPasswordField, "Naya password likho", JOptionPane.OK_CANCEL_OPTION);
            if (choice != JOptionPane.OK_OPTION) {
                return;
            }
            String newPassword = new String(newPasswordField.getPassword()).trim();
            if (newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password khali nahi ho sakta.", "Suchna", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (authService.resetPassword(username, newPassword)) {
                passwordField.setText(newPassword);
                JOptionPane.showMessageDialog(this, "Password reset ho gaya.");
            } else {
                JOptionPane.showMessageDialog(this, "User nahi mil raha.", "Suchna", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Galti", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void signUp() {
        JTextField username = new JTextField(18);
        JPasswordField password = new JPasswordField(18);
        JPasswordField confirmPassword = new JPasswordField(18);
        JTextField email = new JTextField(18);
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.add(new JLabel("Username"));
        panel.add(username);
        panel.add(new JLabel("Password"));
        panel.add(password);
        panel.add(new JLabel("Confirm Password"));
        panel.add(confirmPassword);
        panel.add(new JLabel("Email"));
        panel.add(email);

        int choice = JOptionPane.showConfirmDialog(this, panel, "Sign Up", JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String newUsername = username.getText().trim();
            String newPassword = new String(password.getPassword()).trim();
            String confirm = new String(confirmPassword.getPassword()).trim();
            String newEmail = email.getText().trim();

            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username aur password zaruri hai.", "Suchna", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!newPassword.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Password match nahi kar raha.", "Suchna", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (authService.userExists(newUsername)) {
                JOptionPane.showMessageDialog(this, "Ye username pehle se hai.", "Suchna", JOptionPane.WARNING_MESSAGE);
                return;
            }

            authService.register(newUsername, newPassword, newEmail);
            usernameField.setText(newUsername);
            passwordField.setText("");
            JOptionPane.showMessageDialog(this, "Sign up ho gaya. Ab naye account se login karo.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Galti", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void promptSignUpWhenNoUsers() {
        SwingUtilities.invokeLater(() -> {
            try {
                if (!authService.hasUsers()) {
                    JOptionPane.showMessageDialog(this, "Koi user account nahi mila. Pehle sign up karo.", "Sign Up zaruri hai", JOptionPane.INFORMATION_MESSAGE);
                    signUp();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Galti", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static class LoginIcon implements Icon {
        enum Type {
            ADMIN, USER, PASSWORD, LOGIN, RESET, SIGN_UP, EXIT
        }

        private static final int SIZE = 24;
        private final Type type;

        LoginIcon(Type type) {
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
            g2.setStroke(new BasicStroke(2.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(37, 99, 235));
            g2.fillRoundRect(0, 0, SIZE, SIZE, 7, 7);
            g2.setColor(Color.WHITE);

            if (type == Type.ADMIN || type == Type.USER) {
                g2.drawOval(8, 5, 8, 8);
                g2.drawArc(5, 14, 14, 9, 15, 150);
                if (type == Type.ADMIN) {
                    g2.drawLine(17, 6, 20, 9);
                    g2.drawLine(20, 9, 17, 12);
                }
            } else if (type == Type.PASSWORD) {
                g2.drawRoundRect(6, 11, 12, 8, 3, 3);
                g2.drawArc(8, 5, 8, 11, 0, 180);
                g2.fillOval(11, 14, 3, 3);
            } else if (type == Type.LOGIN) {
                g2.drawLine(6, 12, 17, 12);
                g2.drawLine(13, 8, 17, 12);
                g2.drawLine(13, 16, 17, 12);
                g2.drawRoundRect(7, 5, 11, 14, 3, 3);
            } else if (type == Type.RESET) {
                g2.drawArc(6, 6, 12, 12, 35, 285);
                g2.drawLine(17, 6, 18, 11);
                g2.drawLine(17, 6, 12, 6);
            } else if (type == Type.SIGN_UP) {
                g2.drawOval(6, 5, 7, 7);
                g2.drawArc(4, 13, 11, 8, 15, 150);
                g2.drawLine(18, 8, 18, 18);
                g2.drawLine(13, 13, 23, 13);
            } else {
                g2.drawLine(7, 7, 17, 17);
                g2.drawLine(17, 7, 7, 17);
            }

            g2.dispose();
        }
    }
}
