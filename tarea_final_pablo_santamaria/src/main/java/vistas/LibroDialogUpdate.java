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

    private final Libro libro; // El libro que estamos editando
    private List<Autor> autores;
    private List<Biblioteca> bibliotecas;

    public LibroDialogUpdate(Frame parent, Libro libro) {
        super(parent, "Actualizar Libro", true);
        this.libro = libro;
        setSize(420, 320);
        setLocationRelativeTo(parent);
        initComponents();
        cargarDatosActuales();
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

        btnGuardar.addActionListener(e -> actualizar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void cargarDatosActuales() {
        // Rellenamos los campos con los datos actuales del libro
        txtTitulo.setText(libro.getTitulo());
        txtAnio.setText(String.valueOf(libro.getAnioPublicacion()));
        cmbEstado.setSelectedItem(libro.getEstado());

        // Buscamos en el desplegable el autor actual del libro y lo seleccionamos
        if (libro.getAutor() != null) {
            for (int i = 0; i < cmbAutor.getItemCount(); i++) {
                String item = (String) cmbAutor.getItemAt(i);
                // Comparamos por ID (la parte antes del " - ")
                if (item.startsWith(libro.getAutor().getIdAutor() + " - ")) {
                    cmbAutor.setSelectedIndex(i);
                    break;
                }
            }
        }
        // Igual para la biblioteca
        if (libro.getBiblioteca() != null) {
            for (int i = 0; i < cmbBiblioteca.getItemCount(); i++) {
                String item = (String) cmbBiblioteca.getItemAt(i);
                if (item.startsWith(libro.getBiblioteca().getIdBiblioteca() + " - ")) {
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
                String seleccionAutor = (String) cmbAutor.getSelectedItem();
                int idAutor = Integer.parseInt(seleccionAutor.split(" - ")[0]);
                libro.setAutor(autorController.buscarPorId(idAutor));
            }
            if (cmbBiblioteca.getSelectedItem() != null && !bibliotecas.isEmpty()) {
                String seleccionBiblioteca = (String) cmbBiblioteca.getSelectedItem();
                int idBiblioteca = Integer.parseInt(seleccionBiblioteca.split(" - ")[0]);
                libro.setBiblioteca(bibliotecaController.buscarPorId(idBiblioteca));
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
