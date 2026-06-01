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
        JButton btnListar    = new JButton("Listar");
        JButton btnInsertar  = new JButton("Insertar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar  = new JButton("Eliminar");

        botones.add(btnListar);
        botones.add(btnInsertar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        panel.add(botones, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(panel);

        btnListar.addActionListener(e -> listar());
        btnInsertar.addActionListener(e -> {
            new LibroDialog(this).setVisible(true);
            listar();
        });
        btnActualizar.addActionListener(e -> {
            abrirActualizar();
            listar();
        });
        btnEliminar.addActionListener(e -> eliminar());
    }

    private void listar() {
        List<Libro> libros = controller.listarLibros();
        textArea.setText("");
        if (libros.isEmpty()) {
            textArea.append("No hay libros registrados.\n");
            return;
        }
        for (Libro l : libros) {
            String autor = l.getAutor() != null ? l.getAutor().getNombre() + " " + l.getAutor().getApellido1() : "-";
            String bib   = l.getBiblioteca() != null ? l.getBiblioteca().getNombre() : "-";
            textArea.append("ID: " + l.getIdLibro()
                    + " | " + l.getTitulo()
                    + " (" + l.getAnioPublicacion() + ")"
                    + " | Estado: " + l.getEstado()
                    + " | Autor: " + autor
                    + " | Biblioteca: " + bib + "\n");
        }
    }

    private void abrirActualizar() {
        String input = JOptionPane.showInputDialog(this, "ID del libro a actualizar:");
        if (input == null || input.isBlank()) return;
        try {
            int id = Integer.parseInt(input.trim());
            Libro libro = controller.buscarPorId(id);
            if (libro == null) { JOptionPane.showMessageDialog(this, "Libro no encontrado"); return; }
            new LibroDialogUpdate(this, libro).setVisible(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        }
    }

    private void eliminar() {
        String input = JOptionPane.showInputDialog(this, "ID del libro a eliminar:");
        if (input == null || input.isBlank()) return;
        try {
            int id = Integer.parseInt(input.trim());
            controller.borrarLibro(id);
            JOptionPane.showMessageDialog(this, "Libro eliminado correctamente");
            listar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar libro");
            e.printStackTrace();
        }
    }
}
