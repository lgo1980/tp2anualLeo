package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Objects;

public class HechoService {

  private final RestTemplate restTemplate = new RestTemplate();

  public List<HechoDTO> obtenerHechos(FuenteDTO fuente, String nombreColeccion) {
    String url = fuente.endpoint() + "/api/colecciones/" + nombreColeccion + "/hechos";
    try {
      ResponseEntity<List<HechoDTO>> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          null,
          new ParameterizedTypeReference<>() {
          }
      );

      return Objects.requireNonNullElse(response.getBody(), List.<HechoDTO>of()).stream()
          .map(h -> new HechoDTO(
              h.id(),
              nombreColeccion, // lo forzás acá
              h.titulo(),
              h.etiquetas(),
              h.categoria(),
              h.ubicacion(),
              h.fecha(),
              h.origen()
          ))
          .toList();

    } catch (Exception e) {
      System.err.println("No se pudo consultar la fuente " + fuente.endpoint() + ": " + e.getMessage());
      return List.of(); // devolvés lista vacía si la fuente está caída
    }
  }

}