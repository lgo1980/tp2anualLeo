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

    String base = normalizarEndpoint(fuente.endpoint());
    String url = base + "/api/colecciones/" + nombreColeccion + "/hechos";

    ResponseEntity<List<HechoDTO>> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {
        }
    );

    return Objects.requireNonNull(response.getBody()).stream()
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
  }

  private String normalizarEndpoint(String endpoint) {
    if (!endpoint.startsWith("http://") && !endpoint.startsWith("https://")) {
      return "https://" + endpoint;
    }
    return endpoint;
  }
}