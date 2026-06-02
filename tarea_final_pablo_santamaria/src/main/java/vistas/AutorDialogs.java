package vistas;

import modelos.entidades.Autor;

import javax.swing.*;
import java.awt.*;

public class AutorDialogs {

    public static Autor showInsert(Component parent) {
        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtAp1 = new JTextField();
        JTextField txtAp2 = new JTextField();
        JTextField txtNac = new JTextField();

        String[] labels = {"ID:", "Nombre:", "Apellido 1:", "Apellido 2 (opcional):", "Nacionalidad:"};
        Component[] fields = {txtId, txtNombre, txtAp1, txtAp2, txtNac};
        int res = Dialogs.showForm(parent, "Insertar Autor", labels, fields);
        if (res != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            Autor autor = new Autor();
            autor.setIdAutor(Integer.parseInt(txtId.getText().trim()));
            autor.setNombre(txtNombre.getText().trim());
            autor.setApellido1(txtAp1.getText().trim());
            autor.setApellido2(Dialogs.textOrNull(txtAp2));
            autor.setNacionalidad(Dialogs.textOrNull(txtNac));
            return autor;
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(parent, "El ID debe ser un número entero");
            return null;
        }
    }

    public static Autor showUpdate(Component parent, Autor a) {
        if (a == null) {
            return null;
        }

        Autor autor = a;

        JTextField txtNombre = new JTextField(autor.getNombre());
        JTextField txtAp1 = new JTextField(autor.getApellido1());

        String ap2 = "";
        if (autor.getApellido2() != null) {
            ap2 = autor.getApellido2();
        }
        JTextField txtAp2 = new JTextField(ap2);

        String nac = "";
        if (autor.getNacionalidad() != null) {
            nac = autor.getNacionalidad();
        }
        JTextField txtNac = new JTextField(nac);

        String[] labels = {"Nombre:", "Apellido 1:", "Apellido 2:", "Nacionalidad:"};
        Component[] fields = {txtNombre, txtAp1, txtAp2, txtNac};
        int res = Dialogs.showForm(parent, "Actualizar Autor", labels, fields);
        if (res != JOptionPane.OK_OPTION) {
            return null;
        }

        autor.setNombre(txtNombre.getText().trim());
        autor.setApellido1(txtAp1.getText().trim());
        autor.setApellido2(Dialogs.textOrNull(txtAp2));
        autor.setNacionalidad(Dialogs.textOrNull(txtNac));
        return autor;
    }
}
