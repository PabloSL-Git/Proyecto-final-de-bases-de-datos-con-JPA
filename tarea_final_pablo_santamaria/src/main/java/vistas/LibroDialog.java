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

    private List<Autor> autores;
    private List<Biblioteca> bibliotecas;

    public LibroDialog(Frame parent) {
        super(parent, "Insertar Libro", true);
        setSize(420, 320);
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

        add(new JLabel("Estado:"));
        cmbEstado = new JComboBox<>(new String[]{"disponible", "prestado"});
        add(cmbEstado);

        add(new JLabel("Autor:"));
        String[] opAutores = autores.stream()
                .map(a -> a.getIdAutor() + " - " + a.getNombre() + " " + a.getApellido1())
                .toArray(String[]::new);
        cmbAutor = new JComboBox<>(opAutores);
        add(cmbAutor);

        add(new JLabel("Biblioteca:"));
        String[] opBibliotecas = bibliotecas.stream()
                .map(b -> b.getIdBiblioteca() + " - " + b.getNombre())
                .toArray(String[]::new);
        cmbBiblioteca = new JComboBox<>(opBibliotecas);
        add(cmbBiblioteca);

        JButton btnGuardar = new JButton("Guardar");
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

            if (cmbAutor.getSelectedItem() != null && !autores.isEmpty()) {
                int idAutor = Integer.parseInt(((String) cmbAutor.getSelectedItem()).split(" - ")[0]);
                libro.setAutor(autorController.buscarPorId(idAutor));
            }
            if (cmbBiblioteca.getSelectedItem() != null && !bibliotecas.isEmpty()) {
                int idBib = Integer.parseInt(((String) cmbBiblioteca.getSelectedItem()).split(" - ")[0]);
                libro.setBiblioteca(bibliotecaController.buscarPorId(idBib));
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
