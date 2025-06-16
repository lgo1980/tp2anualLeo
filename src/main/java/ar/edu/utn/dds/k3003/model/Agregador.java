package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Agregadores")
@Data
public class Agregador {

  @Id
  private String id;

  @ManyToMany
  @JoinTable(
      name = "agregador_fuente",
      joinColumns = @JoinColumn(name = "agregador_id"),
      inverseJoinColumns = @JoinColumn(name = "fuente_id")
  )
  private final List<Fuente> fuentes = new ArrayList<>();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "consenso_id")
  private Consenso consenso;

  public void agregarFuente(Fuente fuente) {
    fuentes.add(fuente);
  }

  public List<HechoDTO> consultarHechosPor(String coleccionId) {
    Set<String> titulosVistos = new HashSet<>();
    Set<HechoDTO> hechosUnicos = fuentes.stream()
        .flatMap(fuente -> fuente.getFachadaFuente().buscarHechosXColeccion(coleccionId).stream())
        .filter(hecho -> titulosVistos.add(hecho.titulo().toLowerCase())) // solo se agregan títulos nuevos
        .collect(Collectors.toSet());
    return validarHechos(hechosUnicos, coleccionId);
  }

  private List<HechoDTO> validarHechos(Set<HechoDTO> hechos, String coleccionId) {
    return hechos.stream().filter(hechoDTO -> consenso.aplicar(hechoDTO, fuentes)).toList();
  }

}
