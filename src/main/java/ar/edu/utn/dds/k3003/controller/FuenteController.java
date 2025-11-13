package ar.edu.utn.dds.k3003.controller;


import ar.edu.utn.dds.k3003.app.FachadaAgregador;
import ar.edu.utn.dds.k3003.dto.CambioConsensoDTO;
import ar.edu.utn.dds.k3003.dto.FuenteFachadaDTO;
import ar.edu.utn.dds.k3003.app.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.service.LimpiarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/fuentes")
public class FuenteController {

  private final RestTemplate restTemplate = new RestTemplate();
  private final FachadaAgregador fachadaAgregador;
  @Autowired
  private final FachadaFuente fachadaFuente;
  private final LimpiarService limpiarService;

  public FuenteController(FachadaAgregador fachadaAgregador,
                          FachadaFuente fachadaFuente,
                          LimpiarService limpiarService) {
    this.fachadaAgregador = fachadaAgregador;
    this.fachadaFuente = fachadaFuente;
    this.limpiarService = limpiarService;
  }

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
    fachadaAgregador.addFachadaFuentes(dto.id(), fachadaFuente);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/limpiar_entidades")
  public ResponseEntity<?> limpiarEntidades() {
    limpiarService.limpiarEntidades();
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/busqueda")
  public ResponseEntity<?> buscar(@RequestParam Map<String, String> filtros) {
    List<HechoDTO> hechos = fachadaAgregador.filtrarHechos(filtros);
    return ResponseEntity.ok(hechos); // 200 con la lista*/
  }

} 