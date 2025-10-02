package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "consensos_extricto")
public class ConsensoExtricto extends Consenso {

  private static final String ENDPOINT = "https://fuentes.onrender.com/api/hecho/sin_solicitudes";

  // ⚠️ Usamos lazy caching para no llamar al endpoint por cada hecho.
  private static Set<String> hechosSinSolicitudesCache = null;

  @Override
  public boolean aplicar(HechoDTO hecho, Map<FuenteDTO, List<HechoDTO>> hechosPorFuente) {

    // 1️⃣ Si aún no lo consultamos, traemos la lista del endpoint
    if (hechosSinSolicitudesCache == null) {
      hechosSinSolicitudesCache = obtenerHechosSinSolicitudes();
    }

    // 2️⃣ Normalizamos el título del hecho
    String titulo = hecho.titulo().trim().toLowerCase();

    // 3️⃣ Devolvemos true si ese título está en la lista de hechos sin solicitudes
    return hechosSinSolicitudesCache.contains(titulo);
  }

  /**
   * Hace la llamada HTTP al endpoint externo y obtiene la lista de hechos sin solicitudes.
   */
  private Set<String> obtenerHechosSinSolicitudes() {
    try {
      // ✅ Usamos el cliente HTTP de Java 11+
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(ENDPOINT))
          .GET()
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        // Se asume que el endpoint devuelve JSON array de strings o de objetos con campo "título"
        String json = response.body();

        // 👇 Usamos Jackson para parsear la respuesta
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        // Puede ser que devuelva ["hecho1", "hecho2"] o [{ "titulo": "hecho1" }, { "titulo": "hecho2" }]
        Set<String> titulos = new HashSet<>();

        if (node.isArray()) {
          for (JsonNode item : node) {
            if (item.isTextual()) {
              titulos.add(item.asText().trim().toLowerCase());
            } else if (item.has("titulo")) {
              titulos.add(item.get("titulo").asText().trim().toLowerCase());
            }
          }
        }
        System.out.println("✅ Cargados " + titulos.size() + " hechos sin solicitudes desde API externa");
        return titulos;
      } else {
        System.err.println("❌ No se pudo obtener hechos sin solicitudes. Código HTTP: " + response.statusCode());
      }

    } catch (Exception e) {
      System.err.println("❌ Error al llamar al endpoint externo: " + e.getMessage());
    }

    // Si falla, devolvemos un Set vacío (así ningún hecho pasa el filtro)
    return Set.of();
  }

}
