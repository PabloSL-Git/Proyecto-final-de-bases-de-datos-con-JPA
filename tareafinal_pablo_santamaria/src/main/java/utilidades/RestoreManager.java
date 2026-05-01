package utilidades;

import controladores.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modelos.entidades.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestoreManager {

    private AutorController autorController = new AutorController();
    private LibroController libroController = new LibroController();
    private BibliotecaController bibliotecaController = new BibliotecaController();
    private LectorController lectorController = new LectorController();
    private CredencialController credencialController = new CredencialController();
    private PrestamoController prestamoController = new PrestamoController();

    // =====================
    // OBTENER ÚLTIMA COPIA
    // =====================
    public String obtenerUltimaEopia() {
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("backup_"));
        
        if (files == null || files.length == 0) {
            return null;
        }
        
        // Ordenar por fecha (más reciente primero)
        Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
        
        return files[0].getAbsolutePath();
    }

    // =====================
    // LISTAR TODAS LAS COPIAS
    // =====================
    public List<String> listarTodasLasCopias() {
        List<String> copias = new ArrayList<>();
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("backup_"));
        
        if (files != null) {
            // Ordenar por fecha (más reciente primero)
            Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
            
            for (File f : files) {
                copias.add(f.getAbsolutePath());
            }
        }
        
        return copias;
    }

    // =====================
    // RESTAURAR
    // =====================
    public void restaurar(String carpeta) {

        try {

            // 1. BORRAR
            borrarTodo();

            // 2. RESTAURAR EN ORDEN (respetando claves foráneas)
            restaurarBibliotecas(carpeta + "/bibliotecas.csv");
            restaurarAutores(carpeta + "/autores.csv");
            restaurarLectores(carpeta + "/lectores.csv");
            restaurarLibros(carpeta + "/libros.csv");
            restaurarCredenciales(carpeta + "/credenciales.csv");
            restaurarPrestamos(carpeta + "/prestamos.csv");

            System.out.println("✔ Restauración completada");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------
    // BORRADO GENERAL
    // -------------------------
    private void borrarTodo() {

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Orden IMPORTANTE por claves foráneas
            em.createQuery("DELETE FROM Prestamo").executeUpdate();
            em.createQuery("DELETE FROM Credencial").executeUpdate();
            em.createQuery("DELETE FROM Libro").executeUpdate();
            em.createQuery("DELETE FROM Lector").executeUpdate();
            em.createQuery("DELETE FROM Autor").executeUpdate();
            em.createQuery("DELETE FROM Biblioteca").executeUpdate();

            tx.commit();

            System.out.println("✔ Base de datos limpiada correctamente");

        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // -------------------------
    // AUTORES
    // -------------------------
    private void restaurarAutores(String file) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine(); // saltar cabecera

    // -------------------------
    // BIBLIOTECAS
    // -------------------------
    private void restaurarBibliotecas(String file) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine(); // cabecera

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            Biblioteca b = new Biblioteca();
            b.setIdBiblioteca(Integer.parseInt(data[0]));
            b.setNombre(data[1]);
            b.setDireccion(data[2]);

            bibliotecaController.insertarBiblioteca(b);
        }

        br.close();
    }

    // -------------------------
    // LECTORES
    // -------------------------
    private void restaurarLectores(String file) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine(); // cabecera

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            Lector l = new Lector();
            l.setIdLector(Integer.parseInt(data[0]));
            l.setNombre(data[1]);
            l.setApellido1(data[2]);
            l.setApellido2(data[3]);
            l.setEmail(data[4]);
            l.setTelefono(data[5]);

            // Si hay referencia a biblioteca
            if (data.length > 6 && !data[6].isEmpty() && !data[6].equals("0")) {
                int idBib = Integer.parseInt(data[6]);
                Biblioteca b = bibliotecaController.buscarPorId(idBib);
                l.setBiblioteca(b);
            }

            lectorController.insertarLector(l);
        }

        br.close();
    }

    // -------------------------
    // CREDENCIALES
    // -------------------------
    private void restaurarCredenciales(String file) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine(); // cabecera

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            Credencial c = new Credencial();
            c.setIdCredencial(Integer.parseInt(data[0]));
            c.setNumeroTarjeta(data[1]);
            
            if (data[2] != null && !data[2].isEmpty() && !data[2].equals("null")) {
                c.setFechaEmision(LocalDate.parse(data[2]));
            }

            // Si hay referencia a lector
            if (data.length > 3 && !data[3].isEmpty() && !data[3].equals("0")) {
                int idLector = Integer.parseInt(data[3]);
                Lector l = lectorController.buscarPorId(idLector);
                c.setLector(l);
            }

            credencialController.insertarCredencial(c);
        }

        br.close();
    }

    // -------------------------
    // PRÉSTAMOS
    // -------------------------
    private void restaurarPrestamos(String file) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine(); // cabecera

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            Prestamo p = new Prestamo();
            p.setIdPrestamo(Integer.parseInt(data[0]));
            p.setFechaInicio(LocalDate.parse(data[1]));
            
            if (data[2] != null && !data[2].isEmpty() && !data[2].equals("null")) {
                p.setFechaFin(LocalDate.parse(data[2]));
            }

            // Si hay referencia a lector
            if (data.length > 3 && !data[3].isEmpty() && !data[3].equals("0")) {
                int idLector = Integer.parseInt(data[3]);
                Lector l = lectorController.buscarPorId(idLector);
                p.setLector(l);
            }

            // Si hay referencia a libro
            if (data.length > 4 && !data[4].isEmpty() && !data[4].equals("0")) {
                int idLibro = Integer.parseInt(data[4]);
                Libro lib = libroController.buscarPorId(idLibro);
                p.setLibro(lib);
            }

            prestamoController.insertarPrestamo(p);
        }

        br.close();
    }

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            Autor a = new Autor();
            a.setIdAutor(Integer.parseInt(data[0]));
            a.setNombre(data[1]);
            a.setApellido1(data[2]);
            a.setApellido2(data[3]);
            a.setNacionalidad(data[4]);

            autorController.insertarAutor(a);
        }

        br.close();
    }

    // -------------------------
    // LIBROS
    // -------------------------
    private void restaurarLibros(String file) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine(); // cabecera

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            Libro l = new Libro();
            l.setIdLibro(Integer.parseInt(data[0]));
            l.setTitulo(data[1]);
            l.setAnioPublicacion(Integer.parseInt(data[2]));
            l.setEstado(data[3]);

            libroController.insertarLibro(l);
        }

        br.close();
    }
}