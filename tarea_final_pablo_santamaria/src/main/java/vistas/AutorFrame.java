package vistas;

import controladores.AutorController;
import modelos.entidades.Autor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AutorFrame extends JFrame {

    private final AutorController controller = new AutorController();
    private JTable tabla;
    private DefaultTableModel modelo;

    public AutorFrame() {
        setTitle("Gestión de Autores");
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

        String[] columnas = {"ID", "Nombre", "Apellido 1", "Apellido 2", "Nacionalidad"};
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
        List<Autor> autores = controller.listar();
        for (Autor autor : autores) {
            modelo.addRow(new Object[]{
                autor.getIdAutor(),
                autor.getNombre(),
                autor.getApellido1(),
                autor.getApellido2() != null ? autor.getApellido2() : "-",
                autor.getNacionalidad() != null ? autor.getNacionalidad() : "-"
            });
        }
    }

    private int getIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un autor de la tabla primero.");
            return -1;
        }
        return (int) modelo.getValueAt(fila, 0);
    }

    private void insertar() {
        Autor autor = AutorDialogs.showInsert(this);
        if (autor == null) return;
        try {
            controller.insertar(autor);
            JOptionPane.showMessageDialog(this, "Autor insertado correctamente");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al insertar autor");
            excepcion.printStackTrace();
        }
    }

    private void actualizar() {
        int id = getIdSeleccionado();
        if (id == -1) return;
        try {
            Autor autor = controller.buscarPorId(id);
            if (autor == null) {
                JOptionPane.showMessageDialog(this, "Autor no encontrado");
                return;
            }
            Autor autorActualizado = AutorDialogs.showUpdate(this, autor);
            if (autorActualizado == null) return;
            controller.actualizar(autorActualizado);
            JOptionPane.showMessageDialog(this, "Autor actualizado correctamente");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al actualizar autor");
            excepcion.printStackTrace();
        }
    }

    private void eliminar() {
        int id = getIdSeleccionado();
        if (id == -1) return;
        try {
            controller.borrarAutor(id);
            JOptionPane.showMessageDialog(this, "Autor eliminado correctamente");
            listar();
        } catch (IllegalStateException excepcionEstado) {
            JOptionPane.showMessageDialog(this, excepcionEstado.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al eliminar autor");
            excepcion.printStackTrace();
        }
    }
}
