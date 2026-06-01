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

public class LibroDialogUpdate extends JDialog {

    private JTextField txtTitulo;
    private JTextField txtAnio;
    private JComboBox<String> cmbEstado;
    private JComboBox<String> cmbAutor;
    private JComboBox<String> cmbBiblioteca;

    private final LibroController libroController = new LibroController();
    private final AutorController autorController = new AutorController();
    private final BibliotecaController bibliotecaController = new BibliotecaController();

    private final Libro libro;
    private List<Autor> autores;
    private List<Biblioteca> bibliotecas;

    public LibroDialogUpdate(Frame parent, Libro libro) {
        super(parent, "Actualizar Libro", true);
        this.libro = libro;
        setSize(420, 300);
        setLocationRelativeTo(parent);
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        autores = autorController.listarAutores();
        bibliotecas = bibliotecaController.listarBibliotecas();

        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("Año:"));
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

        btnGuardar.addActionListener(e -> actualizar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void cargarDatos() {
        txtTitulo.setText(libro.getTitulo());
        txtAnio.setText(String.valueOf(libro.getAnioPublicacion()));
        cmbEstado.setSelectedItem(libro.getEstado());

        if (libro.getAutor() != null) {
            for (int i = 0; i < cmbAutor.getItemCount(); i++) {
                if (((String) cmbAutor.getItemAt(i)).startsWith(libro.getAutor().getIdAutor() + " - ")) {
                    cmbAutor.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (libro.getBiblioteca() != null) {
            for (int i = 0; i < cmbBiblioteca.getItemCount(); i++) {
                if (((String) cmbBiblioteca.getItemAt(i)).startsWith(libro.getBiblioteca().getIdBiblioteca() + " - ")) {
                    cmbBiblioteca.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void actualizar() {
        try {
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

            libroController.actualizarLibro(libro);
            JOptionPane.showMessageDialog(this, "Libro actualizado correctamente");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El año debe ser un número entero");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar libro");
            e.printStackTrace();
        }
    }
}
