package vistas;

import controladores.LectorController;
import controladores.LibroController;
import controladores.PrestamoController;
import modelos.entidades.Lector;
import modelos.entidades.Libro;
import modelos.entidades.Prestamo;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoFrame extends JFrame {

    private final PrestamoController controller = new PrestamoController();
    private final LibroController libroController = new LibroController();
    private final LectorController lectorController = new LectorController();
    private JTextArea textArea;

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

        textArea = new JTextArea();
        textArea.setEditable(false);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(panel);

        btnListar.addActionListener(evento -> listar());
        btnNuevo.addActionListener(evento -> nuevoPrestamo());
        btnDevolucion.addActionListener(evento -> registrarDevolucion());
        btnEliminar.addActionListener(evento -> eliminar());
    }

    private void listar() {
        List<Prestamo> prestamos = controller.listarPrestamos();
        textArea.setText("");
        if (prestamos.isEmpty()) {
            textArea.append("No hay préstamos registrados.\n");
            return;
        }
        for (Prestamo prestamo : prestamos) {
            String nombreLector;
            if (prestamo.getLector() != null) {
                nombreLector = prestamo.getLector().getNombre() + " " + prestamo.getLector().getApellido1();
            } else {
                nombreLector = "-";
            }

            String tituloLibro;
            if (prestamo.getLibro() != null) {
                tituloLibro = prestamo.getLibro().getTitulo();
            } else {
                tituloLibro = "-";
            }

            // Si fecha_fin es null el préstamo está activo; si tiene fecha, ya fue devuelto
            String estadoPrestamo;
            if (prestamo.getFechaFin() == null) {
                estadoPrestamo = "ACTIVO";
            } else {
                estadoPrestamo = "Devuelto el " + prestamo.getFechaFin();
            }

            textArea.append("ID: " + prestamo.getIdPrestamo()
                    + " | Lector: " + nombreLector
                    + " | Libro: " + tituloLibro
                    + " | Inicio: " + prestamo.getFechaInicio()
                    + " | Estado: " + estadoPrestamo + "\n");
        }
    }

    private void nuevoPrestamo() {
        List<Lector> lectores = lectorController.listarLectores();

        // Filtramos solo los libros con estado "disponible"
        // No tiene sentido prestar un libro que ya está prestado
        List<Libro> todosLosLibros = libroController.listarLibros();
        List<Libro> librosDisponibles = new ArrayList<>();
        for (Libro libro : todosLosLibros) {
            if (libro.getEstado().equals("disponible")) {
                librosDisponibles.add(libro);
            }
        }

        if (lectores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay lectores registrados.");
            return;
        }
        if (librosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay libros disponibles para prestar.");
            return;
        }

        String[] opcionesLector = new String[lectores.size()];
        for (int i = 0; i < lectores.size(); i++) {
            Lector lector = lectores.get(i);
            opcionesLector[i] = lector.getIdLector() + " - " + lector.getNombre() + " " + lector.getApellido1();
        }

        String[] opcionesLibro = new String[librosDisponibles.size()];
        for (int i = 0; i < librosDisponibles.size(); i++) {
            Libro libro = librosDisponibles.get(i);
            opcionesLibro[i] = libro.getIdLibro() + " - " + libro.getTitulo();
        }

        Prestamo prestamo = PrestamoDialogs.showInsert(this, lectores, librosDisponibles);
        if (prestamo == null) {
            return;
        }

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
        String input = JOptionPane.showInputDialog(this, "ID del préstamo a devolver:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            Prestamo p = controller.buscarPorId(id);

            if (p == null) {
                JOptionPane.showMessageDialog(this, "Préstamo no encontrado");
                return;
            }

            // Si ya tiene fecha_fin, el libro ya fue devuelto anteriormente
            if (p.getFechaFin() != null) {
                JOptionPane.showMessageDialog(this,
                        "Este préstamo ya fue devuelto el " + p.getFechaFin(),
                        "Ya devuelto", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Ponemos la fecha de hoy como fecha de devolución
            // El controlador actualizará el estado del libro a "disponible"
            p.setFechaFin(LocalDate.now());
            controller.actualizarPrestamo(p);
            JOptionPane.showMessageDialog(this, "Devolución registrada. El libro ahora está 'disponible'.");
            listar();
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al registrar devolución");
            excepcion.printStackTrace();
        }
    }

    private void eliminar() {
        String input = JOptionPane.showInputDialog(this, "ID del préstamo a eliminar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            controller.borrarPrestamo(id);
            JOptionPane.showMessageDialog(this, "Préstamo eliminado correctamente");
            listar();
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al eliminar préstamo");
            excepcion.printStackTrace();
        }
    }
}
