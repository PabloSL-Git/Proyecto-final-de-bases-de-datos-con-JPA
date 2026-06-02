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
            Biblioteca b = bibliotecas.get(i);
            opcionesBiblioteca[i] = b.getIdBiblioteca() + " - " + b.getNombre();
        }

        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtAp1 = new JTextField();
        JTextField txtAp2 = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtTel = new JTextField();
        JComboBox<String> cmbBiblioteca = new JComboBox<>(opcionesBiblioteca);

        String[] labels = {"ID:", "Nombre:", "Apellido 1:", "Apellido 2 (opcional):", "Email:", "Teléfono:", "Biblioteca:"};
        Component[] fields = {txtId, txtNombre, txtAp1, txtAp2, txtEmail, txtTel, cmbBiblioteca};
        int res = Dialogs.showForm(parent, "Insertar Lector", labels, fields);
        if (res != JOptionPane.OK_OPTION) return null;

        try {
            String textoBiblioteca = (String) cmbBiblioteca.getSelectedItem();
            int idBiblioteca = Integer.parseInt(textoBiblioteca.split(" - ")[0]);

            Lector l = new Lector();
            l.setIdLector(Integer.parseInt(txtId.getText().trim()));
            l.setNombre(txtNombre.getText().trim());
            l.setApellido1(txtAp1.getText().trim());
            l.setApellido2(Dialogs.textOrNull(txtAp2));
            l.setEmail(Dialogs.textOrNull(txtEmail));
            l.setTelefono(Dialogs.textOrNull(txtTel));
            l.setBiblioteca(bibliotecas.stream().filter(b -> b.getIdBiblioteca() == idBiblioteca).findFirst().orElse(null));
            return l;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "El ID debe ser un número entero");
            return null;
        }
    }

    public static Lector showUpdate(Component parent, Lector l, List<Biblioteca> bibliotecas) {
        if (l == null) return null;
        String[] opcionesBiblioteca = new String[bibliotecas.size()];
        for (int i = 0; i < bibliotecas.size(); i++) {
            Biblioteca b = bibliotecas.get(i);
            opcionesBiblioteca[i] = b.getIdBiblioteca() + " - " + b.getNombre();
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

        JTextField txtNombre = new JTextField(l.getNombre());
        JTextField txtAp1 = new JTextField(l.getApellido1());
        JTextField txtAp2 = new JTextField(l.getApellido2() != null ? l.getApellido2() : "");
        JTextField txtEmail = new JTextField(l.getEmail() != null ? l.getEmail() : "");
        JTextField txtTel = new JTextField(l.getTelefono() != null ? l.getTelefono() : "");

        String[] labels = {"Nombre:", "Apellido 1:", "Apellido 2:", "Email:", "Teléfono:", "Biblioteca:"};
        Component[] fields = {txtNombre, txtAp1, txtAp2, txtEmail, txtTel, cmbBiblioteca};
        int res = Dialogs.showForm(parent, "Actualizar Lector", labels, fields);
        if (res != JOptionPane.OK_OPTION) return null;

        String textoBiblioteca = (String) cmbBiblioteca.getSelectedItem();
        int idBiblioteca = Integer.parseInt(textoBiblioteca.split(" - ")[0]);

        l.setNombre(txtNombre.getText().trim());
        l.setApellido1(txtAp1.getText().trim());
        l.setApellido2(Dialogs.textOrNull(txtAp2));
        l.setEmail(Dialogs.textOrNull(txtEmail));
        l.setTelefono(Dialogs.textOrNull(txtTel));
        l.setBiblioteca(bibliotecas.stream().filter(b -> b.getIdBiblioteca() == idBiblioteca).findFirst().orElse(null));
        return l;
    }
}
