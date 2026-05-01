package vistas;

import controladores.LibroController;
import modelos.entidades.Libro;
import utilidades.BackupManager;
import utilidades.RestoreManager;
import java.io.File;

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

        System.out.println("Cargando MainFrame...");

        JPanel panel = new JPanel(new BorderLayout());

        JPanel botones = new JPanel();

        // BOTONES
        JButton btnListar = new JButton("Listar Libros");
        JButton btnInsertar = new JButton("Insertar Libro");
        JButton btnEliminar = new JButton("Eliminar Libro");
        JButton btnActualizar = new JButton("Actualizar Libro");
        JButton btnBackup = new JButton("Backup CSV");
        JButton btnRestaurar = new JButton("Restaurar Backup");
        JButton btnSalir = new JButton("Salir");
        

        botones.add(btnListar);
        botones.add(btnInsertar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);
        botones.add(btnBackup);
        botones.add(btnRestaurar);
        botones.add(btnSalir);

        panel.add(botones, BorderLayout.NORTH);

        textArea = new JTextArea("Sistema Biblioteca listo...");
        JScrollPane scroll = new JScrollPane(textArea);

        panel.add(scroll, BorderLayout.CENTER);

        add(panel);

        // ACCIONES
        btnListar.addActionListener(e -> listarLibros());
        btnSalir.addActionListener(e -> salirDelPrograma());

        btnInsertar.addActionListener(e -> new LibroDialog(this).setVisible(true));

        btnEliminar.addActionListener(e -> eliminarLibro());

        btnActualizar.addActionListener(e -> abrirActualizarLibro());

        btnBackup.addActionListener(e -> new utilidades.BackupManager().crearBackup());

        btnRestaurar.addActionListener(e -> restaurarBackup());
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

    private void restaurarBackup() {

        try {
            RestoreManager rm = new RestoreManager();
            
            // Verificar si hay copias disponibles
            java.util.List<String> copias = rm.listarTodasLasCopias();
            
            if (copias.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay copias de seguridad disponibles");
                return;
            }

            // Mostrar opciones
            String[] opciones = {
                "1. Restaurar última copia",
                "2. Seleccionar copia específica"
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
                // Restaurar última copia
                carpetaARestaurar = rm.obtenerUltimaEopia();
            } else if (opcion == 1) {
                // Seleccionar copia específica
                String[] nombresBackup = new String[copias.size()];
                for (int i = 0; i < copias.size(); i++) {
                    File f = new File(copias.get(i));
                    nombresBackup[i] = f.getName();
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

    private void salirDelPrograma() {
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Deseas salir del programa?",
            "Salir",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
