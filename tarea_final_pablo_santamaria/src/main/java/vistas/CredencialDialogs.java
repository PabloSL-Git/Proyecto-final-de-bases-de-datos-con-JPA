package vistas;

import modelos.entidades.Credencial;
import modelos.entidades.Lector;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class CredencialDialogs {

    public static Credencial showInsert(Component parent, List<Lector> lectores) {
        if (lectores == null || lectores.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay lectores disponibles. Inserta uno primero.");
            return null;
        }
        String[] opciones = new String[lectores.size()];
        for (int i = 0; i < lectores.size(); i++) {
            Lector lector = lectores.get(i);
            opciones[i] = lector.getIdLector() + " - " + lector.getNombre() + " " + lector.getApellido1();
        }

        JTextField txtFecha = new JTextField(LocalDate.now().toString());
        JComboBox<String> cmbLector = new JComboBox<>(opciones);
        String[] labels = {"Fecha emisión (yyyy-mm-dd):", "Lector:"};
        Component[] fields = {txtFecha, cmbLector};
        int res = Dialogs.showForm(parent, "Insertar Credencial", labels, fields);
        if (res != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            String seleccion = (String) cmbLector.getSelectedItem();
            int idLector = Integer.parseInt(seleccion.split(" - ")[0]);
            Credencial credencial = new Credencial();
            credencial.setFechaEmision(LocalDate.parse(txtFecha.getText().trim()));
            credencial.setLector(lectores.stream().filter(lector -> lector.getIdLector() == idLector).findFirst().orElse(null));
            return credencial;
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(parent, "Datos inválidos (fecha)");
            return null;
        }
    }

    public static Credencial showUpdate(Component parent, Credencial c) {
        if (c == null) {
            return null;
        }

        Credencial credencial = c;

        JTextField txtTarj = new JTextField(credencial.getNumeroTarjeta());

        String fecha = "";
        if (credencial.getFechaEmision() != null) {
            fecha = credencial.getFechaEmision().toString();
        }
        JTextField txtFecha = new JTextField(fecha);

        String[] labels = {"Número de tarjeta:", "Fecha emisión (yyyy-mm-dd):"};
        Component[] fields = {txtTarj, txtFecha};
        int res = Dialogs.showForm(parent, "Actualizar Credencial", labels, fields);
        if (res != JOptionPane.OK_OPTION) {
            return null;
        }

        credencial.setNumeroTarjeta(txtTarj.getText().trim());
        if (!txtFecha.getText().isBlank()) {
            credencial.setFechaEmision(LocalDate.parse(txtFecha.getText().trim()));
        }
        return credencial;
    }
}
