package ar.edu.utn.dds.k3003.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "colecciones")
@Data
public class Coleccion {

  public Coleccion() {
  }

  public Coleccion(String nombre, String descripcion) {
    this.nombre = nombre;
    this.descripcion = descripcion;
  }

  @Id
  private String nombre;
  private String descripcion;
  private LocalDateTime fechaModificacion;


}
