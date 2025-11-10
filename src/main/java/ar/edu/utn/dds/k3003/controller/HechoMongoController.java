package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.model.HechoMongo;
import ar.edu.utn.dds.k3003.service.HechoMongoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/mongo/hechos")
public class HechoMongoController {

  private final HechoMongoService service;

  public HechoMongoController(HechoMongoService service) {
    this.service = service;
  }

  @PostMapping
  public HechoMongo crear(@RequestBody HechoMongo hecho) {
    return service.guardar(hecho);
  }

  @GetMapping
  public List<HechoMongo> listar() {
    return service.listar();
  }

  @GetMapping("/{titulo:.+}")
  public HechoMongo obtenerHechoPorTitulo(@PathVariable String titulo) {
    return service.ObtenerHechoPorTitulo(titulo);
  }

}
