package vistas;

import controladores.LectorController;
import controladores.LibroController;
import controladores.PrestamoController;
import modelos.entidades.Lector;
import modelos.entidades.Libro;
import modelos.entidades.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoFrame extends JFrame {

    private final PrestamoController controller = new PrestamoController();
    private final LibroController libroController = new LibroController();
    private final LectorController lectorController = new LectorController();
    private JTable tabla;
    private DefaultTableModel modelo;

    public PrestamoFrame() {
        setTitle("Gestión de Préstamos");
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
        JButton btnNuevo      = new JButton("Nuevo Préstamo");
        JButton btnDevolucion = new JButton("Registrar Devolución");
        JButton btnEliminar   = new JButton("Eliminar");

        botones.add(btnListar);
        botones.add(btnNuevo);
        botones.add(btnDevolucion);
        botones.add(btnEliminar);
        panel.add(botones, BorderLayout.NORTH);

        String[] columnas = {"ID", "Lector", "Libro", "Fecha Inicio", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panel);

        btnListar.addActionListener(evento -> listar());
        btnNuevo.addActionListener(evento -> nuevoPrestamo());
        btnDevolucion.addActionListener(evento -> registrarDevolucion());
        btnEliminar.addActionListener(evento -> eliminar());
    }

    private void listar() {
        modelo.setRowCount(0);
        List<Prestamo> prestamos = controller.listar();
        for (Prestamo prestamo : prestamos) {
            String lector = prestamo.getLector() != null
                    ? prestamo.getLector().getNombre() + " " + prestamo.getLector().getApellido1()
                    : "-";
            String libro = prestamo.getLibro() != null
                    ? prestamo.getLibro().getTitulo()
                    : "(libro eliminado)";
            String estado = prestamo.getFechaFin() == null
                    ? "ACTIVO"
                    : "Devuelto el " + prestamo.getFechaFin();
            modelo.addRow(new Object[]{
                prestamo.getIdPrestamo(),
                lector,
                libro,
                prestamo.getFechaInicio(),
                estado
            });
        }
    }

    private int getIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un préstamo de la tabla primero.");
            return -1;
        }
        return (int) modelo.getValueAt(fila, 0);
    }

    private void nuevoPrestamo() {
        List<Lector> todosLosLectores = lectorController.listar();
        List<Lector> lectores = new ArrayList<>();
        for (Lector lector : todosLosLectores) {
            if (lector.getCredencial() != null) {
                lectores.add(lector);
            }
        }

        List<Libro> todosLosLibros = libroController.listar();
        List<Libro> librosDisponibles = new ArrayList<>();
        for (Libro libro : todosLosLibros) {
            if (libro.getEstado().equals("disponible")) {
                librosDisponibles.add(libro);
            }
        }

        if (lectores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay lectores con credencial. Asigna una credencial antes de crear un préstamo.", "Sin credencial", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (librosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay libros disponibles para prestar.");
            return;
        }

        Prestamo prestamo = PrestamoDialogs.showInsert(this, lectores, librosDisponibles);
        if (prestamo == null) return;

        try {
            controller.insertarPrestamo(prestamo);
            JOptionPane.showMessageDialog(this, "Préstamo creado. El libro ahora está marcado como 'prestado'.");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al crear préstamo");
            excepcion.printStackTrace();
        }
    }

    private void registrarDevolucion() {
        int id = getIdSeleccionado();
        if (id == -1) return;
        try {
            Prestamo p = controller.buscarPorId(id);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Préstamo no encontrado");
                return;
            }
            if (p.getFechaFin() != null) {
                JOptionPane.showMessageDialog(this,
                        "Este préstamo ya fue devuelto el " + p.getFechaFin(),
                        "Ya devuelto", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            p.setFechaFin(LocalDate.now());
            controller.actualizarPrestamo(p);
            JOptionPane.showMessageDialog(this, "Devolución registrada. El libro ahora está 'disponible'.");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al registrar devolución");
            excepcion.printStackTrace();
        }
    }

    private void eliminar() {
        int id = getIdSeleccionado();
        if (id == -1) return;
        try {
            controller.borrarPrestamo(id);
            JOptionPane.showMessageDialog(this, "Préstamo eliminado correctamente");
            listar();
        } catch (IllegalStateException excepcionEstado) {
            JOptionPane.showMessageDialog(this, excepcionEstado.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al eliminar préstamo");
            excepcion.printStackTrace();
        }
    }
}
