package vistas;

import controladores.BibliotecaController;
import modelos.entidades.Biblioteca;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BibliotecaFrame extends JFrame {

    private final BibliotecaController controller = new BibliotecaController();
    private JTextArea textArea;

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
        List<Biblioteca> bibliotecas = controller.listarBibliotecas();
        textArea.setText("");
        if (bibliotecas.isEmpty()) {
            textArea.append("No hay bibliotecas registradas.\n");
            return;
        }
        for (Biblioteca b : bibliotecas) {
            String direccion = "-";
            if (b.getDireccion() != null) direccion = b.getDireccion();
            textArea.append("ID: " + b.getIdBiblioteca()
                    + " | " + b.getNombre()
                    + " | Dirección: " + direccion + "\n");
        }
    }

    private void insertar() {
        Biblioteca b = BibliotecaDialogs.showInsert(this);
        if (b == null) return;
        try {
            controller.insertarBiblioteca(b);
            JOptionPane.showMessageDialog(this, "Biblioteca insertada correctamente");
            listar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar biblioteca");
            ex.printStackTrace();
        }
    }

    private void actualizar() {
        String input = JOptionPane.showInputDialog(this, "ID de la biblioteca a actualizar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            Biblioteca b = controller.buscarPorId(id);
            if (b == null) {
                JOptionPane.showMessageDialog(this, "Biblioteca no encontrada");
                return;
            }
            Biblioteca updated = BibliotecaDialogs.showUpdate(this, b);
            if (updated == null) return;
            controller.actualizarBiblioteca(updated);
            JOptionPane.showMessageDialog(this, "Biblioteca actualizada correctamente");
            listar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar biblioteca");
            ex.printStackTrace();
        }
    }

    private void eliminar() {
        String input = JOptionPane.showInputDialog(this, "ID de la biblioteca a eliminar:");
        if (input == null || input.isBlank()) {
            return;
        }
        try {
            int id = Integer.parseInt(input.trim());
            controller.borrarBiblioteca(id);
            JOptionPane.showMessageDialog(this, "Biblioteca eliminada correctamente");
            listar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar biblioteca");
            e.printStackTrace();
        }
    }
}
