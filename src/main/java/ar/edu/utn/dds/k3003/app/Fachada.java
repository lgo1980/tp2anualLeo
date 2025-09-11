package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Consenso;
import ar.edu.utn.dds.k3003.model.ConsensoMultiples;
import ar.edu.utn.dds.k3003.model.ConsensoTodos;
import ar.edu.utn.dds.k3003.model.FuenteFachada;
import ar.edu.utn.dds.k3003.repository.AgregadorRepository;
import ar.edu.utn.dds.k3003.repository.ConsensoRepository;
import ar.edu.utn.dds.k3003.repository.ConsensoRepositoryImpl;
import ar.edu.utn.dds.k3003.repository.FuenteRepositoryImpl;
import ar.edu.utn.dds.k3003.repository.InMemoryFuenteRepo;
import ar.edu.utn.dds.k3003.model.Agregador;
import ar.edu.utn.dds.k3003.model.Fuente;
import ar.edu.utn.dds.k3003.repository.FuenteRepository;
import ar.edu.utn.dds.k3003.repository.InMemoryagregadorRepo;
import ar.edu.utn.dds.k3003.service.HechoService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Fachada implements FachadaAgregador {

  private final FuenteRepository fuenteRepository;
  @Getter
  private Agregador agregador;
  private final AgregadorRepository agregadorRepository;
  private final ObjectProvider<FachadaFuente> fachadaFuenteProvider;

  @Autowired
  public Fachada(FuenteRepository fuenteRepository,
                 AgregadorRepository agregadorRepository,
                 ObjectProvider<FachadaFuente> fachadaFuenteProvider) {
    this.fuenteRepository = fuenteRepository;
    this.agregadorRepository = agregadorRepository;
    this.fachadaFuenteProvider = fachadaFuenteProvider;
  }

  public Agregador obtenerAgregadorConFuentes(String id) {
    Optional<Agregador> agregador1 = agregadorRepository.findById(id);
    Agregador a = agregador1.orElseGet(() -> agregadorRepository.save(new Agregador(id)));
    List<FuenteFachada> fuentes = a.getFuenteIds().stream()
        .map(fid -> {
          FachadaFuente instancia = fachadaFuenteProvider.getObject(); // 👈 nueva instancia por id
//          instancia.setId(fid);
          return new FuenteFachada(fid, instancia);
        })
        .toList();

    a.setFuentes(fuentes);
    return a;
  }
/*
  public Fachada(ConsensoRepository consensoRepository) {
    this.consensoRepository = consensoRepository;
    this.fachadaFuenteProvider = null;
    this.fuenteRepository = new InMemoryFuenteRepo();
    this.agregadorRepository = new InMemoryagregadorRepo();
    Optional<Agregador> agregador1 = agregadorRepository.findById("1");
    agregador = agregador1.orElseGet(() -> agregadorRepository.save(new Agregador()));
  }*/

  @PostConstruct
  public void init() {
    this.agregador = obtenerAgregadorConFuentes("1");
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
    List<FuenteDTO> fuentes = fuentes();
    Set<String> titulosVistos = new HashSet<>();
    HechoService hechoService = new HechoService();
    Set<HechoDTO> hechosUnicos = new HashSet<>(fuentes.stream()
        .flatMap(fuente -> {
          try {
            return hechoService.obtenerHechos(fuente, coleccionId).stream();
          } catch (Exception e) {
            System.err.println("Error consultando fuente " + fuente.endpoint() + ": " + e.getMessage());
            return Stream.empty();
          }
        })
        .collect(Collectors.toMap(
            hecho -> hecho.titulo() == null ? "" : hecho.titulo().toLowerCase(),
            hecho -> hecho,
            (hechoExistente, nuevoHecho) -> hechoExistente
        ))
        .values());
    return agregador.validarHechos(hechosUnicos, coleccionId, fuentes);
  }

  /*
    Set<HechoDTO> hechosUnicos = fuentes.stream()
        .flatMap(fuente -> hechoService.obtenerHechos(fuente, coleccionId))
        .filter(hecho -> titulosVistos.add(hecho..toLowerCase())) // solo se agregan títulos nuevos
        .collect(Collectors.toSet());
    return validarHechos(hechosUnicos, coleccionId);*/
  //return agregador.consultarHechosPor(coleccionId);

  @Override
  public void addFachadaFuentes(String fuenteId, FachadaFuente fuente) {
    Set<String> fuenteIds = agregador.getFuenteIds();
    if (!(fuenteIds instanceof HashSet)) {
      fuenteIds = new HashSet<>(fuenteIds);
      agregador.setFuenteIds(fuenteIds);
    }
    agregador.agregarFuenteId(fuenteId);
    agregador.agregarFuente(new FuenteFachada(fuenteId, fuente));
    agregadorRepository.save(agregador);
  }

  @Override
  public void setConsensoStrategy(ConsensosEnum tipoConsenso, String coleccionId)
      throws InvalidParameterException {
    if (tipoConsenso == null || coleccionId == null)
      throw new InvalidParameterException();
    Consenso nuevoConsenso = (tipoConsenso == ConsensosEnum.TODOS)
        ? new ConsensoTodos() : new ConsensoMultiples();
    agregador.agregarConsenso(coleccionId, nuevoConsenso);
    agregadorRepository.save(agregador);
  }

}
