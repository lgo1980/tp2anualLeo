package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pdis")
@Data
public class Pdi {

  @Id
  private String id;
  private String hechoId;
  private String descripcion;
  private String lugar;
  private LocalDateTime momento;
  private String contenido;
  private List<String> etiquetas;

  public Pdi(String id, String hechoId, String descripcion, String lugar,
             LocalDateTime momento, String contenido, List<String> etiquetas) {
    this.id = id;
    this.hechoId = hechoId;
    this.descripcion = descripcion;
    this.lugar = lugar;
    this.momento = momento;
    this.contenido = contenido;
    this.etiquetas = etiquetas;
  }
}
