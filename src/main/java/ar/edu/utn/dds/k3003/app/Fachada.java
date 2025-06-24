package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Consenso;
import ar.edu.utn.dds.k3003.model.ConsensoMultiples;
import ar.edu.utn.dds.k3003.model.ConsensoTodos;
import ar.edu.utn.dds.k3003.repository.AgregadorRepository;
import ar.edu.utn.dds.k3003.repository.InMemoryFuenteRepo;
import ar.edu.utn.dds.k3003.model.Agregador;
import ar.edu.utn.dds.k3003.model.Fuente;
import ar.edu.utn.dds.k3003.repository.FuenteRepository;
import ar.edu.utn.dds.k3003.repository.InMemoryagregadorRepo;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Fachada implements FachadaAgregador {

  private final FuenteRepository fuenteRepository;
  @Getter
  private final Agregador agregador;
  private final AgregadorRepository agregadorRepository;

  @Autowired
  public Fachada(FuenteRepository fuenteRepository, AgregadorRepository agregadorRepository) {
    this.fuenteRepository = fuenteRepository;
    this.agregadorRepository = agregadorRepository;

    Optional<Agregador> agregador1 = agregadorRepository.findById("1");
    this.agregador = agregador1.orElseGet(() -> agregadorRepository.save(new Agregador("1")));
  }

  public Fachada() {
    this.fuenteRepository = new InMemoryFuenteRepo();
    this.agregadorRepository = new InMemoryagregadorRepo();
    Optional<Agregador> agregador1 = agregadorRepository.findById("1");
    agregador = agregador1.orElseGet(() -> agregadorRepository.save(new Agregador()));
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
  public void setConsensoStrategy(ConsensosEnum tipoConsenso, String coleccionId)
      throws InvalidParameterException {
    if (tipoConsenso == null || coleccionId == null)
      throw new InvalidParameterException();
    Consenso nuevoConsenso = (tipoConsenso == ConsensosEnum.TODOS)
        ? new ConsensoTodos() : new ConsensoMultiples();
    agregador.setConsenso(nuevoConsenso);
  }

}
