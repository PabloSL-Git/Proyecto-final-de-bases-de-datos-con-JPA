package vistas;

import controladores.LibroController;
import modelos.entidades.Libro;
import utilidades.BackupManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private LibroController libroController = new LibroController();

    private JTextArea textArea;

    public MainFrame() {
        setTitle("Biblioteca - Gestión JPA");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // ===== BOTONES =====
        JPanel botones = new JPanel();

        JButton btnListar = new JButton("Listar Libros");
        JButton btnSalir = new JButton("Salir");

        botones.add(btnListar);
        botones.add(btnSalir);

        panel.add(botones, BorderLayout.NORTH);

        JButton btnInsertar = new JButton("Insertar Libro");
        botones.add(btnInsertar);

        JButton btnEliminar = new JButton("Eliminar Libro");
        botones.add(btnEliminar);

        JButton btnActualizar = new JButton("Actualizar Libro");
        botones.add(btnActualizar);

        JButton btnBackup = new JButton("Backup CSV");
        botones.add(btnBackup);

        // ===== AREA DE TEXTO =====
        textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);

        panel.add(scroll, BorderLayout.CENTER);

        add(panel);

        // ===== ACCIONES =====

        btnListar.addActionListener(e -> listarLibros());

        btnSalir.addActionListener(e -> {
            System.exit(0);
        });

        btnInsertar.addActionListener(e -> {
            LibroDialog dialog = new LibroDialog(this);
            dialog.setVisible(true);
        });

        btnEliminar.addActionListener(e -> eliminarLibro());

        btnActualizar.addActionListener(e -> abrirActualizarLibro());

        btnBackup.addActionListener(e -> {
            BackupManager bm = new BackupManager();
            bm.crearBackup();
        });
    }

    private void listarLibros() {

        List<Libro> libros = libroController.listarLibros();

        textArea.setText("");

        for (Libro l : libros) {
            textArea.append(
                    "ID: " + l.getIdLibro() +
                            " | Título: " + l.getTitulo() +
                            " | Año: " + l.getAnioPublicacion() +
                            " | Estado: " + l.getEstado() +
                            "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    private void eliminarLibro() {

        try {
            String input = JOptionPane.showInputDialog(this, "Introduce el ID del libro a eliminar:");

            if (input == null || input.isEmpty()) {
                return;
            }

            int id = Integer.parseInt(input);

            libroController.borrarLibro(id);

            JOptionPane.showMessageDialog(this, "Libro eliminado correctamente");

            listarLibros(); // refrescar vista

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar libro");
            e.printStackTrace();
        }
    }

    private void abrirActualizarLibro() {

        try {
            String input = JOptionPane.showInputDialog(this, "Introduce el ID del libro a actualizar:");

            if (input == null || input.isEmpty()) {
                return;
            }

            int id = Integer.parseInt(input);

            Libro libro = libroController.buscarPorId(id);

            if (libro == null) {
                JOptionPane.showMessageDialog(this, "Libro no encontrado");
                return;
            }

            LibroDialogUpdate dialog = new LibroDialogUpdate(this, libro);
            dialog.setVisible(true);

            listarLibros(); // refrescar

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar libro");
            e.printStackTrace();
        }
    }
}
