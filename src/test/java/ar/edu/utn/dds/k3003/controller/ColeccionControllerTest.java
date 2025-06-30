package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Agregador;
import ar.edu.utn.dds.k3003.repository.InMemoryFuenteRepo;
import ar.edu.utn.dds.k3003.repository.InMemoryagregadorRepo;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.Field;
import java.util.List;

@SpringBootTest(classes = ar.edu.utn.dds.k3003.Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ColeccionControllerTest {

  private static final String NOMBRE_FUENTE = "pepe";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private Fachada fachada;

  @Mock
  FachadaFuente fuente1;
  @Mock
  FachadaFuente fuente2;

  @BeforeEach
  public void resetearFachada() throws Exception {
    Field fuenteRepoField = Fachada.class.getDeclaredField("fuenteRepository");
    fuenteRepoField.setAccessible(true);
    fuenteRepoField.set(fachada, new InMemoryFuenteRepo());

    Field agregadorRepoField = Fachada.class.getDeclaredField("agregadorRepository");
    agregadorRepoField.setAccessible(true);
    InMemoryagregadorRepo nuevoAgregadorRepo = new InMemoryagregadorRepo();
    agregadorRepoField.set(fachada, nuevoAgregadorRepo);

    Field agregadorField = Fachada.class.getDeclaredField("agregador");
    agregadorField.setAccessible(true);
    Agregador nuevoAgregador = nuevoAgregadorRepo.save(new Agregador("1"));
    agregadorField.set(fachada, nuevoAgregador);
  }

  @Test
  public void listarColecciones_DeberiaRetornarListaVacia() throws Exception {
    mockMvc.perform(get("/colecciones"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  @Test
  public void crearColeccion_DeberiaRetornarColeccionCreada() throws Exception {
    ColeccionDTO nuevaColeccion = new ColeccionDTO("Coleccion Test", "Descripcion de la coleccion");

    mockMvc.perform(post("/colecciones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nuevaColeccion)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre").value("Coleccion Test"))
        .andExpect(jsonPath("$.descripcion").value("Descripcion de la coleccion"));

    mockMvc.perform(get("/colecciones"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nombre").value("Coleccion Test"));
  }

  @Test
  public void obtenerHechos_deberia_dar_vacio() throws Exception {
    mockMvc.perform(get("/colecciones/1/hechos"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
    inicializarLas2Fuentes();
    mockMvc.perform(get("/colecciones/1/hechos"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  @Test
  public void obtenerHechos_consenso_todos() throws Exception {
    inicializarLas2Fuentes();
    generarDevolucionDeHechosFuente1();
    mockMvc.perform(get("/colecciones/1/hechos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].titulo").value("c"))
        .andExpect(jsonPath("$[1].titulo").value("b"))
        .andExpect(jsonPath("$[2].titulo").value("a"));
    generarDevolucionDeHechosFuente2();
    mockMvc.perform(get("/colecciones/1/hechos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].titulo").value("c"))
        .andExpect(jsonPath("$[1].titulo").value("b"))
        .andExpect(jsonPath("$[2].titulo").value("a"));
  }

  @Test
  public void obtenerHechos_consenso_multiples() throws Exception {
    inicializarFuente(fuente1);
    generarDevolucionDeHechosFuente1();
    mockMvc.perform(get("/colecciones/1/hechos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].titulo").value("c"))
        .andExpect(jsonPath("$[1].titulo").value("b"))
        .andExpect(jsonPath("$[2].titulo").value("a"));
    inicializarFuente(fuente2);
    generarDevolucionDeHechosFuente2();
    mockMvc.perform(get("/colecciones/1/hechos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].titulo").value("b"))
        .andExpect(jsonPath("$[1].titulo").value("a"));
  }

  private void inicializarLas2Fuentes() {
    val fuenteDTO1 = fachada.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    fachada.addFachadaFuentes(fuenteDTO1.id(), fuente1);
    val fuenteDTO2 = fachada.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    fachada.addFachadaFuentes(fuenteDTO2.id(), fuente2);
    fachada.setConsensoStrategy(ConsensosEnum.TODOS, "1");
  }

  private void inicializarFuente(FachadaFuente fuente) {
    val fuenteDTO1 = fachada.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    fachada.addFachadaFuentes(fuenteDTO1.id(), fuente);
    fachada.setConsensoStrategy(ConsensosEnum.AL_MENOS_2, "1");
  }

  private void generarDevolucionDeHechosFuente1() {
    when(fuente1.buscarHechosXColeccion("1")).thenReturn(List.of(
        new HechoDTO("1", "1", "a"),
        new HechoDTO("2", "1", "b"),
        new HechoDTO("3", "1", "c")
    ));
  }

  private void generarDevolucionDeHechosFuente2() {
    when(fuente2.buscarHechosXColeccion("1")).thenReturn(List.of(
        new HechoDTO("4", "1", "a"),
        new HechoDTO("5", "1", "b")
    ));
  }
}
