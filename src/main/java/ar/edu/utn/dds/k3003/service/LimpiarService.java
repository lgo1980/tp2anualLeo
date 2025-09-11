package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.repository.ColeccionRepositoryImpl;
import ar.edu.utn.dds.k3003.repository.ConsensoRepositoryImpl;
import ar.edu.utn.dds.k3003.repository.FuenteRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LimpiarService {

  private final ConsensoRepositoryImpl consensoRepository;
  private final FuenteRepositoryImpl fuenteRepositoryImpl;
  private final ColeccionRepositoryImpl coleccionRepositoryImpl;

  @Autowired
  public LimpiarService(FuenteRepositoryImpl fuenteRepositoryImpl,
                        ConsensoRepositoryImpl consensoRepository,
                        ColeccionRepositoryImpl coleccionRepositoryImpl) {
    this.consensoRepository = consensoRepository;
    this.fuenteRepositoryImpl = fuenteRepositoryImpl;
    this.coleccionRepositoryImpl = coleccionRepositoryImpl;
  }

  @Transactional
  public void limpiarEntidades() {
    consensoRepository.deleteAll();
    fuenteRepositoryImpl.deleteAll();

  }

}
