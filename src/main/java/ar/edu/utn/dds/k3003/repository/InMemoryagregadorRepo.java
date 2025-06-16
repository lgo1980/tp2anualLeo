package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Agregador;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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