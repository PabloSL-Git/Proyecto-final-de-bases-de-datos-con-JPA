package vistas;

import controladores.BibliotecaController;
import controladores.LectorController;
import modelos.entidades.Biblioteca;
import modelos.entidades.Lector;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LectorFrame extends JFrame {

    private final LectorController controller = new LectorController();
    private final BibliotecaController bibliotecaController = new BibliotecaController();
    private JTextArea textArea;

    public LectorFrame() {
        setTitle("Gestión de Lectores");
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
        List<Lector> lectores = controller.listarLectores();
        textArea.setText("");
        if (lectores.isEmpty()) {
            textArea.append("No hay lectores registrados.\n");
            return;
        }
        for (Lector l : lectores) {
            String apellido2;
            if (l.getApellido2() != null) {
                apellido2 = " " + l.getApellido2();
            } else {
                apellido2 = "";
            }

            String email;
            if (l.getEmail() != null) {
                email = l.getEmail();
            } else {
                email = "-";
            }

            String telefono;
            if (l.getTelefono() != null) {
                telefono = l.getTelefono();
            } else {
                telefono = "-";
            }

            String nombreBiblioteca;
            if (l.getBiblioteca() != null) {
                nombreBiblioteca = l.getBiblioteca().getNombre();
            } else {
                nombreBiblioteca = "-";
            }

            textArea.append("ID: " + l.getIdLector()
                    + " | " + l.getNombre() + " " + l.getApellido1() + apellido2
                    + " | Email: " + email
                    + " | Tel: " + telefono
                    + " | Biblioteca: " + nombreBiblioteca + "\n");
        }
    }

    private void insertar() {
        List<Biblioteca> bibliotecas = bibliotecaController.listarBibliotecas();
        Lector l = LectorDialogs.showInsert(this, bibliotecas);
        if (l == null) return;
        try {
            controller.insertarLector(l);
            JOptionPane.showMessageDialog(this, "Lector insertado correctamente");
            listar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar lector");
            ex.printStackTrace();
        }
    }

    private void actualizar() {
        String input = JOptionPane.showInputDialog(this, "ID del lector a actualizar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            Lector l = controller.buscarPorId(id);
            if (l == null) {
                JOptionPane.showMessageDialog(this, "Lector no encontrado");
                return;
            }

            List<Biblioteca> bibliotecas = bibliotecaController.listarBibliotecas();
            Lector updated = LectorDialogs.showUpdate(this, l, bibliotecas);
            if (updated == null) return;
            controller.actualizarLector(updated);
            JOptionPane.showMessageDialog(this, "Lector actualizado correctamente");
            listar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar lector");
            ex.printStackTrace();
        }
    }

    private void eliminar() {
        String input = JOptionPane.showInputDialog(this, "ID del lector a eliminar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            controller.borrarLector(id);
            JOptionPane.showMessageDialog(this, "Lector eliminado correctamente");
            listar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar lector");
            e.printStackTrace();
        }
    }
}
