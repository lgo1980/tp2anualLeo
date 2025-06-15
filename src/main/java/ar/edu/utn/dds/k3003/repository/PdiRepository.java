package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Pdi;
import java.util.Optional;

public interface PdiRepository {

  Optional<Pdi> findById(String id);

  Pdi save(Pdi col);

}