package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Agregador;
import java.util.Optional;

public interface AgregadorRepository {

  Optional<Agregador> findById(String id);

  Agregador save(Agregador col);

  void delete(Agregador agregador);

}