package vistas;

import controladores.BibliotecaController;
import controladores.LectorController;
import modelos.entidades.Biblioteca;
import modelos.entidades.Lector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LectorFrame extends JFrame {

    private final LectorController controller = new LectorController();
    private final BibliotecaController bibliotecaController = new BibliotecaController();
    private JTable tabla;
    private DefaultTableModel modelo;

    public LectorFrame() {
        setTitle("Gestión de Lectores");
        setSize(1000, 500);
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

        String[] columnas = {"ID", "Nombre", "Apellido 1", "Apellido 2", "Email", "Teléfono", "Biblioteca", "Credencial"};
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
        List<Lector> lectores = controller.listar();
        for (Lector lector : lectores) {
            modelo.addRow(new Object[]{
                lector.getIdLector(),
                lector.getNombre(),
                lector.getApellido1(),
                lector.getApellido2() != null ? lector.getApellido2() : "-",
                lector.getEmail() != null ? lector.getEmail() : "-",
                lector.getTelefono() != null ? lector.getTelefono() : "-",
                lector.getBiblioteca() != null ? lector.getBiblioteca().getNombre() : "-",
                lector.getCredencial() != null ? lector.getCredencial().getNumeroTarjeta() : "Sin credencial"
            });
        }
    }

    private int getIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un lector de la tabla primero.");
            return -1;
        }
        return (int) modelo.getValueAt(fila, 0);
    }

    private void insertar() {
        List<Biblioteca> bibliotecas = bibliotecaController.listar();
        Lector lector = LectorDialogs.showInsert(this, bibliotecas);
        if (lector == null) return;
        try {
            controller.insertar(lector);
            JOptionPane.showMessageDialog(this, "Lector insertado correctamente");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al insertar lector");
            excepcion.printStackTrace();
        }
    }

    private void actualizar() {
        int id = getIdSeleccionado();
        if (id == -1) return;
        try {
            Lector lector = controller.buscarPorId(id);
            if (lector == null) {
                JOptionPane.showMessageDialog(this, "Lector no encontrado");
                return;
            }
            List<Biblioteca> bibliotecas = bibliotecaController.listar();
            Lector updated = LectorDialogs.showUpdate(this, lector, bibliotecas);
            if (updated == null) return;
            controller.actualizar(updated);
            JOptionPane.showMessageDialog(this, "Lector actualizado correctamente");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al actualizar lector");
            excepcion.printStackTrace();
        }
    }

    private void eliminar() {
        int id = getIdSeleccionado();
        if (id == -1) return;
        try {
            controller.borrarLector(id);
            JOptionPane.showMessageDialog(this, "Lector eliminado correctamente");
            listar();
        } catch (IllegalStateException excepcionEstado) {
            JOptionPane.showMessageDialog(this, excepcionEstado.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al eliminar lector");
            excepcion.printStackTrace();
        }
    }
}
