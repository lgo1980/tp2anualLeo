package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.HechoMongo;
import ar.edu.utn.dds.k3003.repository.HechoMongoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HechoMongoService {

  private final HechoMongoRepository repo;

  public HechoMongoService(HechoMongoRepository repo) {
    this.repo = repo;
  }

  public HechoMongo guardar(HechoMongo hecho) {
    return repo.save(hecho);
  }

  public List<HechoMongo> listar() {
    return repo.findAll();
  }

  public HechoMongo ObtenerHechoPorTitulo(String nombre) {
    return repo.findByTitulo(nombre)
        .orElse(null);
  }
}