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
        for (Biblioteca biblioteca : bibliotecas) {
            String direccion = "-";
            if (biblioteca.getDireccion() != null) {
                direccion = biblioteca.getDireccion();
            }

            textArea.append("ID: " + biblioteca.getIdBiblioteca()
                    + " | " + biblioteca.getNombre()
                    + " | Dirección: " + direccion + "\n");
        }
    }

    private void insertar() {
        Biblioteca biblioteca = BibliotecaDialogs.showInsert(this);
        if (biblioteca == null) {
            return;
        }

        try {
            controller.insertarBiblioteca(biblioteca);
            JOptionPane.showMessageDialog(this, "Biblioteca insertada correctamente");
            listar();
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al insertar biblioteca");
            excepcion.printStackTrace();
        }
    }

    private void actualizar() {
        String input = JOptionPane.showInputDialog(this, "ID de la biblioteca a actualizar:");
        if (input == null || input.isBlank()) {
            return;
        }

        try {
            int id = Integer.parseInt(input.trim());
            Biblioteca biblioteca = controller.buscarPorId(id);

            if (biblioteca == null) {
                JOptionPane.showMessageDialog(this, "Biblioteca no encontrada");
                return;
            }

            Biblioteca updatedBiblioteca = BibliotecaDialogs.showUpdate(this, biblioteca);
            if (updatedBiblioteca == null) {
                return;
            }

            controller.actualizarBiblioteca(updatedBiblioteca);
            JOptionPane.showMessageDialog(this, "Biblioteca actualizada correctamente");
            listar();
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al actualizar biblioteca");
            excepcion.printStackTrace();
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
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (IllegalStateException excepcionEstado) {
            JOptionPane.showMessageDialog(this, excepcionEstado.getMessage(), "No se puede eliminar", JOptionPane.WARNING_MESSAGE);
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(this, "Error al eliminar biblioteca");
            excepcion.printStackTrace();
        }
    }
}
