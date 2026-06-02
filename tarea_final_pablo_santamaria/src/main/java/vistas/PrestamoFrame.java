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
import java.time.format.DateTimeParseException;
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

        btnListar.addActionListener(e -> listar());
        btnNuevo.addActionListener(e -> nuevoPrestamo());
        btnDevolucion.addActionListener(e -> registrarDevolucion());
        btnEliminar.addActionListener(e -> eliminar());
    }

    private void listar() {
        List<Prestamo> prestamos = controller.listarPrestamos();
        textArea.setText("");
        if (prestamos.isEmpty()) {
            textArea.append("No hay préstamos registrados.\n");
            return;
        }
        for (Prestamo p : prestamos) {
            String nombreLector;
            if (p.getLector() != null) {
                nombreLector = p.getLector().getNombre() + " " + p.getLector().getApellido1();
            } else {
                nombreLector = "-";
            }

            String tituloLibro;
            if (p.getLibro() != null) {
                tituloLibro = p.getLibro().getTitulo();
            } else {
                tituloLibro = "-";
            }

            // Si fecha_fin es null el préstamo está activo; si tiene fecha, ya fue devuelto
            String estadoPrestamo;
            if (p.getFechaFin() == null) {
                estadoPrestamo = "ACTIVO";
            } else {
                estadoPrestamo = "Devuelto el " + p.getFechaFin();
            }

            textArea.append("ID: " + p.getIdPrestamo()
                    + " | Lector: " + nombreLector
                    + " | Libro: " + tituloLibro
                    + " | Inicio: " + p.getFechaInicio()
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
            Lector l = lectores.get(i);
            opcionesLector[i] = l.getIdLector() + " - " + l.getNombre() + " " + l.getApellido1();
        }

        String[] opcionesLibro = new String[librosDisponibles.size()];
        for (int i = 0; i < librosDisponibles.size(); i++) {
            Libro l = librosDisponibles.get(i);
            opcionesLibro[i] = l.getIdLibro() + " - " + l.getTitulo();
        }

        JTextField txtId    = new JTextField();
        JTextField txtFecha = new JTextField(LocalDate.now().toString()); // Fecha de hoy por defecto
        JComboBox<String> cmbLector = new JComboBox<>(opcionesLector);
        JComboBox<String> cmbLibro  = new JComboBox<>(opcionesLibro);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("ID préstamo:"));               panel.add(txtId);
        panel.add(new JLabel("Fecha inicio (yyyy-mm-dd):")); panel.add(txtFecha);
        panel.add(new JLabel("Lector:"));                    panel.add(cmbLector);
        panel.add(new JLabel("Libro (solo disponibles):"));  panel.add(cmbLibro);

        int resultado = JOptionPane.showConfirmDialog(this, panel, "Nuevo Préstamo", JOptionPane.OK_CANCEL_OPTION);
        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            // Extraemos el ID del lector del texto seleccionado ("1 - Juan Perez" → ID 1)
            String textoLector = (String) cmbLector.getSelectedItem();
            String[] partesLector = textoLector.split(" - ");
            int idLector = Integer.parseInt(partesLector[0]);

            // Extraemos el ID del libro del texto seleccionado ("2 - El Hobbit" → ID 2)
            String textoLibro = (String) cmbLibro.getSelectedItem();
            String[] partesLibro = textoLibro.split(" - ");
            int idLibro = Integer.parseInt(partesLibro[0]);

            Prestamo p = new Prestamo();
            p.setIdPrestamo(Integer.parseInt(txtId.getText().trim()));
            p.setFechaInicio(LocalDate.parse(txtFecha.getText().trim()));
            p.setFechaFin(null); // null = préstamo activo, sin fecha de devolución todavía
            p.setLector(lectorController.buscarPorId(idLector));
            p.setLibro(libroController.buscarPorId(idLibro));

            controller.insertarPrestamo(p);
            JOptionPane.showMessageDialog(this, "Préstamo creado. El libro ahora está marcado como 'prestado'.");
            listar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa: yyyy-mm-dd");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear préstamo");
            ex.printStackTrace();
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
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar devolución");
            e.printStackTrace();
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
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar préstamo");
            e.printStackTrace();
        }
    }
}
