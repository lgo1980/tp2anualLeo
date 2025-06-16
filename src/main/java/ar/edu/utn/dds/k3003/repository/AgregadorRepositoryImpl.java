package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Agregador;
import org.springframework.data.repository.CrudRepository;

public interface AgregadorRepositoryImpl extends CrudRepository<Agregador, String>, AgregadorRepository {
}