package vistas;

import utilidades.BackupManager;
import utilidades.JPAUtil;
import utilidades.RestoreManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Biblioteca - Gestión JPA");
        setSize(500, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("Sistema de Gestión de Biblioteca", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(4, 2, 10, 10));

        JButton btnLibros       = new JButton("Gestionar Libros");
        JButton btnAutores      = new JButton("Gestionar Autores");
        JButton btnBibliotecas  = new JButton("Gestionar Bibliotecas");
        JButton btnLectores     = new JButton("Gestionar Lectores");
        JButton btnCredenciales = new JButton("Gestionar Credenciales");
        JButton btnPrestamos    = new JButton("Gestionar Préstamos");
        JButton btnBackup       = new JButton("Backup / Restaurar");
        JButton btnSalir        = new JButton("Salir");

        grid.add(btnLibros);
        grid.add(btnAutores);
        grid.add(btnBibliotecas);
        grid.add(btnLectores);
        grid.add(btnCredenciales);
        grid.add(btnPrestamos);
        grid.add(btnBackup);
        grid.add(btnSalir);

        panel.add(grid, BorderLayout.CENTER);
        add(panel);

        btnLibros.addActionListener(e -> new LibroFrame().setVisible(true));
        btnAutores.addActionListener(e -> new AutorFrame().setVisible(true));
        btnBibliotecas.addActionListener(e -> new BibliotecaFrame().setVisible(true));
        btnLectores.addActionListener(e -> new LectorFrame().setVisible(true));
        btnCredenciales.addActionListener(e -> new CredencialFrame().setVisible(true));
        btnPrestamos.addActionListener(e -> new PrestamoFrame().setVisible(true));
        btnBackup.addActionListener(e -> abrirBackup());
        btnSalir.addActionListener(e -> salir());
    }

    private void abrirBackup() {
        String[] opciones = {"Crear Backup", "Restaurar Backup"};
        int op = JOptionPane.showOptionDialog(this, "¿Qué deseas hacer?", "Backup",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (op == 0) {
            try {
                new BackupManager().crearBackup();
                JOptionPane.showMessageDialog(this, "Backup creado correctamente");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al crear backup");
                e.printStackTrace();
            }
        } else if (op == 1) {
            restaurar();
        }
    }

    private void restaurar() {
        try {
            RestoreManager rm = new RestoreManager();
            List<String> copias = rm.listarTodasLasCopias();

            if (copias.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay copias de seguridad disponibles");
                return;
            }

            String[] opciones = {"Restaurar última copia", "Seleccionar copia específica"};
            int op = JOptionPane.showOptionDialog(this, "¿Qué deseas restaurar?", "Restaurar Backup",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            String carpeta = null;
            if (op == 0) {
                carpeta = rm.obtenerUltimaCopia();
            } else if (op == 1) {
                String[] nombres = copias.stream()
                        .map(c -> new File(c).getName())
                        .toArray(String[]::new);
                String sel = (String) JOptionPane.showInputDialog(this, "Selecciona la copia:",
                        "Seleccionar Backup", JOptionPane.QUESTION_MESSAGE, null, nombres, nombres[0]);
                if (sel != null) {
                    carpeta = new File(".").getAbsolutePath() + File.separator + sel;
                }
            }

            if (carpeta != null) {
                rm.restaurar(carpeta);
                JOptionPane.showMessageDialog(this, "Restauración completada correctamente");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al restaurar backup");
            e.printStackTrace();
        }
    }

    private void salir() {
        int r = JOptionPane.showConfirmDialog(this, "¿Deseas salir del programa?", "Salir",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION) {
            JPAUtil.close();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
