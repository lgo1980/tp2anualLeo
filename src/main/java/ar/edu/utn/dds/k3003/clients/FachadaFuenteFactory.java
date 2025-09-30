package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;

public class FachadaFuenteFactory {
  /**
   * Crea un cliente HTTP que implementa la interfaz FachadaFuente,
   * apuntando al endpoint remoto de una fuente.
   *
   * @param baseUrl endpoint de la fuente (ej.: <a href="http://localhost:8081">...</a>)
   * @return FachadaFuente implementada vía HTTP
   */
  public static FachadaFuente crearCliente(String baseUrl) {
    return new FachadaFuenteHttp(baseUrl);
  }
}
