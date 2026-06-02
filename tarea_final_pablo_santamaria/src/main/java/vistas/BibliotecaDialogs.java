package vistas;

import modelos.entidades.Biblioteca;

import javax.swing.*;
import java.awt.*;

public class BibliotecaDialogs {

    public static Biblioteca showInsert(Component parent) {
        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtDireccion = new JTextField();

        String[] labels = {"ID:", "Nombre:", "Dirección:"};
        Component[] fields = {txtId, txtNombre, txtDireccion};
        int res = Dialogs.showForm(parent, "Insertar Biblioteca", labels, fields);
        if (res != JOptionPane.OK_OPTION) return null;

        try {
            Biblioteca b = new Biblioteca();
            b.setIdBiblioteca(Integer.parseInt(txtId.getText().trim()));
            b.setNombre(txtNombre.getText().trim());
            b.setDireccion(txtDireccion.getText().trim());
            return b;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "El ID debe ser un número entero");
            return null;
        }
    }

    public static Biblioteca showUpdate(Component parent, Biblioteca b) {
        if (b == null) return null;
        JTextField txtNombre = new JTextField(b.getNombre() != null ? b.getNombre() : "");
        JTextField txtDireccion = new JTextField(b.getDireccion() != null ? b.getDireccion() : "");
        String[] labels = {"Nombre:", "Dirección:"};
        Component[] fields = {txtNombre, txtDireccion};
        int res = Dialogs.showForm(parent, "Actualizar Biblioteca", labels, fields);
        if (res != JOptionPane.OK_OPTION) return null;
        b.setNombre(txtNombre.getText().trim());
        b.setDireccion(txtDireccion.getText().trim());
        return b;
    }
}
