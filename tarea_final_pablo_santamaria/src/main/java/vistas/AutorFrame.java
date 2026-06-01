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
        JButton btnListar    = new JButton("Listar");
        JButton btnInsertar  = new JButton("Insertar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar  = new JButton("Eliminar");

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
        if (autores.isEmpty()) { textArea.append("No hay autores registrados.\n"); return; }
        for (Autor a : autores) {
            textArea.append("ID: " + a.getIdAutor()
                    + " | " + a.getNombre() + " " + a.getApellido1()
                    + (a.getApellido2() != null ? " " + a.getApellido2() : "")
                    + " | Nacionalidad: " + (a.getNacionalidad() != null ? a.getNacionalidad() : "-") + "\n");
        }
    }

    private void insertar() {
        JTextField txtId     = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtAp1    = new JTextField();
        JTextField txtAp2    = new JTextField();
        JTextField txtNac    = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(new JLabel("ID:")); panel.add(txtId);
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Apellido 1:")); panel.add(txtAp1);
        panel.add(new JLabel("Apellido 2 (opcional):")); panel.add(txtAp2);
        panel.add(new JLabel("Nacionalidad:")); panel.add(txtNac);

        int res = JOptionPane.showConfirmDialog(this, panel, "Insertar Autor", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        try {
            Autor a = new Autor();
            a.setIdAutor(Integer.parseInt(txtId.getText().trim()));
            a.setNombre(txtNombre.getText().trim());
            a.setApellido1(txtAp1.getText().trim());
            a.setApellido2(txtAp2.getText().isBlank() ? null : txtAp2.getText().trim());
            a.setNacionalidad(txtNac.getText().isBlank() ? null : txtNac.getText().trim());
            controller.insertarAutor(a);
            JOptionPane.showMessageDialog(this, "Autor insertado correctamente");
            listar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar autor");
            ex.printStackTrace();
        }
    }

    private void actualizar() {
        String input = JOptionPane.showInputDialog(this, "ID del autor a actualizar:");
        if (input == null || input.isBlank()) return;
        try {
            int id = Integer.parseInt(input.trim());
            Autor a = controller.buscarPorId(id);
            if (a == null) { JOptionPane.showMessageDialog(this, "Autor no encontrado"); return; }

            JTextField txtNombre = new JTextField(a.getNombre());
            JTextField txtAp1    = new JTextField(a.getApellido1());
            JTextField txtAp2    = new JTextField(a.getApellido2() != null ? a.getApellido2() : "");
            JTextField txtNac    = new JTextField(a.getNacionalidad() != null ? a.getNacionalidad() : "");

            JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
            panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
            panel.add(new JLabel("Apellido 1:")); panel.add(txtAp1);
            panel.add(new JLabel("Apellido 2:")); panel.add(txtAp2);
            panel.add(new JLabel("Nacionalidad:")); panel.add(txtNac);

            int res = JOptionPane.showConfirmDialog(this, panel, "Actualizar Autor", JOptionPane.OK_CANCEL_OPTION);
            if (res != JOptionPane.OK_OPTION) return;

            a.setNombre(txtNombre.getText().trim());
            a.setApellido1(txtAp1.getText().trim());
            a.setApellido2(txtAp2.getText().isBlank() ? null : txtAp2.getText().trim());
            a.setNacionalidad(txtNac.getText().isBlank() ? null : txtNac.getText().trim());
            controller.actualizarAutor(a);
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
        if (input == null || input.isBlank()) return;
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
