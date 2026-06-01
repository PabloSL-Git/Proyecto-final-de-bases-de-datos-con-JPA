package vistas;

import controladores.LibroController;
import modelos.entidades.Libro;

import javax.swing.*;
import java.awt.*;

public class LibroDialog extends JDialog {

    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextField txtAnio;
    private JTextField txtEstado;

    private LibroController controller = new LibroController();

    public LibroDialog(Frame parent) {
        super(parent, "Insertar Libro", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("ID:"));
        txtId = new JTextField();
        add(txtId);

        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("Año publicación:"));
        txtAnio = new JTextField();
        add(txtAnio);

        add(new JLabel("Estado:"));
        txtEstado = new JTextField();
        add(txtEstado);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        add(btnGuardar);
        add(btnCancelar);

        btnGuardar.addActionListener(e -> insertarLibro());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void insertarLibro() {
        try {
            Libro libro = new Libro();
            libro.setIdLibro(Integer.parseInt(txtId.getText().trim()));
            libro.setTitulo(txtTitulo.getText().trim());
            libro.setAnioPublicacion(Integer.parseInt(txtAnio.getText().trim()));
            libro.setEstado(txtEstado.getText().trim());

            controller.insertarLibro(libro);

            JOptionPane.showMessageDialog(this, "Libro insertado correctamente");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID y Año deben ser números enteros");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al insertar libro");
            e.printStackTrace();
        }
    }
}
