package vistas;

import controladores.LibroController;
import modelos.entidades.Libro;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LibroFrame extends JFrame {

    private final LibroController controller = new LibroController();
    private JTextArea textArea;

    public LibroFrame() {
        setTitle("Gestión de Libros");
        setSize(900, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        listar();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnListar     = new JButton("Listar");
        JButton btnInsertar   = new JButton("Insertar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar   = new JButton("Eliminar");

        botones.add(btnListar);
        botones.add(btnInsertar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        panel.add(botones, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(panel);

        btnListar.addActionListener(evento -> listar());
        btnInsertar.addActionListener(evento -> abrirInsertar());
        btnActualizar.addActionListener(evento -> abrirActualizar());
        btnEliminar.addActionListener(evento -> eliminar());
    }

    private void listar() {
        List<Libro> libros = controller.listar();
        textArea.setText("");
        if (libros.isEmpty()) {
            textArea.append("No hay libros registrados.\n");
            return;
        }
        for (Libro libro : libros) {
            String nombreAutor;
            if (libro.getAutor() != null) {
                nombreAutor = libro.getAutor().getNombre() + " " + libro.getAutor().getApellido1();
            } else {
                nombreAutor = "-";
            }

            String nombreBiblioteca;
            if (libro.getBiblioteca() != null) {
                nombreBiblioteca = libro.getBiblioteca().getNombre();
            } else {
                nombreBiblioteca = "-";
            }

            textArea.append("ID: " + libro.getIdLibro()
                    + " | " + libro.getTitulo()
                    + " (" + libro.getAnioPublicacion() + ")"
                    + " | Estado: " + libro.getEstado()
                    + " | Autor: " + nombreAutor
                    + " | Biblioteca: " + nombreBiblioteca + "\n");
        }
    }

    private void abrirInsertar() {
        Libro libro = LibroDialogs.showInsert(this);
        if (libro == null) {
            return;
        }

        try {
            controller.insertar(libro);
            JOptionPane.showMessageDialog(this, "Libro insertado correctamente");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al insertar libro");
            excepcion.printStackTrace();
        }
    }

    private void abrirActualizar() {
        String input = JOptionPane.showInputDialog(this, "ID del libro a actualizar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            Libro libro = controller.buscarPorId(id);
            if (libro == null) {
                JOptionPane.showMessageDialog(this, "Libro no encontrado");
                return;
            }
            Libro libroActualizado = LibroDialogs.showUpdate(this, libro);
            if (libroActualizado == null) {
                return;
            }
            controller.actualizar(libroActualizado);
            JOptionPane.showMessageDialog(this, "Libro actualizado correctamente");
            listar();
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        }
    }

    private void eliminar() {
        String input = JOptionPane.showInputDialog(this, "ID del libro a eliminar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            controller.borrarLibro(id);
            JOptionPane.showMessageDialog(this, "Libro eliminado correctamente");
            listar();
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (IllegalStateException excepcionEstado) {
            // El controlador lanza esta excepción si el libro tiene préstamos activos
            JOptionPane.showMessageDialog(this, excepcionEstado.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al eliminar libro");
            excepcion.printStackTrace();
        }
    }
}
