package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import java.util.List;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

@Service
public interface FachadaFuenteRetrofitClient {

  @POST("colecciones")
  Call<ColeccionDTO> agregarColeccion(@Body ColeccionDTO coleccion);

  @GET("/api/colecciones/{id}")
  Call<ColeccionDTO> buscarColeccionXId(@Path("id") String id);

  @POST("hechos")
  Call<HechoDTO> agregarHecho(@Body HechoDTO hecho);

  @GET("hechos/{id}")
  Call<HechoDTO> buscarHechoXId(@Path("id") String id);

  @GET("colecciones/{id}/hechos")
  Call<List<HechoDTO>> buscarHechosXColeccion(@Path("id") String coleccionId);

  @POST("pdis")
  Call<PdIDTO> agregarPdi(@Body PdIDTO pdi);

  @GET("colecciones")
  Call<List<ColeccionDTO>> colecciones();

}
