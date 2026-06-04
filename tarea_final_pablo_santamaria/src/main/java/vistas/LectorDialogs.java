package vistas;

import modelos.entidades.Biblioteca;
import modelos.entidades.Lector;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LectorDialogs {

    public static Lector showInsert(Component parent, List<Biblioteca> bibliotecas) {
        if (bibliotecas == null || bibliotecas.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay bibliotecas disponibles. Inserta una primero.");
            return null;
        }

        String[] opcionesBiblioteca = new String[bibliotecas.size()];
        for (int i = 0; i < bibliotecas.size(); i++) {
            Biblioteca biblioteca = bibliotecas.get(i);
            opcionesBiblioteca[i] = biblioteca.getIdBiblioteca() + " - " + biblioteca.getNombre();
        }

        JTextField txtNombre = new JTextField();
        JTextField txtAp1 = new JTextField();
        JTextField txtAp2 = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtTel = new JTextField();
        JComboBox<String> cmbBiblioteca = new JComboBox<>(opcionesBiblioteca);

        String[] labels = {"Nombre:", "Apellido 1:", "Apellido 2 (opcional):", "Email:", "Teléfono:", "Biblioteca:"};
        Component[] fields = {txtNombre, txtAp1, txtAp2, txtEmail, txtTel, cmbBiblioteca};
        int res = Dialogs.showForm(parent, "Insertar Lector", labels, fields);
        if (res != JOptionPane.OK_OPTION) {
            return null;
        }

        String textoBiblioteca = (String) cmbBiblioteca.getSelectedItem();
        int idBiblioteca = Integer.parseInt(textoBiblioteca.split(" - ")[0]);

        Lector lector = new Lector();
        lector.setNombre(txtNombre.getText().trim());
        lector.setApellido1(txtAp1.getText().trim());
        lector.setApellido2(Dialogs.textOrNull(txtAp2));
        lector.setEmail(Dialogs.textOrNull(txtEmail));
        lector.setTelefono(Dialogs.textOrNull(txtTel));
        lector.setBiblioteca(bibliotecas.stream().filter(biblioteca -> biblioteca.getIdBiblioteca() == idBiblioteca).findFirst().orElse(null));
        return lector;
    }

    public static Lector showUpdate(Component parent, Lector l, List<Biblioteca> bibliotecas) {
        if (l == null) {
            return null;
        }

        String[] opcionesBiblioteca = new String[bibliotecas.size()];
        for (int i = 0; i < bibliotecas.size(); i++) {
            Biblioteca biblioteca = bibliotecas.get(i);
            opcionesBiblioteca[i] = biblioteca.getIdBiblioteca() + " - " + biblioteca.getNombre();
        }
        JComboBox<String> cmbBiblioteca = new JComboBox<>(opcionesBiblioteca);
        if (l.getBiblioteca() != null) {
            String pref = l.getBiblioteca().getIdBiblioteca() + " - ";
            for (int i = 0; i < cmbBiblioteca.getItemCount(); i++) {
                if (((String) cmbBiblioteca.getItemAt(i)).startsWith(pref)) {
                    cmbBiblioteca.setSelectedIndex(i);
                    break;
                }
            }
        }

        Lector lector = l;

        JTextField txtNombre = new JTextField(lector.getNombre());
        JTextField txtAp1 = new JTextField(lector.getApellido1());

        String ap2 = "";
        if (lector.getApellido2() != null) {
            ap2 = lector.getApellido2();
        }
        JTextField txtAp2 = new JTextField(ap2);

        String email = "";
        if (lector.getEmail() != null) {
            email = lector.getEmail();
        }
        JTextField txtEmail = new JTextField(email);

        String telefono = "";
        if (lector.getTelefono() != null) {
            telefono = lector.getTelefono();
        }
        JTextField txtTel = new JTextField(telefono);

        String[] labels = {"Nombre:", "Apellido 1:", "Apellido 2:", "Email:", "Teléfono:", "Biblioteca:"};
        Component[] fields = {txtNombre, txtAp1, txtAp2, txtEmail, txtTel, cmbBiblioteca};
        int res = Dialogs.showForm(parent, "Actualizar Lector", labels, fields);
        if (res != JOptionPane.OK_OPTION) {
            return null;
        }

        String textoBiblioteca = (String) cmbBiblioteca.getSelectedItem();
        int idBiblioteca = Integer.parseInt(textoBiblioteca.split(" - ")[0]);

        lector.setNombre(txtNombre.getText().trim());
        lector.setApellido1(txtAp1.getText().trim());
        lector.setApellido2(Dialogs.textOrNull(txtAp2));
        lector.setEmail(Dialogs.textOrNull(txtEmail));
        lector.setTelefono(Dialogs.textOrNull(txtTel));
        lector.setBiblioteca(bibliotecas.stream().filter(biblioteca -> biblioteca.getIdBiblioteca() == idBiblioteca).findFirst().orElse(null));
        return lector;
    }
}
