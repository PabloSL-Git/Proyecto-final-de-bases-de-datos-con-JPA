package modelos.entidades;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Biblioteca")
public class Biblioteca {

    @Id
    @Column(name = "id_biblioteca")
    private int idBiblioteca;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "direccion", length = 150)
    private String direccion;

    @OneToMany(mappedBy = "biblioteca")
    private List<Libro> libros;

    @OneToMany(mappedBy = "biblioteca")
    private List<Lector> lectores;

    public Biblioteca() {}

    public Biblioteca(int idBiblioteca, String nombre, String direccion) {
        this.idBiblioteca = idBiblioteca;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public int getIdBiblioteca() { return idBiblioteca; }
    public void setIdBiblioteca(int idBiblioteca) { this.idBiblioteca = idBiblioteca; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }

    public List<Lector> getLectores() { return lectores; }
    public void setLectores(List<Lector> lectores) { this.lectores = lectores; }
}
