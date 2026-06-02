package vistas;

import controladores.BibliotecaController;
import controladores.LectorController;
import modelos.entidades.Biblioteca;
import modelos.entidades.Lector;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LectorFrame extends JFrame {

    private final LectorController controller = new LectorController();
    private final BibliotecaController bibliotecaController = new BibliotecaController();
    private JTextArea textArea;

    public LectorFrame() {
        setTitle("Gestión de Lectores");
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

        btnListar.addActionListener(e -> listar());
        btnInsertar.addActionListener(e -> insertar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
    }

    private void listar() {
        List<Lector> lectores = controller.listarLectores();
        textArea.setText("");
        if (lectores.isEmpty()) {
            textArea.append("No hay lectores registrados.\n");
            return;
        }
        for (Lector lector : lectores) {
            String apellido2;
            if (lector.getApellido2() != null) {
                apellido2 = " " + lector.getApellido2();
            } else {
                apellido2 = "";
            }
            String email;
            if (lector.getEmail() != null) {
                email = lector.getEmail();
            } else {
                email = "-";
            }

            String telefono;
            if (lector.getTelefono() != null) {
                telefono = lector.getTelefono();
            } else {
                telefono = "-";
            }

            String nombreBiblioteca;
            if (lector.getBiblioteca() != null) {
                nombreBiblioteca = lector.getBiblioteca().getNombre();
            } else {
                nombreBiblioteca = "-";
            }

            textArea.append("ID: " + lector.getIdLector()
                    + " | " + lector.getNombre() + " " + lector.getApellido1() + apellido2
                    + " | Email: " + email
                    + " | Tel: " + telefono
                    + " | Biblioteca: " + nombreBiblioteca + "\n");
        }
    }

    private void insertar() {
        List<Biblioteca> bibliotecas = bibliotecaController.listarBibliotecas();
        Lector lector = LectorDialogs.showInsert(this, bibliotecas);
        if (lector == null) {
            return;
        }

        try {
            controller.insertarLector(lector);
            JOptionPane.showMessageDialog(this, "Lector insertado correctamente");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al insertar lector");
            excepcion.printStackTrace();
        }
    }

    private void actualizar() {
        String input = JOptionPane.showInputDialog(this, "ID del lector a actualizar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            Lector lector = controller.buscarPorId(id);
            if (lector == null) {
                JOptionPane.showMessageDialog(this, "Lector no encontrado");
                return;
            }

            List<Biblioteca> bibliotecas = bibliotecaController.listarBibliotecas();
            Lector updated = LectorDialogs.showUpdate(this, lector, bibliotecas);
            if (updated == null) {
                return;
            }

            controller.actualizarLector(updated);
            JOptionPane.showMessageDialog(this, "Lector actualizado correctamente");
            listar();
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al actualizar lector");
            excepcion.printStackTrace();
        }
    }

    private void eliminar() {
        String input = JOptionPane.showInputDialog(this, "ID del lector a eliminar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            controller.borrarLector(id);
            JOptionPane.showMessageDialog(this, "Lector eliminado correctamente");
            listar();
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (IllegalStateException excepcionEstado) {
            JOptionPane.showMessageDialog(this, excepcionEstado.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al eliminar lector");
            excepcion.printStackTrace();
        }
    }
}
