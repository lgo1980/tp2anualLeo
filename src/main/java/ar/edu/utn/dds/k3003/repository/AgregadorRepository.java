package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Agregador;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface AgregadorRepository extends CrudRepository<Agregador, String>{

  Optional<Agregador> findById(String id);

  Agregador save(Agregador col);

  void delete(Agregador agregador);

}

//public interface AgregadorRepository extends JpaRepository<Agregador, String> {