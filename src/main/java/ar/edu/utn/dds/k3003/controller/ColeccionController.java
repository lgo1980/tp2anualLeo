package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colecciones")
public class ColeccionController {

  private final FachadaAgregador fachadaAgregador;
  private final FachadaFuente fachadaFuente;

 /* hola*/
  @Autowired
  public ColeccionController(FachadaAgregador fachadaAgregador,
                             FachadaFuente fachadaFuente) {
    this.fachadaAgregador = fachadaAgregador;
    this.fachadaFuente = fachadaFuente;
  }

  @GetMapping
  public ResponseEntity<List<ColeccionDTO>> listarColecciones() {
    return ResponseEntity.ok(fachadaFuente.colecciones());
  }

  @GetMapping("/{nombre}")
  public ResponseEntity<ColeccionDTO> obtenerColeccion(@PathVariable String nombre) {
    return ResponseEntity.ok(fachadaFuente.buscarColeccionXId(nombre));
  }

  @PostMapping
  public ResponseEntity<ColeccionDTO> crearColeccion(@RequestBody ColeccionDTO coleccion) {
    return ResponseEntity.ok(fachadaFuente.agregar(coleccion));
  }

  @GetMapping("/{nombre}/hechos")
  public ResponseEntity<List<HechoDTO>> obtenerHechos(@PathVariable String nombre) {
    return ResponseEntity.ok(fachadaAgregador.hechos(nombre));
  }
} 