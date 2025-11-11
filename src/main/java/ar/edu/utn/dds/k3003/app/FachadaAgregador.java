package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.dto.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public interface FachadaAgregador {

  FuenteDTO agregar(FuenteDTO fuente);

  List<FuenteDTO> fuentes();

  FuenteDTO buscarFuenteXId(String fuenteId) throws NoSuchElementException;

  List<HechoDTO> hechos(String coleccionId) throws NoSuchElementException;

  void addFachadaFuentes(String fuenteId, FachadaFuente fuente);

  void setConsensoStrategy(ConsensosEnum tipoConsenso, String coleccionId)
      throws InvalidParameterException;

  List<HechoDTO> filtrarHechos(Map<String,String> filtros) throws NoSuchElementException;

}
