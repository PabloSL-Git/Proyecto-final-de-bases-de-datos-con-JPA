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
import java.util.List;
import java.util.stream.Collectors;

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
        JButton btnListar      = new JButton("Listar");
        JButton btnNuevo       = new JButton("Nuevo Préstamo");
        JButton btnDevolucion  = new JButton("Registrar Devolución");
        JButton btnEliminar    = new JButton("Eliminar");

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
        if (prestamos.isEmpty()) { textArea.append("No hay préstamos registrados.\n"); return; }
        for (Prestamo p : prestamos) {
            String lector = p.getLector() != null
                    ? p.getLector().getNombre() + " " + p.getLector().getApellido1() : "-";
            String libro = p.getLibro() != null ? p.getLibro().getTitulo() : "-";
            String estado = p.getFechaFin() == null ? "ACTIVO" : "Devuelto " + p.getFechaFin();
            textArea.append("ID: " + p.getIdPrestamo()
                    + " | Lector: " + lector
                    + " | Libro: " + libro
                    + " | Inicio: " + p.getFechaInicio()
                    + " | " + estado + "\n");
        }
    }

    private void nuevoPrestamo() {
        List<Lector> lectores = lectorController.listarLectores();
        List<Libro> disponibles = libroController.listarLibros().stream()
                .filter(l -> "disponible".equals(l.getEstado()))
                .collect(Collectors.toList());

        if (lectores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay lectores registrados."); return;
        }
        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay libros disponibles para prestar."); return;
        }

        JTextField txtId    = new JTextField();
        JTextField txtFecha = new JTextField(LocalDate.now().toString());
        String[] opLect = lectores.stream()
                .map(l -> l.getIdLector() + " - " + l.getNombre() + " " + l.getApellido1())
                .toArray(String[]::new);
        String[] opLibro = disponibles.stream()
                .map(l -> l.getIdLibro() + " - " + l.getTitulo())
                .toArray(String[]::new);
        JComboBox<String> cmbLect  = new JComboBox<>(opLect);
        JComboBox<String> cmbLibro = new JComboBox<>(opLibro);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("ID préstamo:")); panel.add(txtId);
        panel.add(new JLabel("Fecha inicio (yyyy-mm-dd):")); panel.add(txtFecha);
        panel.add(new JLabel("Lector:")); panel.add(cmbLect);
        panel.add(new JLabel("Libro (disponibles):")); panel.add(cmbLibro);

        int res = JOptionPane.showConfirmDialog(this, panel, "Nuevo Préstamo", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        try {
            int idLect  = Integer.parseInt(((String) cmbLect.getSelectedItem()).split(" - ")[0]);
            int idLibro = Integer.parseInt(((String) cmbLibro.getSelectedItem()).split(" - ")[0]);
            Prestamo p = new Prestamo();
            p.setIdPrestamo(Integer.parseInt(txtId.getText().trim()));
            p.setFechaInicio(LocalDate.parse(txtFecha.getText().trim()));
            p.setLector(lectorController.buscarPorId(idLect));
            p.setLibro(libroController.buscarPorId(idLibro));
            controller.insertarPrestamo(p);
            JOptionPane.showMessageDialog(this, "Préstamo creado. El libro ahora está marcado como 'prestado'.");
            listar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido (usa yyyy-mm-dd)");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear préstamo");
            ex.printStackTrace();
        }
    }

    private void registrarDevolucion() {
        String input = JOptionPane.showInputDialog(this, "ID del préstamo a devolver:");
        if (input == null || input.isBlank()) return;
        try {
            int id = Integer.parseInt(input.trim());
            Prestamo p = controller.buscarPorId(id);
            if (p == null) { JOptionPane.showMessageDialog(this, "Préstamo no encontrado"); return; }
            if (p.getFechaFin() != null) {
                JOptionPane.showMessageDialog(this, "Este préstamo ya fue devuelto el " + p.getFechaFin(),
                        "Ya devuelto", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
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
        if (input == null || input.isBlank()) return;
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
