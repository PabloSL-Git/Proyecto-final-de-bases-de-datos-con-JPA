package vistas;

import javax.swing.*;
import java.awt.*;

public class Dialogs {

    public static int showForm(Component parent, String title, String[] labels, JTextField[] fields) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 5, 5));
        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            panel.add(fields[i]);
        }
        return JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION);
    }

    public static String textOrNull(JTextField f) {
        if (f == null) return null;
        String s = f.getText().trim();
        return s.isBlank() ? null : s;
    }
}
