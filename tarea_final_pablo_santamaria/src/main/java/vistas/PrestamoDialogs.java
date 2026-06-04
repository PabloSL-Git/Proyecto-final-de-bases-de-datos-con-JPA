package vistas;

import modelos.entidades.Lector;
import modelos.entidades.Libro;
import modelos.entidades.Prestamo;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PrestamoDialogs {

    public static Prestamo showInsert(Component parent, List<Lector> lectores, List<Libro> librosDisponibles) {
        if (lectores == null || lectores.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay lectores registrados.");
            return null;
        }
        if (librosDisponibles == null || librosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay libros disponibles para prestar.");
            return null;
        }

        String[] opcionesLector = new String[lectores.size()];
        for (int i = 0; i < lectores.size(); i++) {
            Lector l = lectores.get(i);
            opcionesLector[i] = l.getIdLector() + " - " + l.getNombre() + " " + l.getApellido1();
        }
        String[] opcionesLibro = new String[librosDisponibles.size()];
        for (int i = 0; i < librosDisponibles.size(); i++) {
            Libro l = librosDisponibles.get(i);
            opcionesLibro[i] = l.getIdLibro() + " - " + l.getTitulo();
        }

        JTextField txtFecha = new JTextField(LocalDate.now().toString());
        JComboBox<String> cmbLector = new JComboBox<>(opcionesLector);
        JComboBox<String> cmbLibro = new JComboBox<>(opcionesLibro);
        String[] labels = {"Fecha inicio (yyyy-mm-dd):", "Lector:", "Libro (solo disponibles):"};
        Component[] fields = {txtFecha, cmbLector, cmbLibro};
        int res = Dialogs.showForm(parent, "Nuevo Préstamo", labels, fields);
        if (res != JOptionPane.OK_OPTION) return null;

        try {
            String textoLector = (String) cmbLector.getSelectedItem();
            int idLector = Integer.parseInt(textoLector.split(" - ")[0]);
            String textoLibro = (String) cmbLibro.getSelectedItem();
            int idLibro = Integer.parseInt(textoLibro.split(" - ")[0]);

            Prestamo p = new Prestamo();
            p.setFechaInicio(LocalDate.parse(txtFecha.getText().trim()));
            p.setFechaFin(null);
            p.setLector(lectores.stream().filter(l -> l.getIdLector() == idLector).findFirst().orElse(null));
            p.setLibro(librosDisponibles.stream().filter(l -> l.getIdLibro() == idLibro).findFirst().orElse(null));
            return p;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Datos inválidos (ID/fecha)");
            return null;
        }
    }
}
