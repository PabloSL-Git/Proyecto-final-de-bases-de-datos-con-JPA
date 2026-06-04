package vistas;

import controladores.LibroController;
import modelos.entidades.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LibroFrame extends JFrame {

    private final LibroController controller = new LibroController();
    private JTable tabla;
    private DefaultTableModel modelo;

    public LibroFrame() {
        setTitle("Gestión de Libros");
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

        String[] columnas = {"ID", "Título", "Año", "Estado", "Autor", "Biblioteca"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panel);

        btnListar.addActionListener(evento -> listar());
        btnInsertar.addActionListener(evento -> abrirInsertar());
        btnActualizar.addActionListener(evento -> abrirActualizar());
        btnEliminar.addActionListener(evento -> eliminar());
    }

    private void listar() {
        modelo.setRowCount(0);
        List<Libro> libros = controller.listar();
        for (Libro libro : libros) {
            String autor = libro.getAutor() != null
                    ? libro.getAutor().getNombre() + " " + libro.getAutor().getApellido1()
                    : "-";
            String biblioteca = libro.getBiblioteca() != null
                    ? libro.getBiblioteca().getNombre()
                    : "-";
            modelo.addRow(new Object[]{
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAnioPublicacion(),
                libro.getEstado(),
                autor,
                biblioteca
            });
        }
    }

    private int getIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro de la tabla primero.");
            return -1;
        }
        return (int) modelo.getValueAt(fila, 0);
    }

    private void abrirInsertar() {
        Libro libro = LibroDialogs.showInsert(this);
        if (libro == null) return;
        try {
            controller.insertar(libro);
            JOptionPane.showMessageDialog(this, "Libro insertado correctamente");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al insertar libro");
            excepcion.printStackTrace();
        }
    }

    private void abrirActualizar() {
        int id = getIdSeleccionado();
        if (id == -1) return;
        try {
            Libro libro = controller.buscarPorId(id);
            if (libro == null) {
                JOptionPane.showMessageDialog(this, "Libro no encontrado");
                return;
            }
            Libro libroActualizado = LibroDialogs.showUpdate(this, libro);
            if (libroActualizado == null) return;
            controller.actualizar(libroActualizado);
            JOptionPane.showMessageDialog(this, "Libro actualizado correctamente");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al actualizar libro");
            excepcion.printStackTrace();
        }
    }

    private void eliminar() {
        int id = getIdSeleccionado();
        if (id == -1) return;
        try {
            controller.borrarLibro(id);
            JOptionPane.showMessageDialog(this, "Libro eliminado correctamente");
            listar();
        } catch (IllegalStateException excepcionEstado) {
            JOptionPane.showMessageDialog(this, excepcionEstado.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al eliminar libro");
            excepcion.printStackTrace();
        }
    }
}
