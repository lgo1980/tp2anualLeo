package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.app.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class FachadaFuenteProxy implements FachadaFuente {

  private final String endpoint;
  private final FachadaFuenteRetrofitClient service;

  @Autowired
  public FachadaFuenteProxy(ObjectMapper objectMapper) {
    var env = System.getenv();
    this.endpoint = env.getOrDefault("URL_FACHADA_FUENTE", "http://localhost:8081/");

    var retrofit =
        new Retrofit.Builder()
            .baseUrl(this.endpoint)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build();

    this.service = retrofit.create(FachadaFuenteRetrofitClient.class);
  }

  @Override
  public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
    try {
      return service.agregarColeccion(coleccionDTO).execute().body();
    } catch (IOException e) {
      throw new RuntimeException("Error al agregar coleccion", e);
    }
  }

  @Override
  public ColeccionDTO buscarColeccionXId(String coleccionId) throws NoSuchElementException {
    try {
      var response = service.buscarColeccionXId(coleccionId).execute();
      System.out.println("estamos buscando  por id");
      if (response.isSuccessful() && response.body() != null) {
        return response.body();
      } else {
        throw new NoSuchElementException("No se encontró coleccion con id: " + coleccionId);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error al buscar coleccion", e);
    }
  }

  @Override
  public HechoDTO agregar(HechoDTO hechoDTO) {
    try {
      return service.agregarHecho(hechoDTO).execute().body();
    } catch (IOException e) {
      throw new RuntimeException("Error al agregar hecho", e);
    }
  }

  @Override
  public HechoDTO buscarHechoXId(String hechoId) throws NoSuchElementException {
    try {
      var response = service.buscarHechoXId(hechoId).execute();
      if (response.isSuccessful() && response.body() != null) {
        return response.body();
      } else {
        throw new NoSuchElementException("No se encontró hecho con id: " + hechoId);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error al buscar hecho", e);
    }
  }

  @Override
  public List<HechoDTO> buscarHechosXColeccion(String coleccionId) throws NoSuchElementException {
    try {
      var response = service.buscarHechosXColeccion(coleccionId).execute();
      if (response.isSuccessful() && response.body() != null) {
        return response.body();
      } else {
        throw new NoSuchElementException("No se encontraron hechos para la coleccion " + coleccionId);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error al buscar hechos por coleccion", e);
    }
  }

  @Override
  public void setProcesadorPdI(FachadaProcesadorPdI procesador) {
    // Este probablemente no se use en la fachada remota
  }

  @Override
  public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
    try {
      return service.agregarPdi(pdIDTO).execute().body();
    } catch (IOException e) {
      throw new RuntimeException("Error al agregar PDI", e);
    }
  }

  @Override
  public List<ColeccionDTO> colecciones() {
    try {
      var response = service.colecciones().execute();
      if (response.isSuccessful() && response.body() != null) {
        return response.body();
      } else {
        return List.of();
      }
    } catch (IOException e) {
      throw new RuntimeException("Error al obtener colecciones", e);
    }
  }

  @Override
  public List<HechoDTO> buscarHechosFiltrados(Map<String, String> filtros) {
    return List.of();
  }

}
