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
        if (res != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            Biblioteca biblioteca = new Biblioteca();
            biblioteca.setIdBiblioteca(Integer.parseInt(txtId.getText().trim()));
            biblioteca.setNombre(txtNombre.getText().trim());
            biblioteca.setDireccion(txtDireccion.getText().trim());
            return biblioteca;
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(parent, "El ID debe ser un número entero");
            return null;
        }
    }

    public static Biblioteca showUpdate(Component parent, Biblioteca b) {
        if (b == null) {
            return null;
        }

        Biblioteca biblioteca = b;

        String nombre = "";
        if (biblioteca.getNombre() != null) {
            nombre = biblioteca.getNombre();
        }
        JTextField txtNombre = new JTextField(nombre);

        String direccion = "";
        if (biblioteca.getDireccion() != null) {
            direccion = biblioteca.getDireccion();
        }
        JTextField txtDireccion = new JTextField(direccion);

        String[] labels = {"Nombre:", "Dirección:"};
        Component[] fields = {txtNombre, txtDireccion};
        int res = Dialogs.showForm(parent, "Actualizar Biblioteca", labels, fields);
        if (res != JOptionPane.OK_OPTION) {
            return null;
        }

        biblioteca.setNombre(txtNombre.getText().trim());
        biblioteca.setDireccion(txtDireccion.getText().trim());
        return biblioteca;
    }
}
