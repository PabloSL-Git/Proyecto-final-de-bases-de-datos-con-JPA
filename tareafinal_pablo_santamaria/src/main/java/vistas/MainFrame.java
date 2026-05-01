package vistas;

import controladores.LibroController;
import modelos.entidades.Libro;

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
}
