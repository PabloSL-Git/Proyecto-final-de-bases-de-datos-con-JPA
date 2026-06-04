package vistas;

import modelos.entidades.Biblioteca;

import javax.swing.*;
import java.awt.*;

public class BibliotecaDialogs {

    public static Biblioteca showInsert(Component parent) {
        JTextField txtNombre = new JTextField();
        JTextField txtDireccion = new JTextField();

        String[] labels = {"Nombre:", "Dirección:"};
        Component[] fields = {txtNombre, txtDireccion};
        int res = Dialogs.showForm(parent, "Insertar Biblioteca", labels, fields);
        if (res != JOptionPane.OK_OPTION) {
            return null;
        }

        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNombre(txtNombre.getText().trim());
        biblioteca.setDireccion(txtDireccion.getText().trim());
        return biblioteca;
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
