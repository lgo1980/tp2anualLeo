package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.model.Coleccion;
import ar.edu.utn.dds.k3003.model.Hecho;
import ar.edu.utn.dds.k3003.model.Pdi;
import ar.edu.utn.dds.k3003.repository.ColeccionRepository;
import ar.edu.utn.dds.k3003.repository.HechoRepository;
import ar.edu.utn.dds.k3003.repository.InMemoryColeccionRepo;
import ar.edu.utn.dds.k3003.repository.InMemoryHechoRepo;
import ar.edu.utn.dds.k3003.repository.InMemoryPdiRepo;
import ar.edu.utn.dds.k3003.repository.PdiRepository;
import lombok.Getter;
import lombok.val;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FachadaFuenteImple implements FachadaFuente {


  private final ColeccionRepository coleccionRepository;
  private final HechoRepository hechoRepository;
  @Getter
  private final PdiRepository pdiRepository;
  private FachadaProcesadorPdI fachadaProcesadorPDI;

  public FachadaFuenteImple() {
    this.coleccionRepository = new InMemoryColeccionRepo();
    this.hechoRepository = new InMemoryHechoRepo();
    this.pdiRepository = new InMemoryPdiRepo();
    this.fachadaProcesadorPDI = new FachadaProcesadorPDIImplentado();
  }

  @Override
  public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
    if (this.coleccionRepository.findById(coleccionDTO.nombre()).isPresent()) {
      throw new IllegalArgumentException(coleccionDTO.nombre() + " ya existe");
    }
    val coleccion = new Coleccion(coleccionDTO.nombre(), coleccionDTO.descripcion());
    this.coleccionRepository.save(coleccion);
    return new ColeccionDTO(coleccion.getNombre(), coleccion.getDescripcion());
  }

  @Override
  public ColeccionDTO buscarColeccionXId(String coleccionId) throws NoSuchElementException {
    val coleccionOptional = this.coleccionRepository.findById(coleccionId);
    if (coleccionOptional.isEmpty()) {
      throw new NoSuchElementException(coleccionId + " no existe");
    }
    val coleccion = coleccionOptional.get();
    return new ColeccionDTO(coleccion.getNombre(), coleccion.getDescripcion());
  }

  @Override
  public HechoDTO agregar(HechoDTO hechoDTO) {
    if (this.hechoRepository.findById(hechoDTO.id()).isPresent()) {
      throw new IllegalArgumentException(hechoDTO.id() + " ya existe");
    }

    val hecho = new Hecho(hechoDTO.id(), hechoDTO.titulo(),
        hechoDTO.nombreColeccion(), hechoDTO.categoria(), hechoDTO.ubicacion(),
        hechoDTO.fecha(), hechoDTO.origen(), hechoDTO.etiquetas());
    this.hechoRepository.save(hecho);
    return new HechoDTO(hecho.getId(), hecho.getNombreColeccion(), hecho.getTitulo(),
        hecho.getEtiquetas(), hecho.getCategoria(), hecho.getUbicacion(), hecho.getFecha(),
        hecho.getOrigen());
  }

  @Override
  public HechoDTO buscarHechoXId(String hechoId) throws NoSuchElementException {
    val hechoOptional = this.hechoRepository.findById(hechoId);
    if (hechoOptional.isEmpty()) {
      throw new NoSuchElementException(hechoId + " no existe");
    }
    val hecho = hechoOptional.get();
    return new HechoDTO(hecho.getId(), hecho.getNombreColeccion(), hecho.getTitulo(),
        hecho.getEtiquetas(), hecho.getCategoria(), hecho.getUbicacion(), hecho.getFecha(),
        hecho.getOrigen());
  }

  @Override
  public List<HechoDTO> buscarHechosXColeccion(String coleccionId) throws NoSuchElementException {
    List<Hecho> hechos = this.hechoRepository.findByColeccion(coleccionId);
    if (hechos.isEmpty()) {
      throw new NoSuchElementException(coleccionId + " no existe");
    }
    return hechos.stream()
        .map(x -> new HechoDTO(x.getId(), x.getNombreColeccion(), x.getTitulo(), x.getEtiquetas(),
            x.getCategoria(), x.getUbicacion(), x.getFecha(), x.getOrigen()))
        .collect(Collectors.toList());
  }

  @Override
  public void setProcesadorPdI(FachadaProcesadorPdI procesador) {
    this.fachadaProcesadorPDI = procesador;
  }

  @Override
  public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
    this.fachadaProcesadorPDI.procesar(pdIDTO);
    val pdi = new Pdi(
        pdIDTO.id(),
        pdIDTO.hechoId(),
        pdIDTO.descripcion(),
        pdIDTO.lugar(),
        pdIDTO.momento(),
        pdIDTO.contenido(),
        pdIDTO.etiquetas()
    );

    this.pdiRepository.save(pdi);

    return new PdIDTO(
        pdi.getId(),
        pdi.getHechoId(),
        pdi.getDescripcion(),
        pdi.getLugar(),
        pdi.getMomento(),
        pdi.getContenido(),
        pdi.getEtiquetas()
    );
  }

  @Override
  public List<ColeccionDTO> colecciones() {
    List<Coleccion> colecciones = coleccionRepository.findAll();
    return colecciones.stream()
        .map(coleccion -> new ColeccionDTO(coleccion.getNombre(), coleccion.getDescripcion()))
        .collect(Collectors.toList());
  }
}
