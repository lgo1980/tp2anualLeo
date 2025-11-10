package ar.edu.utn.dds.k3003.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "hechosMongo")
public class HechoMongo {

  @Id
  private String id;
  private String titulo;

  @Field("nombreColeccion")
  private String nombreColeccion;

  public HechoMongo() { }

  public HechoMongo(String titulo, String nombreColeccion) {
    this.titulo = titulo;
    this.nombreColeccion = nombreColeccion;
  }

}
