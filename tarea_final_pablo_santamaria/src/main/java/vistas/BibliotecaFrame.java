package vistas;

import controladores.BibliotecaController;
import modelos.entidades.Biblioteca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BibliotecaFrame extends JFrame {

    private final BibliotecaController controller = new BibliotecaController();
    private JTable tabla;
    private DefaultTableModel modelo;

    public BibliotecaFrame() {
        setTitle("Gestión de Bibliotecas");
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

        String[] columnas = {"ID", "Nombre", "Dirección"};
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
        List<Biblioteca> bibliotecas = controller.listar();
        for (Biblioteca biblioteca : bibliotecas) {
            modelo.addRow(new Object[]{
                biblioteca.getIdBiblioteca(),
                biblioteca.getNombre(),
                biblioteca.getDireccion() != null ? biblioteca.getDireccion() : "-"
            });
        }
    }

    private int getIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una biblioteca de la tabla primero.");
            return -1;
        }
        return (int) modelo.getValueAt(fila, 0);
    }

    private void insertar() {
        Biblioteca biblioteca = BibliotecaDialogs.showInsert(this);
        if (biblioteca == null) return;
        try {
            controller.insertar(biblioteca);
            JOptionPane.showMessageDialog(this, "Biblioteca insertada correctamente");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al insertar biblioteca");
            excepcion.printStackTrace();
        }
    }

    private void actualizar() {
        int id = getIdSeleccionado();
        if (id == -1) return;
        try {
            Biblioteca biblioteca = controller.buscarPorId(id);
            if (biblioteca == null) {
                JOptionPane.showMessageDialog(this, "Biblioteca no encontrada");
                return;
            }
            Biblioteca updatedBiblioteca = BibliotecaDialogs.showUpdate(this, biblioteca);
            if (updatedBiblioteca == null) return;
            controller.actualizar(updatedBiblioteca);
            JOptionPane.showMessageDialog(this, "Biblioteca actualizada correctamente");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al actualizar biblioteca");
            excepcion.printStackTrace();
        }
    }

    private void eliminar() {
        int id = getIdSeleccionado();
        if (id == -1) return;
        try {
            controller.borrarBiblioteca(id);
            JOptionPane.showMessageDialog(this, "Biblioteca eliminada correctamente");
            listar();
        } catch (IllegalStateException excepcionEstado) {
            JOptionPane.showMessageDialog(this, excepcionEstado.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al eliminar biblioteca");
            excepcion.printStackTrace();
        }
    }
}
