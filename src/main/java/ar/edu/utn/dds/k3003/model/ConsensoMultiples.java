package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "consensos_multiples")
public class ConsensoMultiples extends Consenso {

  @Override
  public Boolean aplicar(HechoDTO hecho, List<Fuente> fuentes) {
    if (fuentes.size() == 1) {
      return true;
    }

    int contador = 0;
    for (Fuente fuente : fuentes) {
      List<HechoDTO> hechos = fuente.getFachadaFuente().buscarHechosXColeccion(hecho.nombreColeccion());

      boolean estaPresente = hechos.stream()
          .anyMatch(h -> h.titulo().equalsIgnoreCase(hecho.titulo()));

      if (estaPresente) {
        contador++;
      }
    }
    return contador > 1;
  }

}
