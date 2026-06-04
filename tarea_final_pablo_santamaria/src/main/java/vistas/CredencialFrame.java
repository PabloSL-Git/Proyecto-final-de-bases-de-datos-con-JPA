package vistas;

import controladores.CredencialController;
import modelos.entidades.Credencial;
import modelos.entidades.Lector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CredencialFrame extends JFrame {

    private final CredencialController controller = new CredencialController();
    private JTable tabla;
    private DefaultTableModel modelo;

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

        String[] columnas = {"ID", "Número Tarjeta", "Fecha Emisión", "Lector"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panel);

        btnListar.addActionListener(e -> listar());
        btnInsertar.addActionListener(e -> insertar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
    }

    private void listar() {
        modelo.setRowCount(0);
        List<Credencial> credenciales = controller.listar();
        for (Credencial credencial : credenciales) {
            String lector = "-";
            if (credencial.getLector() != null) {
                lector = credencial.getLector().getNombre() + " " + credencial.getLector().getApellido1();
            }
            modelo.addRow(new Object[]{
                credencial.getIdCredencial(),
                credencial.getNumeroTarjeta(),
                credencial.getFechaEmision() != null ? credencial.getFechaEmision().toString() : "-",
                lector
            });
        }
    }

    private int getIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una credencial de la tabla primero.");
            return -1;
        }
        return (int) modelo.getValueAt(fila, 0);
    }

    private void insertar() {
        List<Lector> lectores = controller.listarLectoresSinCredencial();
        if (lectores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los lectores ya tienen una credencial asignada.");
            return;
        }
        Credencial credencial = CredencialDialogs.showInsert(this, lectores);
        if (credencial == null) return;
        try {
            controller.insertar(credencial);
            JOptionPane.showMessageDialog(this, "Credencial insertada correctamente");
            listar();
        } catch (IllegalStateException excepcionEstado) {
            JOptionPane.showMessageDialog(this, excepcionEstado.getMessage(), "No se puede insertar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al insertar credencial");
            excepcion.printStackTrace();
        }
    }

    private void actualizar() {
        int id = getIdSeleccionado();
        if (id == -1) return;
        try {
            Credencial credencial = controller.buscarPorId(id);
            if (credencial == null) {
                JOptionPane.showMessageDialog(this, "Credencial no encontrada");
                return;
            }
            Credencial updatedCredencial = CredencialDialogs.showUpdate(this, credencial);
            if (updatedCredencial == null) return;
            controller.actualizar(updatedCredencial);
            JOptionPane.showMessageDialog(this, "Credencial actualizada correctamente");
            listar();
        } catch (DateTimeParseException excepcionFecha) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa: yyyy-mm-dd");
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al actualizar credencial");
            excepcion.printStackTrace();
        }
    }

    private void eliminar() {
        int id = getIdSeleccionado();
        if (id == -1) return;
        try {
            controller.borrarCredencial(id);
            JOptionPane.showMessageDialog(this, "Credencial eliminada correctamente");
            listar();
        } catch (IllegalStateException excepcionEstado) {
            JOptionPane.showMessageDialog(this, excepcionEstado.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al eliminar credencial");
            excepcion.printStackTrace();
        }
    }
}
