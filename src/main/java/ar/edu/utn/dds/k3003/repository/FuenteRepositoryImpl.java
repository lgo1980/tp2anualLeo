package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Fuente;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("repoCrudSpring")
@Profile("!test")
public interface FuenteRepositoryImpl extends CrudRepository<Fuente, String>, FuenteRepository {
}
