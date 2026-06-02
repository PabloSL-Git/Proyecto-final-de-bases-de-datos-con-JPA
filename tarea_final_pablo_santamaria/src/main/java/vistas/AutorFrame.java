package vistas;

import controladores.AutorController;
import modelos.entidades.Autor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AutorFrame extends JFrame {

    private final AutorController controller = new AutorController();
    private JTextArea textArea;

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
        List<Autor> autores = controller.listarAutores();
        textArea.setText("");
        if (autores.isEmpty()) {
            textArea.append("No hay autores registrados.\n");
            return;
        }
        for (Autor a : autores) {
            // apellido2 y nacionalidad son opcionales, pueden ser null
            String apellido2;
            if (a.getApellido2() != null) {
                apellido2 = " " + a.getApellido2();
            } else {
                apellido2 = "";
            }

            String nacionalidad;
            if (a.getNacionalidad() != null) {
                nacionalidad = a.getNacionalidad();
            } else {
                nacionalidad = "-";
            }

            textArea.append("ID: " + a.getIdAutor()
                    + " | " + a.getNombre() + " " + a.getApellido1() + apellido2
                    + " | Nacionalidad: " + nacionalidad + "\n");
        }
    }

    private void insertar() {
        Autor a = AutorDialogs.showInsert(this);
        if (a == null) return;
        try {
            controller.insertarAutor(a);
            JOptionPane.showMessageDialog(this, "Autor insertado correctamente");
            listar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar autor");
            ex.printStackTrace();
        }
    }

    private void actualizar() {
        String input = JOptionPane.showInputDialog(this, "ID del autor a actualizar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            Autor a = controller.buscarPorId(id);
            if (a == null) {
                JOptionPane.showMessageDialog(this, "Autor no encontrado");
                return;
            }

            // Pre-rellenamos con los valores actuales (null lo tratamos como cadena vacía)
            Autor updated = AutorDialogs.showUpdate(this, a);
            if (updated == null) return;
            controller.actualizarAutor(updated);
            JOptionPane.showMessageDialog(this, "Autor actualizado correctamente");
            listar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar autor");
            ex.printStackTrace();
        }
    }

    private void eliminar() {
        String input = JOptionPane.showInputDialog(this, "ID del autor a eliminar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            controller.borrarAutor(id);
            JOptionPane.showMessageDialog(this, "Autor eliminado correctamente");
            listar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar autor");
            e.printStackTrace();
        }
    }
}
