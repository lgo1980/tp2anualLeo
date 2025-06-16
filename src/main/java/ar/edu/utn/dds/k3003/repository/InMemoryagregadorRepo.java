package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Agregador;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("test")
public class InMemoryagregadorRepo implements AgregadorRepository {

  private final List<Agregador> agregadores;

  public InMemoryagregadorRepo() {
    this.agregadores = new ArrayList<>();
  }

  @Override
  public Optional<Agregador> findById(String id) {
    return this.agregadores.stream().filter(x -> x.getId().equals(id)).findFirst();
  }

  @Override
  public Agregador save(Agregador agregador) {
    this.agregadores.add(agregador);
    return agregador;
  }

  @Override
  public void delete(Agregador agregador) {
    agregadores.remove(agregador);
  }

}