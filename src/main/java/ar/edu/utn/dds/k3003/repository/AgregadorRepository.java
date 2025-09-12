package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Agregador;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface AgregadorRepository extends CrudRepository<Agregador, String> {

  Optional<Agregador> findById(String id);

  Agregador save( Agregador col);

  void delete(Agregador agregador);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM agregadores_consensos", nativeQuery = true)
  void borrarConsensosDeAgregadores();

}

//public interface AgregadorRepository extends JpaRepository<Agregador, String> {