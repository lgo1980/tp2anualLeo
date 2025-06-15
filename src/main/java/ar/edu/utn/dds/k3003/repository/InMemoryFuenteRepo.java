package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Fuente;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("repoCrudList")
public class InMemoryFuenteRepo implements FuenteRepository {

  private final List<Fuente> fuentes;

  public InMemoryFuenteRepo() {
    this.fuentes = new ArrayList<>();
  }

  @Override
  public Optional<Fuente> findById(String id) {
    return this.fuentes.stream().filter(x -> x.getId().equals(id)).findFirst();
  }

  @Override
  public Fuente save(Fuente fuente) {
    this.fuentes.add(fuente);
    fuente.setFechaModificacion(LocalDateTime.now());
    return fuente;
  }

  public List<Fuente> findAll() {
    return this.fuentes;
  }
}