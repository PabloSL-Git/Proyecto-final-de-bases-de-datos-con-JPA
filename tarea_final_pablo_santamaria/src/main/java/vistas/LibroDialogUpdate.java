package vistas;

import controladores.LibroController;
import modelos.entidades.Libro;

import javax.swing.*;
import java.awt.*;

public class LibroDialogUpdate extends JDialog {

    private JTextField txtTitulo;
    private JTextField txtAnio;
    private JTextField txtEstado;

    private LibroController controller = new LibroController();
    private Libro libro;

    public LibroDialogUpdate(Frame parent, Libro libro) {
        super(parent, "Actualizar Libro", true);
        this.libro = libro;
        setSize(400, 250);
        setLocationRelativeTo(parent);
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("Año:"));
        txtAnio = new JTextField();
        add(txtAnio);

        add(new JLabel("Estado:"));
        txtEstado = new JTextField();
        add(txtEstado);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        add(btnGuardar);
        add(btnCancelar);

        btnGuardar.addActionListener(e -> actualizarLibro());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void cargarDatos() {
        txtTitulo.setText(libro.getTitulo());
        txtAnio.setText(String.valueOf(libro.getAnioPublicacion()));
        txtEstado.setText(libro.getEstado());
    }

    private void actualizarLibro() {
        try {
            libro.setTitulo(txtTitulo.getText().trim());
            libro.setAnioPublicacion(Integer.parseInt(txtAnio.getText().trim()));
            libro.setEstado(txtEstado.getText().trim());

            controller.actualizarLibro(libro);

            JOptionPane.showMessageDialog(this, "Libro actualizado correctamente");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El año debe ser un número entero");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar libro");
            e.printStackTrace();
        }
    }
}
