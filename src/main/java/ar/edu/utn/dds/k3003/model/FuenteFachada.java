package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import lombok.Data;

@Data
public class FuenteFachada {

  private String id;

  private FachadaFuente fuente;

  public FuenteFachada() {
  }

  public FuenteFachada(String id, FachadaFuente fuente) {
    this.id = id;
    this.fuente = fuente;
  }
}
