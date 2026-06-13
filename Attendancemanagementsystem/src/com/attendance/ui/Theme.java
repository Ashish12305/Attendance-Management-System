package com.attendance.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.RenderingHints;

public final class Theme {
    public static final Color BG = new Color(121, 122, 243);
    public static final Color PANEL = Color.blue;
    public static final Color SIDEBAR = new Color(255, 255, 255);
    public static final Color TEXT = new Color(1, 8, 18);
    public static final Color MUTED = new Color(16, 234, 197);
    public static final Color ACCENT = new Color(37, 99, 235);
    public static final Color GOOD = new Color(22, 163, 74);
    public static final Color BAD = new Color(112, 218, 200);
    public static final Color WARNING = new Color(217, 119, 6);
    public static final Color INFO = new Color(8, 145, 178);
    public static final Color NEUTRAL = new Color(220, 172, 19);

    private Theme() {
    }

    public static void panel(JComponent component) {
        component.setBackground(PANEL);
        component.setForeground(TEXT);
        component.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    }

    public static JButton button(String text) {
        return button(text, ACCENT);
    }

    public static JButton successButton(String text) {
        return button(text, GOOD);
    }

    public static JButton dangerButton(String text) {
        return button(text, BAD);
    }

    public static JButton warningButton(String text) {
        return button(text, WARNING);
    }

    public static JButton infoButton(String text) {
        return button(text, INFO);
    }

    public static JButton neutralButton(String text) {
        return button(text, NEUTRAL);
    }

    public static JButton button(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFocusPainted(false);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor(color));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
            }
        });
        return button;
    }

    private static Color hoverColor(Color color) {
        return new Color(
                Math.max(0, (int) (color.getRed() * 0.82)),
                Math.max(0, (int) (color.getGreen() * 0.82)),
                Math.max(0, (int) (color.getBlue() * 0.82))
        );
    }
}
