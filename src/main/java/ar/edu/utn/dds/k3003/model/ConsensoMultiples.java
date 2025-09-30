package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "consensos_multiples")
public class ConsensoMultiples extends Consenso {


  @Override
  public boolean aplicar(HechoDTO hecho, Map<FuenteDTO, List<HechoDTO>> hechosPorFuente) {

    if (hechosPorFuente.size() == 1) {
      return true; // caso especial: si hay una sola fuente, todos pasan
    }

    String titulo = hecho.titulo().trim().toLowerCase();

    System.out.println("EL titulo es: " + titulo);

    // contar en cuántas fuentes aparece
    long count = hechosPorFuente.values().stream()
        .filter(lista -> lista.stream()
            .anyMatch(h -> h.titulo().trim().equalsIgnoreCase(titulo)))
        .count();

    System.out.println("EL count es: " + count);

    return count >= 2;
  }
}
