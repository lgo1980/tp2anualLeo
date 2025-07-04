package ar.edu.utn.dds.k3003.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Coleccion;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class FachadaTest {
  public static final String UNA_COLECCION = "coleccion 1";
  public static final String DOS_COLECCION = "coleccion 2";
  public static final String COLECCION_NO_ENCONTRADA = "Coleccion no encontrada";
  public static final String DESCRIPCION = "Es la primera coleccion";
  public static final String UN_HECHO = "1";
  public static final String DOS_HECHO = "2";
  public static final String HECHO_NO_ENCONTRADO = "3";
  public static final String UN_PDI = "1";
  public static final String DOS_PDI = "2";
  Coleccion coleccion;
  FachadaFuenteImple fachada;


  @BeforeEach
  void setUp() {
    coleccion = new Coleccion(UNA_COLECCION, DESCRIPCION);
    fachada = new FachadaFuenteImple();
  }

  @Test
  @DisplayName("Agrega una coleccion y la busca")
  void testAgregarYBuscarColeccion() {
    fachada.agregar(new ColeccionDTO(UNA_COLECCION, DESCRIPCION));
    val col = fachada.buscarColeccionXId(UNA_COLECCION);
    assertEquals(UNA_COLECCION, col.nombre());
  }

  @Test
  @DisplayName("Agrega una coleccion pero es repetido y lanza exception")
  void testNoSePuedeAgregarColeccionRepetida() {
    fachada.agregar(new ColeccionDTO(UNA_COLECCION, DESCRIPCION));
    assertThrows(IllegalArgumentException.class, () ->
        fachada.agregar(new ColeccionDTO(UNA_COLECCION, "321")));
  }

  @Test
  @DisplayName("Se quiere buscar una coleccion que no existe y lanza exception")
  void testDevuelveExcepcionSinNoSeEncuentraLaColeccion() {
    assertThrows(NoSuchElementException.class, () ->
        fachada.buscarColeccionXId(COLECCION_NO_ENCONTRADA));
  }

  @Test
  @DisplayName("Agrega un hecho y lo busca")
  void testAgregarYBuscarHecho() {
    fachada.agregar(new HechoDTO(UN_HECHO, UNA_COLECCION, "UnTitulo"));
    val col = fachada.buscarHechoXId(UN_HECHO);
    assertEquals(UN_HECHO, String.valueOf(col.id()));
  }

  @Test
  @DisplayName("Agrega un hecho pero esta repetido y lanza exception")
  void testNoSePuedeAgregarHechoRepetido() {
    fachada.agregar(new HechoDTO(UN_HECHO, UNA_COLECCION, "UnTitulo"));
    assertThrows(IllegalArgumentException.class, () ->
        fachada.agregar(new HechoDTO(UN_HECHO, DOS_COLECCION, "UnTitulo")));
  }

  @Test
  @DisplayName("Se quiere buscar un hecho que no existe y lanza exception")
  void testDevuelveExcepcionSinNoSeEncuentraElHecho() {
    assertThrows(NoSuchElementException.class, () ->
        fachada.buscarHechoXId(HECHO_NO_ENCONTRADO));
  }

  @Test
  @DisplayName("Se quiere buscar los hecho de una coleccion y lanza exception")
  void testBuscarHechosXColeccionNoExiteColeccionYExistePeroNoTieneHechos() {
    assertThrows(NoSuchElementException.class, () ->
        fachada.buscarHechosXColeccion(COLECCION_NO_ENCONTRADA));
    fachada.agregar(new ColeccionDTO(UNA_COLECCION, DESCRIPCION));
    assertThrows(NoSuchElementException.class, () ->
        fachada.buscarHechosXColeccion(UNA_COLECCION));
  }

  @Test
  @DisplayName("Se quiere buscar los hecho de una coleccion")
  void testBuscarHechosXColeccion() {
    fachada.setId("1");
    fachada.agregar(new HechoDTO(UN_HECHO, UNA_COLECCION, "UnTitulo"));
    List<HechoDTO> hechoDTOS = fachada.buscarHechosXColeccion(UNA_COLECCION);
    assertEquals(1, hechoDTOS.size());
    fachada.agregar(new HechoDTO(DOS_HECHO, UNA_COLECCION, "UnTitulo"));
    hechoDTOS = fachada.buscarHechosXColeccion(UNA_COLECCION);
    assertEquals(2, hechoDTOS.size());
    fachada.agregar(new HechoDTO("3", DOS_COLECCION, "UnTitulo"));
    hechoDTOS = fachada.buscarHechosXColeccion(UNA_COLECCION);
    assertEquals(2, hechoDTOS.size());
  }

}
