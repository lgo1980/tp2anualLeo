package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Agregador;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("dev")
public interface AgregadorRepositoryImpl extends CrudRepository<Agregador, String>, AgregadorRepository {
}