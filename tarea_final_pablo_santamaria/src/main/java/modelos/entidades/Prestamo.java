package modelos.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Prestamo")
public class Prestamo {

    @Id
    @Column(name = "id_prestamo")
    private int idPrestamo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    // fecha_fin es null mientras el préstamo está activo
    // Cuando el lector devuelve el libro, se rellena con la fecha de devolución
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    // Un préstamo pertenece a un lector (N:1)
    @ManyToOne
    @JoinColumn(name = "id_lector")
    private Lector lector;

    // Un préstamo es de un libro concreto (N:1)
    @ManyToOne
    @JoinColumn(name = "id_libro")
    private Libro libro;

    public Prestamo() {}

    public Prestamo(int idPrestamo, LocalDate fechaInicio, LocalDate fechaFin,
                    Lector lector, Libro libro) {
        this.idPrestamo = idPrestamo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.lector = lector;
        this.libro = libro;
    }

    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Lector getLector() { return lector; }
    public void setLector(Lector lector) { this.lector = lector; }

    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }
}
