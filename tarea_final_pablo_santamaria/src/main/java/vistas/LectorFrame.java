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
        List<Lector> lectores = controller.listarLectores();
        textArea.setText("");
        if (lectores.isEmpty()) { textArea.append("No hay lectores registrados.\n"); return; }
        for (Lector l : lectores) {
            String bib = l.getBiblioteca() != null ? l.getBiblioteca().getNombre() : "-";
            textArea.append("ID: " + l.getIdLector()
                    + " | " + l.getNombre() + " " + l.getApellido1()
                    + (l.getApellido2() != null ? " " + l.getApellido2() : "")
                    + " | Email: " + (l.getEmail() != null ? l.getEmail() : "-")
                    + " | Tel: " + (l.getTelefono() != null ? l.getTelefono() : "-")
                    + " | Biblioteca: " + bib + "\n");
        }
    }

    private void insertar() {
        List<Biblioteca> bibliotecas = bibliotecaController.listarBibliotecas();
        if (bibliotecas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay bibliotecas disponibles. Inserta una primero.");
            return;
        }
        JTextField txtId     = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtAp1    = new JTextField();
        JTextField txtAp2    = new JTextField();
        JTextField txtEmail  = new JTextField();
        JTextField txtTel    = new JTextField();
        String[] opBib = bibliotecas.stream()
                .map(b -> b.getIdBiblioteca() + " - " + b.getNombre())
                .toArray(String[]::new);
        JComboBox<String> cmbBib = new JComboBox<>(opBib);

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.add(new JLabel("ID:")); panel.add(txtId);
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Apellido 1:")); panel.add(txtAp1);
        panel.add(new JLabel("Apellido 2 (opcional):")); panel.add(txtAp2);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Teléfono:")); panel.add(txtTel);
        panel.add(new JLabel("Biblioteca:")); panel.add(cmbBib);

        int res = JOptionPane.showConfirmDialog(this, panel, "Insertar Lector", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        try {
            int idBib = Integer.parseInt(((String) cmbBib.getSelectedItem()).split(" - ")[0]);
            Lector l = new Lector();
            l.setIdLector(Integer.parseInt(txtId.getText().trim()));
            l.setNombre(txtNombre.getText().trim());
            l.setApellido1(txtAp1.getText().trim());
            l.setApellido2(txtAp2.getText().isBlank() ? null : txtAp2.getText().trim());
            l.setEmail(txtEmail.getText().isBlank() ? null : txtEmail.getText().trim());
            l.setTelefono(txtTel.getText().isBlank() ? null : txtTel.getText().trim());
            l.setBiblioteca(bibliotecaController.buscarPorId(idBib));
            controller.insertarLector(l);
            JOptionPane.showMessageDialog(this, "Lector insertado correctamente");
            listar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar lector");
            ex.printStackTrace();
        }
    }

    private void actualizar() {
        String input = JOptionPane.showInputDialog(this, "ID del lector a actualizar:");
        if (input == null || input.isBlank()) return;
        try {
            int id = Integer.parseInt(input.trim());
            Lector l = controller.buscarPorId(id);
            if (l == null) { JOptionPane.showMessageDialog(this, "Lector no encontrado"); return; }

            List<Biblioteca> bibliotecas = bibliotecaController.listarBibliotecas();
            String[] opBib = bibliotecas.stream()
                    .map(b -> b.getIdBiblioteca() + " - " + b.getNombre())
                    .toArray(String[]::new);
            JComboBox<String> cmbBib = new JComboBox<>(opBib);
            if (l.getBiblioteca() != null) {
                for (int i = 0; i < cmbBib.getItemCount(); i++) {
                    if (((String) cmbBib.getItemAt(i)).startsWith(l.getBiblioteca().getIdBiblioteca() + " - ")) {
                        cmbBib.setSelectedIndex(i); break;
                    }
                }
            }

            JTextField txtNombre = new JTextField(l.getNombre());
            JTextField txtAp1    = new JTextField(l.getApellido1());
            JTextField txtAp2    = new JTextField(l.getApellido2() != null ? l.getApellido2() : "");
            JTextField txtEmail  = new JTextField(l.getEmail() != null ? l.getEmail() : "");
            JTextField txtTel    = new JTextField(l.getTelefono() != null ? l.getTelefono() : "");

            JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
            panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
            panel.add(new JLabel("Apellido 1:")); panel.add(txtAp1);
            panel.add(new JLabel("Apellido 2:")); panel.add(txtAp2);
            panel.add(new JLabel("Email:")); panel.add(txtEmail);
            panel.add(new JLabel("Teléfono:")); panel.add(txtTel);
            panel.add(new JLabel("Biblioteca:")); panel.add(cmbBib);

            int res = JOptionPane.showConfirmDialog(this, panel, "Actualizar Lector", JOptionPane.OK_CANCEL_OPTION);
            if (res != JOptionPane.OK_OPTION) return;

            int idBib = Integer.parseInt(((String) cmbBib.getSelectedItem()).split(" - ")[0]);
            l.setNombre(txtNombre.getText().trim());
            l.setApellido1(txtAp1.getText().trim());
            l.setApellido2(txtAp2.getText().isBlank() ? null : txtAp2.getText().trim());
            l.setEmail(txtEmail.getText().isBlank() ? null : txtEmail.getText().trim());
            l.setTelefono(txtTel.getText().isBlank() ? null : txtTel.getText().trim());
            l.setBiblioteca(bibliotecaController.buscarPorId(idBib));
            controller.actualizarLector(l);
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
        if (input == null || input.isBlank()) return;
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
