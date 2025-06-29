package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "fuentes_fachada")
@Data
public class FuenteFachada {

  @Id
  private String id;

  @Autowired
  @Transient
  private FachadaFuente fuente;

  public FuenteFachada() {
  }

  public FuenteFachada(String id, FachadaFuente fuente) {
    this.id = id;
    this.fuente = fuente;
  }
}
