package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Pdi;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("test")
public class InMemoryPdiRepo implements PdiRepository {

  private final List<Pdi> pdis;

  public InMemoryPdiRepo() {
    this.pdis = new ArrayList<>();
  }

  @Override
  public Optional<Pdi> findById(String id) {
    return this.pdis.stream().filter(x -> x.getId().equals(id)).findFirst();
  }

  @Override
  public Pdi save(Pdi pdi) {
    this.pdis.add(pdi);
    return pdi;
  }

  public Optional<Pdi> findByHecho(String hecho_id) {
    return this.pdis.stream().filter(x -> x.getHechoId().equals(hecho_id)).findFirst();
  }

}