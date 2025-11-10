package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.HechoMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface HechoMongoRepository extends MongoRepository<HechoMongo, String> {

  Optional<HechoMongo> findByTitulo(String titulo);
}
