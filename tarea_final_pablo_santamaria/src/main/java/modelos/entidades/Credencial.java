package modelos.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Credencial")
public class Credencial {

    @Id
    @Column(name = "id_credencial")
    private int idCredencial;

    // unique = true impide que dos credenciales tengan el mismo número de tarjeta
    @Column(name = "numero_tarjeta", nullable = false, unique = true, length = 50)
    private String numeroTarjeta;

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    // Relación 1:1 con Lector — cada credencial pertenece a un único lector
    // unique = true en @JoinColumn garantiza que un lector no tenga más de una credencial
    @OneToOne
    @JoinColumn(name = "id_lector", unique = true)
    private Lector lector;

    public Credencial() {}

    public Credencial(int idCredencial, String numeroTarjeta, LocalDate fechaEmision, Lector lector) {
        this.idCredencial = idCredencial;
        this.numeroTarjeta = numeroTarjeta;
        this.fechaEmision = fechaEmision;
        this.lector = lector;
    }

    public int getIdCredencial() { return idCredencial; }
    public void setIdCredencial(int idCredencial) { this.idCredencial = idCredencial; }

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }

    public Lector getLector() { return lector; }
    public void setLector(Lector lector) { this.lector = lector; }
}
