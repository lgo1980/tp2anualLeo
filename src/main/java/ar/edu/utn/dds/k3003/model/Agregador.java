package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "Agregadores")
@Data
public class Agregador {

  @Id
  private String id;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @MapKeyColumn(name = "clave")
  private Map<String, Consenso> consensos = new HashMap<>();

  public Agregador() {
  }

  public Agregador(String id) {
    this.id = id;
  }

  public List<HechoDTO> consultarHechosPor(String coleccionId) {
    return null;
  }

  public List<HechoDTO> validarHechos(
      List<HechoDTO> todosLosHechos,
      String coleccionId,
      Map<FuenteDTO, List<HechoDTO>> hechosPorFuente) {
    Consenso consenso = consensos.get(coleccionId);
    return todosLosHechos.stream()
        .filter(h -> consenso.aplicar(h, hechosPorFuente))
        .toList();
  }

  public void agregarConsenso(String coleccionId, Consenso consenso) {
    consensos.put(coleccionId, consenso);
  }

}
