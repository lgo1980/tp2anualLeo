package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class FachadaProcesadorPDIImplentado implements FachadaProcesadorPdI {

  @Override
  public PdIDTO procesar(PdIDTO pdi) throws IllegalStateException {
    return null;
  }

  @Override
  public PdIDTO buscarPdIPorId(String pdiId) throws NoSuchElementException {
    return null;
  }

  @Override
  public List<PdIDTO> buscarPorHecho(String hechoId) throws NoSuchElementException {
    return List.of();
  }

  @Override
  public void setFachadaSolicitudes(FachadaSolicitudes fachadaSolicitudes) {
  }
}
