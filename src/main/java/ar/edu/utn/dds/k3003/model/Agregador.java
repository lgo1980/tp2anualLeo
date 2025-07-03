package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
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

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "agregador_fuentes",
      joinColumns = @JoinColumn(name = "agregador_id"),
      uniqueConstraints = @UniqueConstraint(columnNames = {"agregador_id", "fuente_id"})
  )
  @Column(name = "fuente_id")
  private Set<String> fuenteIds = new HashSet<>();

  @Transient
  private List<FuenteFachada> fuentes = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @MapKeyColumn(name = "clave")
  private Map<String, Consenso> consensos = new HashMap<>();

  public Agregador() {
  }

  public Agregador(String id) {
    this.id = id;
  }

  public void setFuenteIds(Set<String> fuenteIds) {
    this.fuenteIds = new HashSet<>(fuenteIds); // siempre lo hacés mutable al setear
  }

  public void agregarFuente(FuenteFachada fuente) {
    fuentes.add(fuente);
  }

  public List<HechoDTO> consultarHechosPor(String coleccionId) {
    Set<String> titulosVistos = new HashSet<>();
    Set<HechoDTO> hechosUnicos = fuentes.stream()
        .flatMap(fuente -> fuente.getFuente().buscarHechosXColeccion(coleccionId).stream())
        .filter(hecho -> titulosVistos.add(hecho.titulo().toLowerCase())) // solo se agregan títulos nuevos
        .collect(Collectors.toSet());
    return validarHechos(hechosUnicos, coleccionId);
  }

  private List<HechoDTO> validarHechos(Set<HechoDTO> hechos, String coleccionId) {
    Consenso consenso = consensos.get(coleccionId);
    return hechos.stream().filter(hechoDTO -> consenso.aplicar(hechoDTO, fuentes)).toList();
  }

  public void agregarConsenso(String coleccionId, Consenso consenso) {
    consensos.put(coleccionId, consenso);
  }

}
