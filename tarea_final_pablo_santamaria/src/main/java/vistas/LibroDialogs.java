package vistas;

import controladores.AutorController;
import controladores.BibliotecaController;
import modelos.entidades.Autor;
import modelos.entidades.Biblioteca;
import modelos.entidades.Libro;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LibroDialogs {

    public static Libro showInsert(Component parent) {
        AutorController autorController = new AutorController();
        BibliotecaController bibliotecaController = new BibliotecaController();
        List<Autor> autores = autorController.listar();
        List<Biblioteca> bibliotecas = bibliotecaController.listar();

        JTextField txtTitulo = new JTextField();
        JTextField txtAnio = new JTextField();

        String[] opcionesAutor = new String[autores.size()];
        for (int i = 0; i < autores.size(); i++) {
            Autor a = autores.get(i);
            opcionesAutor[i] = a.getIdAutor() + " - " + a.getNombre() + " " + a.getApellido1();
        }
        JComboBox<String> cmbAutor = new JComboBox<>(opcionesAutor);

        String[] opcionesBiblioteca = new String[bibliotecas.size()];
        for (int i = 0; i < bibliotecas.size(); i++) {
            Biblioteca b = bibliotecas.get(i);
            opcionesBiblioteca[i] = b.getIdBiblioteca() + " - " + b.getNombre();
        }
        JComboBox<String> cmbBiblioteca = new JComboBox<>(opcionesBiblioteca);

        String[] labels = {"Título:", "Año publicación:", "Autor:", "Biblioteca:"};
        Component[] fields = {txtTitulo, txtAnio, cmbAutor, cmbBiblioteca};
        int res = Dialogs.showForm(parent, "Insertar Libro", labels, fields);
        if (res != JOptionPane.OK_OPTION) return null;

        try {
            Libro libro = new Libro();
            libro.setTitulo(txtTitulo.getText().trim());
            libro.setAnioPublicacion(Integer.parseInt(txtAnio.getText().trim()));
            libro.setEstado("disponible");

            if (autores.size() > 0) {
                String textoAutor = (String) cmbAutor.getSelectedItem();
                int idAutor = Integer.parseInt(textoAutor.split(" - ")[0]);
                libro.setAutor(autorController.buscarPorId(idAutor));
            }
            if (bibliotecas.size() > 0) {
                String textoBiblioteca = (String) cmbBiblioteca.getSelectedItem();
                int idB = Integer.parseInt(textoBiblioteca.split(" - ")[0]);
                libro.setBiblioteca(bibliotecaController.buscarPorId(idB));
            }
            return libro;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "El año debe ser un número entero");
            return null;
        }
    }

    public static Libro showUpdate(Component parent, Libro libro) {
        if (libro == null) return null;
        AutorController autorController = new AutorController();
        BibliotecaController bibliotecaController = new BibliotecaController();
        List<Autor> autores = autorController.listar();
        List<Biblioteca> bibliotecas = bibliotecaController.listar();

        JTextField txtTitulo = new JTextField(libro.getTitulo());
        JTextField txtAnio = new JTextField(String.valueOf(libro.getAnioPublicacion()));
        JTextField txtEstado = new JTextField(libro.getEstado());
        txtEstado.setEditable(false);

        String[] opcionesAutor = new String[autores.size()];
        for (int i = 0; i < autores.size(); i++) {
            Autor a = autores.get(i);
            opcionesAutor[i] = a.getIdAutor() + " - " + a.getNombre() + " " + a.getApellido1();
        }
        JComboBox<String> cmbAutor = new JComboBox<>(opcionesAutor);
        if (libro.getAutor() != null) {
            String pref = libro.getAutor().getIdAutor() + " - ";
            for (int i = 0; i < cmbAutor.getItemCount(); i++) {
                if (((String) cmbAutor.getItemAt(i)).startsWith(pref)) {
                    cmbAutor.setSelectedIndex(i);
                    break;
                }
            }
        }

        String[] opcionesBiblioteca = new String[bibliotecas.size()];
        for (int i = 0; i < bibliotecas.size(); i++) {
            Biblioteca b = bibliotecas.get(i);
            opcionesBiblioteca[i] = b.getIdBiblioteca() + " - " + b.getNombre();
        }
        JComboBox<String> cmbBiblioteca = new JComboBox<>(opcionesBiblioteca);
        if (libro.getBiblioteca() != null) {
            String pref = libro.getBiblioteca().getIdBiblioteca() + " - ";
            for (int i = 0; i < cmbBiblioteca.getItemCount(); i++) {
                if (((String) cmbBiblioteca.getItemAt(i)).startsWith(pref)) {
                    cmbBiblioteca.setSelectedIndex(i);
                    break;
                }
            }
        }

        String[] labels = {"Título:", "Año:", "Estado (solo lectura):", "Autor:", "Biblioteca:"};
        Component[] fields = {txtTitulo, txtAnio, txtEstado, cmbAutor, cmbBiblioteca};
        int res = Dialogs.showForm(parent, "Actualizar Libro", labels, fields);
        if (res != JOptionPane.OK_OPTION) return null;

        try {
            libro.setTitulo(txtTitulo.getText().trim());
            libro.setAnioPublicacion(Integer.parseInt(txtAnio.getText().trim()));

            if (autores.size() > 0) {
                String textoAutor = (String) cmbAutor.getSelectedItem();
                int idAutor = Integer.parseInt(textoAutor.split(" - ")[0]);
                libro.setAutor(autorController.buscarPorId(idAutor));
            }
            if (bibliotecas.size() > 0) {
                String textoBiblioteca = (String) cmbBiblioteca.getSelectedItem();
                int idB = Integer.parseInt(textoBiblioteca.split(" - ")[0]);
                libro.setBiblioteca(bibliotecaController.buscarPorId(idB));
            }
            return libro;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "El año debe ser un número entero");
            return null;
        }
    }
}
