package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Pdi;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface PdiRepositoryImpl extends CrudRepository<Pdi, String>, PdiRepository {
}
