package vistas;

import controladores.LibroController;
import modelos.entidades.Libro;
import utilidades.BackupManager;
import utilidades.JPAUtil;
import utilidades.RestoreManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {

    private LibroController libroController = new LibroController();
    private JTextArea textArea;

    public MainFrame() {
        setTitle("Biblioteca - Gestión JPA");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnListar    = new JButton("Listar Libros");
        JButton btnInsertar  = new JButton("Insertar Libro");
        JButton btnEliminar  = new JButton("Eliminar Libro");
        JButton btnActualizar = new JButton("Actualizar Libro");
        JButton btnBackup    = new JButton("Crear Backup");
        JButton btnRestaurar = new JButton("Restaurar Backup");
        JButton btnSalir     = new JButton("Salir");

        botones.add(btnListar);
        botones.add(btnInsertar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);
        botones.add(btnBackup);
        botones.add(btnRestaurar);
        botones.add(btnSalir);

        panel.add(botones, BorderLayout.NORTH);

        textArea = new JTextArea("Sistema Biblioteca listo...\n");
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        panel.add(scroll, BorderLayout.CENTER);

        add(panel);

        // Acciones
        btnListar.addActionListener(e -> listarLibros());
        btnInsertar.addActionListener(e -> {
            new LibroDialog(this).setVisible(true);
            listarLibros();
        });
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnActualizar.addActionListener(e -> abrirActualizarLibro());
        btnBackup.addActionListener(e -> crearBackup());
        btnRestaurar.addActionListener(e -> restaurarBackup());
        btnSalir.addActionListener(e -> salirDelPrograma());
    }

    // -------------------------
    // LISTAR
    // -------------------------
    private void listarLibros() {
        List<Libro> libros = libroController.listarLibros();
        textArea.setText("");
        if (libros.isEmpty()) {
            textArea.append("No hay libros registrados.\n");
            return;
        }
        for (Libro l : libros) {
            textArea.append(
                "ID: " + l.getIdLibro() +
                " | Título: " + l.getTitulo() +
                " | Año: " + l.getAnioPublicacion() +
                " | Estado: " + l.getEstado() + "\n");
        }
    }

    // -------------------------
    // ELIMINAR
    // -------------------------
    private void eliminarLibro() {
        try {
            String input = JOptionPane.showInputDialog(this, "Introduce el ID del libro a eliminar:");
            if (input == null || input.isBlank()) return;

            int id = Integer.parseInt(input.trim());
            libroController.borrarLibro(id);
            JOptionPane.showMessageDialog(this, "Libro eliminado correctamente");
            listarLibros();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar libro");
            e.printStackTrace();
        }
    }

    // -------------------------
    // ACTUALIZAR
    // -------------------------
    private void abrirActualizarLibro() {
        try {
            String input = JOptionPane.showInputDialog(this, "Introduce el ID del libro a actualizar:");
            if (input == null || input.isBlank()) return;

            int id = Integer.parseInt(input.trim());
            Libro libro = libroController.buscarPorId(id);

            if (libro == null) {
                JOptionPane.showMessageDialog(this, "Libro no encontrado");
                return;
            }

            new LibroDialogUpdate(this, libro).setVisible(true);
            listarLibros();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al abrir actualización");
            e.printStackTrace();
        }
    }

    // -------------------------
    // BACKUP
    // -------------------------
    private void crearBackup() {
        try {
            new BackupManager().crearBackup();
            JOptionPane.showMessageDialog(this, "✔ Backup creado correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear backup");
            e.printStackTrace();
        }
    }

    // -------------------------
    // RESTAURAR
    // -------------------------
    private void restaurarBackup() {
        try {
            RestoreManager rm = new RestoreManager();
            List<String> copias = rm.listarTodasLasCopias();

            if (copias.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay copias de seguridad disponibles");
                return;
            }

            String[] opciones = {
                "Restaurar última copia",
                "Seleccionar copia específica"
            };

            int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Qué deseas hacer?",
                "Restaurar Backup",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );

            String carpetaARestaurar = null;

            if (opcion == 0) {
                carpetaARestaurar = rm.obtenerUltimaCopia();  // corregido: sin la E

            } else if (opcion == 1) {
                String[] nombresBackup = new String[copias.size()];
                for (int i = 0; i < copias.size(); i++) {
                    nombresBackup[i] = new File(copias.get(i)).getName();
                }

                String seleccion = (String) JOptionPane.showInputDialog(
                    this,
                    "Selecciona la copia a restaurar:",
                    "Seleccionar Backup",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    nombresBackup,
                    nombresBackup[0]
                );

                if (seleccion != null) {
                    carpetaARestaurar = new File(".").getAbsolutePath() + File.separator + seleccion;
                }
            }

            if (carpetaARestaurar != null) {
                rm.restaurar(carpetaARestaurar);
                JOptionPane.showMessageDialog(this, "✔ Restauración completada correctamente");
                listarLibros();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al restaurar backup");
            e.printStackTrace();
        }
    }

    // -------------------------
    // SALIR
    // -------------------------
    private void salirDelPrograma() {
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Deseas salir del programa?",
            "Salir",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            JPAUtil.close(); // cerrar EntityManagerFactory correctamente
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
