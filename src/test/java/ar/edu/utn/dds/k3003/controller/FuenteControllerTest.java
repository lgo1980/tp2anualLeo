package ar.edu.utn.dds.k3003.controller;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.dto.CambioConsensoDTO;
import ar.edu.utn.dds.k3003.dto.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.model.Agregador;
import ar.edu.utn.dds.k3003.model.Consenso;
import ar.edu.utn.dds.k3003.model.ConsensoMultiples;
import ar.edu.utn.dds.k3003.model.ConsensoTodos;
import ar.edu.utn.dds.k3003.repository.InMemoryFuenteRepo;
import ar.edu.utn.dds.k3003.repository.InMemoryagregadorRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
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

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private Fachada fachada;

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

    CambioConsensoDTO cambio = new CambioConsensoDTO("1", ConsensosEnum.TODOS);

    mockMvc.perform(patch("/fuentes/consenso")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cambio)))
        .andExpect(status().isNoContent());

    Consenso consenso = fachada.getAgregador().getConsensos().get("1");
    assertNotNull(consenso);
    assertInstanceOf(ConsensoTodos.class, consenso);

    cambio = new CambioConsensoDTO("1", ConsensosEnum.AL_MENOS_2);

    mockMvc.perform(patch("/fuentes/consenso")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cambio)))
        .andExpect(status().isNoContent());

    consenso = fachada.getAgregador().getConsensos().get("1");
    assertNotNull(consenso);
    assertInstanceOf(ConsensoMultiples.class, consenso);

  }
}
