package vistas;

import controladores.AutorController;
import controladores.BibliotecaController;
import controladores.LibroController;
import modelos.entidades.Autor;
import modelos.entidades.Biblioteca;
import modelos.entidades.Libro;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Diálogo exclusivo para ACTUALIZAR un libro existente
// Se abre desde LibroFrame al pulsar "Actualizar", recibe el Libro que se va a editar
public class ActualizarLibroDialog extends JDialog {

    private JTextField txtTitulo;
    private JTextField txtAnio;
    private JComboBox<String> cmbEstado;
    private JComboBox<String> cmbAutor;
    private JComboBox<String> cmbBiblioteca;

    private final LibroController libroController = new LibroController();
    private final AutorController autorController = new AutorController();
    private final BibliotecaController bibliotecaController = new BibliotecaController();

    private final Libro libro; // El libro que estamos editando
    private List<Autor> autores;
    private List<Biblioteca> bibliotecas;

    public ActualizarLibroDialog(Frame parent, Libro libro) {
        super(parent, "Actualizar Libro", true);
        this.libro = libro;
        setSize(420, 320);
        setLocationRelativeTo(parent);
        initComponents();
        cargarDatosActuales(); // Rellenamos los campos con los valores actuales
    }

    private void initComponents() {
        autores = autorController.listarAutores();
        bibliotecas = bibliotecaController.listarBibliotecas();

        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel("Año:"));
        txtAnio = new JTextField();
        add(txtAnio);

        add(new JLabel("Estado:"));
        cmbEstado = new JComboBox<>(new String[]{"disponible", "prestado"});
        add(cmbEstado);

        String[] opcionesAutor = new String[autores.size()];
        for (int i = 0; i < autores.size(); i++) {
            Autor a = autores.get(i);
            opcionesAutor[i] = a.getIdAutor() + " - " + a.getNombre() + " " + a.getApellido1();
        }
        add(new JLabel("Autor:"));
        cmbAutor = new JComboBox<>(opcionesAutor);
        add(cmbAutor);

        String[] opcionesBiblioteca = new String[bibliotecas.size()];
        for (int i = 0; i < bibliotecas.size(); i++) {
            Biblioteca b = bibliotecas.get(i);
            opcionesBiblioteca[i] = b.getIdBiblioteca() + " - " + b.getNombre();
        }
        add(new JLabel("Biblioteca:"));
        cmbBiblioteca = new JComboBox<>(opcionesBiblioteca);
        add(cmbBiblioteca);

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        add(btnGuardar);
        add(btnCancelar);

        btnGuardar.addActionListener(e -> actualizar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void cargarDatosActuales() {
        // Rellenamos cada campo con el valor actual del libro
        txtTitulo.setText(libro.getTitulo());
        txtAnio.setText(String.valueOf(libro.getAnioPublicacion()));
        cmbEstado.setSelectedItem(libro.getEstado());

        // Pre-seleccionamos el autor actual en el desplegable
        // Las opciones tienen formato "id - nombre", buscamos la que empiece con el ID del autor actual
        if (libro.getAutor() != null) {
            String prefijoBuscado = libro.getAutor().getIdAutor() + " - ";
            for (int i = 0; i < cmbAutor.getItemCount(); i++) {
                String opcion = (String) cmbAutor.getItemAt(i);
                if (opcion.startsWith(prefijoBuscado)) {
                    cmbAutor.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Pre-seleccionamos la biblioteca actual en el desplegable
        if (libro.getBiblioteca() != null) {
            String prefijoBuscado = libro.getBiblioteca().getIdBiblioteca() + " - ";
            for (int i = 0; i < cmbBiblioteca.getItemCount(); i++) {
                String opcion = (String) cmbBiblioteca.getItemAt(i);
                if (opcion.startsWith(prefijoBuscado)) {
                    cmbBiblioteca.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void actualizar() {
        try {
            libro.setTitulo(txtTitulo.getText().trim());
            libro.setAnioPublicacion(Integer.parseInt(txtAnio.getText().trim()));
            libro.setEstado((String) cmbEstado.getSelectedItem());

            if (!autores.isEmpty()) {
                String textoAutor = (String) cmbAutor.getSelectedItem();
                String[] partesAutor = textoAutor.split(" - ");
                int idAutor = Integer.parseInt(partesAutor[0]);
                libro.setAutor(autorController.buscarPorId(idAutor));
            }

            if (!bibliotecas.isEmpty()) {
                String textoBiblioteca = (String) cmbBiblioteca.getSelectedItem();
                String[] partesBiblioteca = textoBiblioteca.split(" - ");
                int idBiblioteca = Integer.parseInt(partesBiblioteca[0]);
                libro.setBiblioteca(bibliotecaController.buscarPorId(idBiblioteca));
            }

            libroController.actualizarLibro(libro);
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
