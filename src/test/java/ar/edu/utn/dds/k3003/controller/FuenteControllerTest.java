package ar.edu.utn.dds.k3003.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
public class FuenteControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;


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
    mockMvc.perform(patch("/consenso")
            .contentType(MediaType.APPLICATION_JSON)
            .content("\"MULTIPLES\""))
        .andExpect(status().isNoContent());
  }
}
