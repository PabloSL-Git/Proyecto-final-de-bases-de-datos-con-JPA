package modelos.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "Libro")
public class Libro {

    @Id
    @Column(name = "id_libro")
    private int idLibro;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "anio_publicacion")
    private int anioPublicacion;

    // El ENUM en MySQL solo permite los valores 'disponible' o 'prestado'
    // Se actualiza automáticamente al crear o devolver un préstamo
    @Column(name = "estado", nullable = false)
    private String estado;

    // Relación N:1 con Autor: varios libros pueden tener el mismo autor
    // @JoinColumn indica qué columna de esta tabla actúa como clave foránea
    @ManyToOne
    @JoinColumn(name = "id_autor")
    private Autor autor;

    // Relación N:1 con Biblioteca: varios libros pueden pertenecer a la misma biblioteca
    @ManyToOne
    @JoinColumn(name = "id_biblioteca")
    private Biblioteca biblioteca;

    public Libro() {}

    public Libro(int idLibro, String titulo, int anioPublicacion, String estado,
                 Autor autor, Biblioteca biblioteca) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
        this.estado = estado;
        this.autor = autor;
        this.biblioteca = biblioteca;
    }

    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    public Biblioteca getBiblioteca() { return biblioteca; }
    public void setBiblioteca(Biblioteca biblioteca) { this.biblioteca = biblioteca; }
}
