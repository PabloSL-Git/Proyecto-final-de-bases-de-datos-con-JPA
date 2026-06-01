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

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "id_lector")
    private Lector lector;

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
