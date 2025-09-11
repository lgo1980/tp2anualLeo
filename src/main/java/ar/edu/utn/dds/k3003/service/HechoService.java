package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.List;

public class HechoService {

  private final RestTemplate restTemplate = new RestTemplate();

  public List<HechoDTO> obtenerHechos(FuenteDTO fuente, String nombreColeccion) {
    String url = fuente.endpoint() + "/api/colecciones/" + nombreColeccion + "/hechos";

    ResponseEntity<List<HechoDTO>> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {
        }
    );

    return response.getBody();
  }
}