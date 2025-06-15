package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Consenso;
import ar.edu.utn.dds.k3003.model.ConsensoMultiples;
import ar.edu.utn.dds.k3003.model.ConsensoTodos;
import ar.edu.utn.dds.k3003.model.Fuente;
import lombok.Getter;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Getter
public class Agregador {

  private final List<Fuente> fuentes = new ArrayList<>();
  private Consenso consenso;

  public void agregarConsenso(ConsensosEnum tipoConsenso) {
    this.consenso = (tipoConsenso == ConsensosEnum.TODOS) ? new ConsensoTodos() : new ConsensoMultiples();
  }

  public void agregarFuente(Fuente fuente) {
    fuentes.add(fuente);
  }

  public List<HechoDTO> consultarHechosPor(String coleccionId) {
    Set<String> titulosVistos = new HashSet<>();
    Set<HechoDTO> hechosUnicos = fuentes.stream()
        .flatMap(fuente -> fuente.getFachadaFuente().buscarHechosXColeccion(coleccionId).stream())
        .filter(hecho -> titulosVistos.add(hecho.titulo().toLowerCase())) // solo se agregan títulos nuevos
        .collect(Collectors.toSet());
    return validarHechos(hechosUnicos);
  }

  private List<HechoDTO> validarHechos(Set<HechoDTO> hechos) {
    if (consenso == null)
      return List.of(); // devuelve lista inmutable vacía
    return hechos.stream().filter(hechoDTO -> consenso.aplicar(hechoDTO, fuentes)).toList();
  }

}
