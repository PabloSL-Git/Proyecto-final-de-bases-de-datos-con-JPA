package modelos.entidades;

import jakarta.persistence.*;
import java.util.List;

// @Entity le dice a JPA que esta clase representa una tabla en la base de datos
@Entity
// @Table indica el nombre exacto de la tabla en MySQL
@Table(name = "Autor")
public class Autor {

    // @Id marca este campo como la clave primaria de la tabla
    @Id
    // @Column permite indicar el nombre exacto de la columna en la BD
    @Column(name = "id_autor")
    private int idAutor;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido1", nullable = false, length = 100)
    private String apellido1;

    @Column(name = "apellido2", length = 100)
    private String apellido2;

    @Column(name = "nacionalidad", length = 100)
    private String nacionalidad;

    // Un autor puede tener varios libros (relación 1:N)
    // mappedBy indica que la FK está en la clase Libro, en el campo "autor"
    @OneToMany(mappedBy = "autor")
    private List<Libro> libros;

    public Autor() {}

    public Autor(int idAutor, String nombre, String apellido1, String apellido2, String nacionalidad) {
        this.idAutor = idAutor;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.nacionalidad = nacionalidad;
    }

    public int getIdAutor() { return idAutor; }
    public void setIdAutor(int idAutor) { this.idAutor = idAutor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido1() { return apellido1; }
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }

    public String getApellido2() { return apellido2; }
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }
}
