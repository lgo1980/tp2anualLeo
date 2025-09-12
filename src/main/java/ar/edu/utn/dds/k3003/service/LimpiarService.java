package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.Agregador;
import ar.edu.utn.dds.k3003.repository.AgregadorRepositoryImpl;
import ar.edu.utn.dds.k3003.repository.ConsensoRepositoryImpl;
import ar.edu.utn.dds.k3003.repository.FuenteRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LimpiarService {

  private final ConsensoRepositoryImpl consensoRepository;
  private final FuenteRepositoryImpl fuenteRepositoryImpl;
  private final AgregadorRepositoryImpl agregadorRepositoryImpl;

  @Autowired
  public LimpiarService(FuenteRepositoryImpl fuenteRepositoryImpl,
                        ConsensoRepositoryImpl consensoRepository,
                        AgregadorRepositoryImpl agregadorRepositoryImpl) {
    this.consensoRepository = consensoRepository;
    this.fuenteRepositoryImpl = fuenteRepositoryImpl;
    this.agregadorRepositoryImpl = agregadorRepositoryImpl;
  }

  @Transactional
  public void limpiarEntidades() {
    List<Agregador> agregadores = (List<Agregador>) agregadorRepositoryImpl.findAll();
    for (Agregador a : agregadores) {
      if (a.getConsensos() != null) {
        a.getConsensos().clear(); // limpia el Map en memoria
        agregadorRepositoryImpl.save(a); // persiste los cambios
      }
    }

    // 2️⃣ Borrar todas las tablas de datos dependientes
    agregadorRepositoryImpl.deleteAll();
    consensoRepository.deleteAll();
    fuenteRepositoryImpl.deleteAll();
  }

}
