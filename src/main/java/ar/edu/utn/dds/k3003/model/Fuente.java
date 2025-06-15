package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "fuentes")
@Data
public class Fuente {

  @Id
  private String id;
  private String nombre;
  private String endpoint;
  private LocalDateTime fechaModificacion;
  @Transient
  private FachadaFuente fachadaFuente;

  public Fuente() {
    this.fechaModificacion = LocalDateTime.now();
  }

  public Fuente(String id, String nombre, String endpoint) {
    this();
    this.id = id;
    this.nombre = nombre;
    this.endpoint = endpoint;
  }

  public Fuente(String id, FachadaFuente fachadaFuente) {
    this();
    this.id = id;
    this.fachadaFuente = fachadaFuente;
  }

  public void agregarFachadaFuente(FachadaFuente fachadaFuente) {
    this.fachadaFuente = fachadaFuente;
  }


}
