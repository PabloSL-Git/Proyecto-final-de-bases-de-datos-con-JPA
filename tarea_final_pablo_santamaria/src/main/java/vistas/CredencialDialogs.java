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
            Lector l = lectores.get(i);
            opciones[i] = l.getIdLector() + " - " + l.getNombre() + " " + l.getApellido1();
        }

        JTextField txtId = new JTextField();
        JTextField txtTarj = new JTextField();
        JTextField txtFecha = new JTextField(LocalDate.now().toString());
        JComboBox<String> cmbLector = new JComboBox<>(opciones);
        String[] labels = {"ID:", "Número de tarjeta:", "Fecha emisión (yyyy-mm-dd):", "Lector:"};
        Component[] fields = {txtId, txtTarj, txtFecha, cmbLector};
        int res = Dialogs.showForm(parent, "Insertar Credencial", labels, fields);
        if (res != JOptionPane.OK_OPTION) return null;

        try {
            String seleccion = (String) cmbLector.getSelectedItem();
            int idLector = Integer.parseInt(seleccion.split(" - ")[0]);
            Credencial c = new Credencial();
            c.setIdCredencial(Integer.parseInt(txtId.getText().trim()));
            c.setNumeroTarjeta(txtTarj.getText().trim());
            c.setFechaEmision(LocalDate.parse(txtFecha.getText().trim()));
            c.setLector(lectores.stream().filter(l -> l.getIdLector() == idLector).findFirst().orElse(null));
            return c;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Datos inválidos (ID/fecha)");
            return null;
        }
    }

    public static Credencial showUpdate(Component parent, Credencial c) {
        if (c == null) return null;
        JTextField txtTarj = new JTextField(c.getNumeroTarjeta());
        String fecha = "";
        if (c.getFechaEmision() != null) fecha = c.getFechaEmision().toString();
        JTextField txtFecha = new JTextField(fecha);
        String[] labels = {"Número de tarjeta:", "Fecha emisión (yyyy-mm-dd):"};
        Component[] fields = {txtTarj, txtFecha};
        int res = Dialogs.showForm(parent, "Actualizar Credencial", labels, fields);
        if (res != JOptionPane.OK_OPTION) return null;
        c.setNumeroTarjeta(txtTarj.getText().trim());
        if (!txtFecha.getText().isBlank()) c.setFechaEmision(LocalDate.parse(txtFecha.getText().trim()));
        return c;
    }
}
