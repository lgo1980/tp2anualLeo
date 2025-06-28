package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.CategoriaHechoEnum;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "hechos")
@Data
@EqualsAndHashCode(of = "titulo")
public class Hecho {

  @Id
  private String id;
  private String titulo;
  private String nombreColeccion;
  private CategoriaHechoEnum categoria;
  private String ubicacion;
  private LocalDateTime fecha;
  private String origen;
  private List<String> etiquetas;

  public Hecho() {
  }

  public Hecho(String id, String titulo, String nombreColeccion, CategoriaHechoEnum categoria,
               String ubicacion, LocalDateTime fecha, String origen, List<String> etiquetas) {
    this.id = id;
    this.titulo = titulo;
    this.nombreColeccion = nombreColeccion;
    this.categoria = categoria;
    this.ubicacion = ubicacion;
    this.fecha = fecha;
    this.origen = origen;
    this.etiquetas = etiquetas;
  }

}
