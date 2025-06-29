package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "consensos_todos")
public class ConsensoTodos extends Consenso {

  @Override
  public Boolean aplicar(HechoDTO hecho, List<FuenteFachada> fuentes) {
    return true;
  }
}
