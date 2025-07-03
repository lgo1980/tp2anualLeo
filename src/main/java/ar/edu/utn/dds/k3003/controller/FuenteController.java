package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.dto.CambioConsensoDTO;
import ar.edu.utn.dds.k3003.dto.FuenteFachadaDTO;
import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

  @PatchMapping("/consenso")
  public ResponseEntity<?> modificarConsenso(
      @RequestBody CambioConsensoDTO cambioConsensoDTO) {
    fachadaAgregador.setConsensoStrategy(cambioConsensoDTO.tipo(), cambioConsensoDTO.coleccionId());
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/fuente_fahada")
  public ResponseEntity<?> agregarFuenteFachada(
      @RequestBody FuenteFachadaDTO dto) {
    fachadaAgregador.addFachadaFuentes(dto.id(), fuente1);
    return ResponseEntity.noContent().build();
  }

} 