package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.repository.AgregadorRepositoryImpl;
import ar.edu.utn.dds.k3003.repository.ConsensoRepositoryImpl;
import ar.edu.utn.dds.k3003.repository.FuenteRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    consensoRepository.deleteAll();
    fuenteRepositoryImpl.deleteAll();
    agregadorRepositoryImpl.deleteAll();
  }

}
