package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "consensos_extricto")
public class ConsensoExtricto extends Consenso {

  @Override
  public boolean aplicar(HechoDTO hecho, Map<FuenteDTO, List<HechoDTO>> hechosPorFuente) {
    return true;
  }

}
