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

// Diálogo exclusivo para INSERTAR un nuevo libro
// Se abre desde LibroFrame al pulsar "Insertar"
public class InsertarLibroDialog extends JDialog {

    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextField txtAnio;
    private JComboBox<String> cmbEstado;
    private JComboBox<String> cmbAutor;
    private JComboBox<String> cmbBiblioteca;

    private final LibroController libroController = new LibroController();
    private final AutorController autorController = new AutorController();
    private final BibliotecaController bibliotecaController = new BibliotecaController();

    private List<Autor> autores;
    private List<Biblioteca> bibliotecas;

    public InsertarLibroDialog(Frame parent) {
        super(parent, "Insertar Libro", true); // true = modal (bloquea la ventana padre)
        setSize(420, 340);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
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

        // JComboBox limita el estado a solo los dos valores válidos del ENUM de la BD
        add(new JLabel("Estado:"));
        cmbEstado = new JComboBox<>(new String[]{"disponible", "prestado"});
        add(cmbEstado);

        // Construimos las opciones del desplegable en formato "id - nombre"
        // Así podemos mostrar texto legible y recuperar el ID al guardar
        String[] opcionesAutor = new String[autores.size()];
        for (int i = 0; i < autores.size(); i++) {
            Autor a = autores.get(i);
            opcionesAutor[i] = a.getIdAutor() + " - " + a.getNombre() + " " + a.getApellido1();
        }
        add(new JLabel("Autor:"));
        cmbAutor = new JComboBox<>(opcionesAutor);
        add(cmbAutor);

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

            // El combo muestra "3 - García Márquez"; separamos por " - " para obtener el ID
            if (!autores.isEmpty()) {
                String textoAutor = (String) cmbAutor.getSelectedItem();
                String[] partesAutor = textoAutor.split(" - ");
                int idAutor = Integer.parseInt(partesAutor[0]);
                libro.setAutor(autorController.buscarPorId(idAutor));
            }

            if (!bibliotecas.isEmpty()) {
                String textoBiblioteca = (String) cmbBiblioteca.getSelectedItem();
                String[] partesBiblioteca = textoBiblioteca.split(" - ");
                int idBiblioteca = Integer.parseInt(partesBiblioteca[0]);
                libro.setBiblioteca(bibliotecaController.buscarPorId(idBiblioteca));
            }

            libroController.insertarLibro(libro);
            JOptionPane.showMessageDialog(this, "Libro insertado correctamente");
            dispose(); // Cerramos el diálogo al terminar
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID y Año deben ser números enteros");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al insertar libro");
            e.printStackTrace();
        }
    }
}
