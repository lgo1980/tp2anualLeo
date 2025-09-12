package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.model.Agregador;
import ar.edu.utn.dds.k3003.model.Consenso;
import ar.edu.utn.dds.k3003.model.ConsensoTodos;
import ar.edu.utn.dds.k3003.repository.AgregadorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/agregadores")
public class AgregadorController {

  private final AgregadorRepository agregadorRepository;

  public AgregadorController(AgregadorRepository agregadorRepository) {
    this.agregadorRepository = agregadorRepository;
  }

  @GetMapping
  public ResponseEntity<List<Agregador>> listarAgregadores() {
    List<Agregador> agregadores = (List<Agregador>) agregadorRepository.findAll();
    return ResponseEntity.ok(agregadores);
  }

  @GetMapping("/consenso/{nombrecoleccion}")
  public ResponseEntity<ConsensosEnum> verConsenso(String coleccionId) {
    List<Agregador> agregadores = (List<Agregador>) agregadorRepository.findAll();
    if (agregadores.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Consenso consenso = agregadores.get(0).getConsensos().get(coleccionId);

    if (consenso == null) {
      return ResponseEntity.notFound().build();
    }

    ConsensosEnum resultado = (consenso instanceof ConsensoTodos) ? ConsensosEnum.TODOS : ConsensosEnum.AL_MENOS_2;

    return ResponseEntity.ok(resultado);
  }
}
