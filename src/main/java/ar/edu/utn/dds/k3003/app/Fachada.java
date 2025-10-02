package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.clients.FachadaFuenteFactory;
import ar.edu.utn.dds.k3003.dto.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Consenso;
import ar.edu.utn.dds.k3003.model.ConsensoExtricto;
import ar.edu.utn.dds.k3003.model.ConsensoMultiples;
import ar.edu.utn.dds.k3003.model.ConsensoTodos;
import ar.edu.utn.dds.k3003.repository.AgregadorRepository;
import ar.edu.utn.dds.k3003.model.Agregador;
import ar.edu.utn.dds.k3003.model.Fuente;
import ar.edu.utn.dds.k3003.repository.FuenteRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.val;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Fachada implements FachadaAgregador {

  private static final Logger log = LoggerFactory.getLogger(Fachada.class);
  private final FuenteRepository fuenteRepository;
  @Getter
  private Agregador agregador;
  private final AgregadorRepository agregadorRepository;
  private final Map<String, FachadaFuente> fuentes = new HashMap<>();

  @Autowired
  public Fachada(FuenteRepository fuenteRepository,
                 AgregadorRepository agregadorRepository,
                 ObjectProvider<FachadaFuente> fachadaFuenteProvider) {
    this.fuenteRepository = fuenteRepository;
    this.agregadorRepository = agregadorRepository;
    Optional<Agregador> agregador1 = agregadorRepository.findById("1");
    agregador = agregador1.orElseGet(() -> agregadorRepository.save(new Agregador("1")));
  }

  @PostConstruct
  public void init() {
    List<Fuente> todas = fuenteRepository.findAll();
    for (Fuente f : todas) {
      try {
        if (f.getEndpoint() == null) {
          continue;
        }
        FachadaFuente cliente = FachadaFuenteFactory.crearCliente(f.getEndpoint());
        addFachadaFuentes(f.getId(), cliente);
      } catch (Exception ex) {
        System.out.println("Hubo una exepcion");
      }
    }
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

    Map<FuenteDTO, List<HechoDTO>> hechosPorFuente = new HashMap<>();

    for (FuenteDTO fuente : fuentes()) {
      try {
        List<HechoDTO> hechos = this.fuentes.get(fuente.id()).buscarHechosXColeccion(coleccionId);
        Map<String, HechoDTO> sinRepetidos = hechos.stream()
            .collect(Collectors.toMap(
                h -> h.titulo().trim().toLowerCase(),
                h -> h,
                (h1, h2) -> h1
            ));
        hechosPorFuente.put(fuente, new ArrayList<>(sinRepetidos.values()));
      } catch (Exception e) {
        System.out.println("No se pudo obtener los hechos porque: " + e.getMessage());
      }
    }

    Map<String, HechoDTO> hechosUnicos = new HashMap<>();
    for (List<HechoDTO> lista : hechosPorFuente.values()) {
      for (HechoDTO h : lista) {
        String key = h.titulo().trim().toLowerCase();
        hechosUnicos.putIfAbsent(key, h);
      }
    }
    List<HechoDTO> todosLosHechos = new ArrayList<>(hechosUnicos.values());
    return agregador.validarHechos(todosLosHechos, coleccionId, hechosPorFuente);
  }

  @Override
  public void addFachadaFuentes(String fuenteId, FachadaFuente fuente) {
    this.fuentes.put(fuenteId, fuente);
  }

  @Override
  public void setConsensoStrategy(ConsensosEnum tipoConsenso, String coleccionId)
      throws InvalidParameterException {
    if (tipoConsenso == null || coleccionId == null)
      throw new InvalidParameterException();


    Consenso nuevoConsenso;

    switch (tipoConsenso) {
      case TODOS -> nuevoConsenso = new ConsensoTodos();
      case AL_MENOS_2 -> nuevoConsenso = new ConsensoMultiples();
      case EXTRICTO -> nuevoConsenso = new ConsensoExtricto();
      default -> throw new InvalidParameterException("Tipo de consenso no soportado: " + tipoConsenso);
    }

    if (agregador.getConsensos() == null) {
      agregador.setConsensos(new HashMap<>());
    }

    agregador.getConsensos().remove(coleccionId);
    agregador.agregarConsenso(coleccionId, nuevoConsenso);
    agregadorRepository.save(agregador);
  }

}
