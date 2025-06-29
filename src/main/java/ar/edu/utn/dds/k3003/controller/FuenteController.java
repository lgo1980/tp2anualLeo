package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/fuentes")
public class FuenteController {

  @Autowired
  private FachadaAgregador fachadaAgregador;

  @Autowired
  private FachadaFuente fuente1;

  @GetMapping
  public ResponseEntity<List<FuenteDTO>> listarFuentes() {
    return ResponseEntity.ok(fachadaAgregador.fuentes());
  }

  @PostMapping
  public ResponseEntity<FuenteDTO> crearFuente(@RequestBody FuenteDTO fuenteDTO) {
    FuenteDTO fuenteCreada = fachadaAgregador.agregar(fuenteDTO);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(fuenteCreada.id())
        .toUri();

    return ResponseEntity.created(location).body(fuenteCreada);
  }

  @GetMapping("/coleccion/{nombre}/hechos")
  public ResponseEntity<List<HechoDTO>> obtenerHechos(@PathVariable String nombre) {
//    fachadaAgregador.addFachadaFuentes("1", fuente1);
    return ResponseEntity.ok(fachadaAgregador.hechos(nombre));
  }

  @PatchMapping("/consenso")
  public ResponseEntity<?> modificarConsenso(
      @RequestBody ConsensosEnum consensosEnum) {
    fachadaAgregador.setConsensoStrategy(consensosEnum, "Coleccion1");
    return ResponseEntity.noContent().build();
  }

} 