package vistas;

import controladores.CredencialController;
import controladores.LectorController;
import modelos.entidades.Credencial;
import modelos.entidades.Lector;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CredencialFrame extends JFrame {

    private final CredencialController controller = new CredencialController();
    private final LectorController lectorController = new LectorController();
    private JTextArea textArea;

    public CredencialFrame() {
        setTitle("Gestión de Credenciales");
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
        List<Credencial> credenciales = controller.listarCredenciales();
        textArea.setText("");
        if (credenciales.isEmpty()) {
            textArea.append("No hay credenciales registradas.\n");
            return;
        }
        for (Credencial credencial : credenciales) {
            String fechaEmision = "-";
            if (credencial.getFechaEmision() != null) {
                fechaEmision = credencial.getFechaEmision().toString();
            }

            String lector = "-";
            if (credencial.getLector() != null) {
                lector = credencial.getLector().getNombre() + " " + credencial.getLector().getApellido1();
            }

            textArea.append("ID: " + credencial.getIdCredencial()
                    + " | Tarjeta: " + credencial.getNumeroTarjeta()
                    + " | Emisión: " + fechaEmision
                    + " | Lector: " + lector + "\n");
        }
    }

    private void insertar() {
        List<Lector> lectores = lectorController.listarLectores();
        Credencial credencial = CredencialDialogs.showInsert(this, lectores);
        if (credencial == null) {
            return;
        }

        try {
            controller.insertarCredencial(credencial);
            JOptionPane.showMessageDialog(this, "Credencial insertada correctamente");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al insertar credencial");
            excepcion.printStackTrace();
        }
    }

    private void actualizar() {
        String input = JOptionPane.showInputDialog(this, "ID de la credencial a actualizar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            Credencial credencial = controller.buscarPorId(id);
            if (credencial == null) {
                JOptionPane.showMessageDialog(this, "Credencial no encontrada");
                return;
            }

            Credencial updatedCredencial = CredencialDialogs.showUpdate(this, credencial);
            if (updatedCredencial == null) {
                return;
            }

            controller.actualizarCredencial(updatedCredencial);
            JOptionPane.showMessageDialog(this, "Credencial actualizada correctamente");
            listar();
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (DateTimeParseException excepcionFecha) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa: yyyy-mm-dd");
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al actualizar credencial");
            excepcion.printStackTrace();
        }
    }

    private void eliminar() {
        String input = JOptionPane.showInputDialog(this, "ID de la credencial a eliminar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            controller.borrarCredencial(id);
            JOptionPane.showMessageDialog(this, "Credencial eliminada correctamente");
            listar();
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al eliminar credencial");
            excepcion.printStackTrace();
        }
    }
}
