package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Hecho;
import java.util.List;
import java.util.Optional;

public interface HechoRepository {

  Optional<Hecho> findById(String id);

  Hecho save(Hecho col);

  List<Hecho> findByColeccion(String coleccion);

}