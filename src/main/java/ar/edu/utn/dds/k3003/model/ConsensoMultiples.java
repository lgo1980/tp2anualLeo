package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.service.HechoService;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "consensos_multiples")
public class ConsensoMultiples extends Consenso {

  @Override
  public Boolean aplicar(HechoDTO hecho, List<FuenteDTO> fuentes) {
    if (fuentes.size() == 1) {
      return true;
    }

    HechoService hechoService = new HechoService();
    int contador = 0;
    for (FuenteDTO fuente : fuentes) {
      System.out.println("El nombre de la coleccion: " + hecho.nombreColeccion());
      List<HechoDTO> hechos = hechoService.obtenerHechos(fuente, hecho.nombreColeccion());

      boolean estaPresente = hechos.stream()
          .anyMatch(h -> h.titulo().equalsIgnoreCase(hecho.titulo()));

      if (estaPresente) {
        contador++;
      }
    }
    return contador > 1;
  }

}
