package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.Agregador;
import ar.edu.utn.dds.k3003.repository.AgregadorRepository;
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
  private final AgregadorRepository agregadorRepository;

  @Autowired
  public LimpiarService(FuenteRepositoryImpl fuenteRepositoryImpl,
                        ConsensoRepositoryImpl consensoRepository,
                        AgregadorRepository agregadorRepository) {
    this.consensoRepository = consensoRepository;
    this.fuenteRepositoryImpl = fuenteRepositoryImpl;
    this.agregadorRepository = agregadorRepository;
  }

  @Transactional
  public void limpiarEntidades() {
    // 1️⃣ Limpiar los mapas de consensos de todos los agregadores
    List<Agregador> agregadores = (List<Agregador>) agregadorRepository.findAll();
    for (Agregador a : agregadores) {
      if (a.getConsensos() != null && !a.getConsensos().isEmpty()) {
        a.getConsensos().clear(); // limpia el Map en memoria
        agregadorRepository.save(a); // persiste los cambios
      }
    }

    // 2️⃣ Borrar todas las tablas de datos dependientes
    consensoRepository.deleteAll();  // aunque ya se limpiaron en memoria, esto asegura la limpieza completa
    fuenteRepositoryImpl.deleteAll();

    // 3️⃣ Borrar los agregadores
    agregadorRepository.deleteAll();
  }

}
