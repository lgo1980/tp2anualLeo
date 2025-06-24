package ar.edu.utn.dds.k3003.controller;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Agregador;
import ar.edu.utn.dds.k3003.model.ConsensoMultiples;
import ar.edu.utn.dds.k3003.model.ConsensoTodos;
import ar.edu.utn.dds.k3003.repository.InMemoryFuenteRepo;
import ar.edu.utn.dds.k3003.repository.InMemoryagregadorRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ar.edu.utn.dds.k3003.Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.Random.class)
public class FuenteControllerTest {

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
  public void listarFuentes_DeberiaRetornarListaVacia() throws Exception {
    mockMvc.perform(get("/fuentes"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  @Test
  public void crearFuente_DeberiaRetornarColeccionCreada() throws Exception {
    FuenteDTO fuenteDTO = new FuenteDTO("1", "Fuente 1", "1");

    mockMvc.perform(post("/fuentes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(fuenteDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nombre").value("Fuente 1"))
        .andExpect(jsonPath("$.endpoint").value("1"));

    mockMvc.perform(get("/fuentes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nombre").value("Fuente 1"));
  }

  @Test
  public void modificarConsenso() throws Exception {

    Field consensusField = Agregador.class.getDeclaredField("consenso");
    consensusField.setAccessible(true);
    assertNull(consensusField.get(fachada.getAgregador()));


    mockMvc.perform(patch("/fuentes/consenso")
            .contentType(MediaType.APPLICATION_JSON)
            .content("\"TODOS\""))
        .andExpect(status().isNoContent());

    Object consensoActual = consensusField.get(fachada.getAgregador());
    assertNotNull(consensoActual);
    assertInstanceOf(ConsensoTodos.class, consensoActual);

    mockMvc.perform(patch("/fuentes/consenso")
            .contentType(MediaType.APPLICATION_JSON)
            .content("\"AL_MENOS_2\""))
        .andExpect(status().isNoContent());

    consensoActual = consensusField.get(fachada.getAgregador());
    assertNotNull(consensoActual);
    assertInstanceOf(ConsensoMultiples.class, consensoActual);

  }

  @Test
  public void obtenerHechos_deberia_dar_vacio() throws Exception {
    mockMvc.perform(get("/fuentes/coleccion/1/hechos"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
    inicializarSoloFuentes(ConsensosEnum.TODOS);
    mockMvc.perform(get("/fuentes/coleccion/1/hechos"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  @Test
  public void obtenerHechos_consenso_todos() throws Exception {
    inicializarSoloFuentes(ConsensosEnum.TODOS);
    generarDevolucionDeHechosFuente1();
    mockMvc.perform(get("/fuentes/coleccion/1/hechos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].titulo").value("c"))
        .andExpect(jsonPath("$[1].titulo").value("b"))
        .andExpect(jsonPath("$[2].titulo").value("a"));
    /*generarDevolucionDeHechosFuente2();
    mockMvc.perform(get("/fuentes/coleccion/1/hechos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].titulo").value("c"))
        .andExpect(jsonPath("$[1].titulo").value("b"))
        .andExpect(jsonPath("$[2].titulo").value("a"));*/
  }

  private void inicializarSoloFuentes(ConsensosEnum consenso) {
    val fuenteDTO1 = fachada.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    fachada.addFachadaFuentes(fuenteDTO1.id(), fuente1);
    val fuenteDTO2 = fachada.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    fachada.addFachadaFuentes(fuenteDTO2.id(), fuente2);
    fachada.setConsensoStrategy(consenso, "1");
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

  /*
  private void inicializarSoloFuentes(ConsensosEnum consenso) {
    val fuenteDTO1 = fachada.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    fachada.addFachadaFuentes(fuenteDTO1.id(), fuente1);
    val fuenteDTO2 = fachada.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    fachada.addFachadaFuentes(fuenteDTO2.id(), fuente2);
    fachada.setConsensoStrategy(consenso, "1");
    when(fuente1.buscarHechosXColeccion("1")).thenReturn(List.of(
        new HechoDTO("1", "1", "a"), new HechoDTO("2", "1", "b"), new HechoDTO("3", "1", "c")
    ));
    when(fuente2.buscarHechosXColeccion("1")).thenReturn(List.of(
        new HechoDTO("4", "1", "a"), new HechoDTO("5", "1", "b")
    ));
  }
   */
}
