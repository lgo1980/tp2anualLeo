package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.repository.ColeccionRepository;
import ar.edu.utn.dds.k3003.repository.InMemoryColeccionRepo;
import ar.edu.utn.dds.k3003.repository.InMemoryFuenteRepo;
import ar.edu.utn.dds.k3003.service.Agregador;
import ar.edu.utn.dds.k3003.model.Fuente;
import ar.edu.utn.dds.k3003.repository.FuenteRepository;
import lombok.val;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class Fachada implements FachadaAgregador {

  /* @Autowired
   @Qualifier("repoCrudSpring")*/
  private final FuenteRepository fuenteRepository = new InMemoryFuenteRepo();
  private final Agregador agregador = new Agregador();
  private final ColeccionRepository coleccionRepository = new InMemoryColeccionRepo();

  public Fachada() {

  }

  @Override
  public FuenteDTO agregar(FuenteDTO fuenteDTO) {
    val fuente = new Fuente(fuenteDTO.id(), fuenteDTO.nombre(), fuenteDTO.endpoint());
    this.fuenteRepository.save(fuente);
    return new FuenteDTO(fuente.getId(), fuente.getNombre(), fuente.getEndpoint());
  }

  @Override
  public List<FuenteDTO> fuentes() {
    List<Fuente> fuentes = fuenteRepository.findAll();
    return fuentes.stream()
        .map(x -> new FuenteDTO(x.getId(), x.getNombre(), x.getEndpoint()))
        .collect(Collectors.toList());
  }

  @Override
  public FuenteDTO buscarFuenteXId(String fuenteId) throws NoSuchElementException {
    val fuenteOptional = this.fuenteRepository.findById(fuenteId);
    if (fuenteOptional.isEmpty()) {
      throw new NoSuchElementException(fuenteId + " no existe");
    }
    val fuente = fuenteOptional.get();
    return new FuenteDTO(fuente.getId(), fuente.getNombre(), fuente.getEndpoint());
  }

  @Override
  public List<HechoDTO> hechos(String coleccionId) throws NoSuchElementException {
    return agregador.consultarHechosPor(coleccionId);
  }

  @Override
  public void addFachadaFuentes(String fuenteId, FachadaFuente fuente) {
    agregador.agregarFuente(new Fuente(fuenteId, fuente));
  }

  @Override
  public void setConsensoStrategy(ConsensosEnum tipoConsenso, String coleccionId) throws InvalidParameterException {
    agregador.agregarConsenso(tipoConsenso);
  }

}
