package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class FachadaFuenteHttp implements FachadaFuente {

  private final RestTemplate restTemplate;
  private final String baseUrl;
  private static final String COLECCIONES = "/api/colecciones";
  private static final String HECHOS = "/api/hechos";

  public FachadaFuenteHttp(String baseUrl) {
    this.restTemplate = new RestTemplate();
    this.baseUrl = baseUrl;
  }

  @Override
  public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
    return restTemplate.postForObject(
        baseUrl + COLECCIONES,
        coleccionDTO,
        ColeccionDTO.class
    );
  }

  @Override
  public ColeccionDTO buscarColeccionXId(String coleccionId) throws NoSuchElementException {
    try {
      return restTemplate.getForObject(
          baseUrl + COLECCIONES + coleccionId,
          ColeccionDTO.class
      );
    } catch (Exception e) {
      throw new NoSuchElementException("Colección no encontrada en " + baseUrl);
    }
  }

  @Override
  public HechoDTO agregar(HechoDTO hechoDTO) {
    return restTemplate.postForObject(
        baseUrl + HECHOS,
        hechoDTO,
        HechoDTO.class
    );
  }

  @Override
  public HechoDTO buscarHechoXId(String hechoId) throws NoSuchElementException {
    try {
      return restTemplate.getForObject(
          baseUrl + HECHOS + hechoId,
          HechoDTO.class
      );
    } catch (Exception e) {
      throw new NoSuchElementException("Hecho no encontrado en " + baseUrl);
    }
  }

  @Override
  public List<HechoDTO> buscarHechosXColeccion(String coleccionId) throws NoSuchElementException {
    try {
      HechoDTO[] hechos = restTemplate.getForObject(
          baseUrl + COLECCIONES + "/" + coleccionId + "/hechos",
          HechoDTO[].class
      );
      assert hechos != null;
      return Arrays.asList(hechos);
    } catch (Exception e) {
      throw new NoSuchElementException("No se encontraron hechos para la colección " + coleccionId);
    }
  }

  @Override
  public void setProcesadorPdI(FachadaProcesadorPdI procesador) {
    // En un cliente HTTP, normalmente este método no tiene efecto.
    // El procesador se usa en la implementación real de la fuente, no en el cliente.
  }

  @Override
  public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
    return restTemplate.postForObject(
        baseUrl + "/pdis",
        pdIDTO,
        PdIDTO.class
    );
  }

  @Override
  public List<ColeccionDTO> colecciones() {
    ColeccionDTO[] colecciones = restTemplate.getForObject(
        baseUrl + COLECCIONES,
        ColeccionDTO[].class
    );
    assert colecciones != null;
    return Arrays.asList(colecciones);
  }
}
