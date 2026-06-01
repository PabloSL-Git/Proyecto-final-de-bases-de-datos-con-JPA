package vistas;

import controladores.AutorController;
import controladores.BibliotecaController;
import controladores.LibroController;
import modelos.entidades.Autor;
import modelos.entidades.Biblioteca;
import modelos.entidades.Libro;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LibroDialog extends JDialog {

    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextField txtAnio;
    private JComboBox<String> cmbEstado;
    private JComboBox<String> cmbAutor;
    private JComboBox<String> cmbBiblioteca;

    private final LibroController libroController = new LibroController();
    private final AutorController autorController = new AutorController();
    private final BibliotecaController bibliotecaController = new BibliotecaController();

    // Guardamos las listas para recuperar el ID al guardar
    private List<Autor> autores;
    private List<Biblioteca> bibliotecas;

    public LibroDialog(Frame parent) {
        super(parent, "Insertar Libro", true); // true = modal (bloquea la ventana padre)
        setSize(420, 340);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        // Cargamos los autores y bibliotecas para los desplegables
        autores = autorController.listarAutores();
        bibliotecas = bibliotecaController.listarBibliotecas();

        setLayout(new GridLayout(7, 2, 5, 5));

        add(new JLabel("ID:"));
        txtId = new JTextField();
        add(txtId);

        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("Año publicación:"));
        txtAnio = new JTextField();
        add(txtAnio);

        add(new JLabel("Estado:"));
        cmbEstado = new JComboBox<>(new String[]{"disponible", "prestado"});
        add(cmbEstado);

        // Construimos las opciones del desplegable de autores
        String[] opcionesAutor = new String[autores.size()];
        for (int i = 0; i < autores.size(); i++) {
            Autor a = autores.get(i);
            opcionesAutor[i] = a.getIdAutor() + " - " + a.getNombre() + " " + a.getApellido1();
        }
        add(new JLabel("Autor:"));
        cmbAutor = new JComboBox<>(opcionesAutor);
        add(cmbAutor);

        // Construimos las opciones del desplegable de bibliotecas
        String[] opcionesBiblioteca = new String[bibliotecas.size()];
        for (int i = 0; i < bibliotecas.size(); i++) {
            Biblioteca b = bibliotecas.get(i);
            opcionesBiblioteca[i] = b.getIdBiblioteca() + " - " + b.getNombre();
        }
        add(new JLabel("Biblioteca:"));
        cmbBiblioteca = new JComboBox<>(opcionesBiblioteca);
        add(cmbBiblioteca);

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        add(btnGuardar);
        add(btnCancelar);

        btnGuardar.addActionListener(e -> insertar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void insertar() {
        try {
            Libro libro = new Libro();
            libro.setIdLibro(Integer.parseInt(txtId.getText().trim()));
            libro.setTitulo(txtTitulo.getText().trim());
            libro.setAnioPublicacion(Integer.parseInt(txtAnio.getText().trim()));
            libro.setEstado((String) cmbEstado.getSelectedItem());

            // Extraemos el ID del autor del texto seleccionado y buscamos el objeto
            if (cmbAutor.getSelectedItem() != null && !autores.isEmpty()) {
                String seleccionAutor = (String) cmbAutor.getSelectedItem();
                int idAutor = Integer.parseInt(seleccionAutor.split(" - ")[0]);
                libro.setAutor(autorController.buscarPorId(idAutor));
            }
            // Igual para la biblioteca
            if (cmbBiblioteca.getSelectedItem() != null && !bibliotecas.isEmpty()) {
                String seleccionBiblioteca = (String) cmbBiblioteca.getSelectedItem();
                int idBiblioteca = Integer.parseInt(seleccionBiblioteca.split(" - ")[0]);
                libro.setBiblioteca(bibliotecaController.buscarPorId(idBiblioteca));
            }

            libroController.insertarLibro(libro);
            JOptionPane.showMessageDialog(this, "Libro insertado correctamente");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID y Año deben ser números enteros");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al insertar libro");
            e.printStackTrace();
        }
    }
}
