package ar.edu.utn.dds.k3003.bot;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MiBot implements LongPollingSingleThreadUpdateConsumer {

  private final TelegramClient telegramClient = new OkHttpTelegramClient("8271535623:AAFq1CupmqZYgZJOWBJUxXbhwacuDpNN5iQ");
  private final RestTemplate restTemplate = new RestTemplate();
  private final String BASE_URL = "http://localhost:8080/api/fuentes/busqueda";

  @Override
  public void consume(Update update) {
    if (!update.hasMessage() || !update.getMessage().hasText()) return;

    String chatId = update.getMessage().getChatId().toString();
    String texto = update.getMessage().getText().trim();

    if (texto.startsWith("/busqueda")) {
      try {
        // 1️⃣ Parsear filtros desde el mensaje
        Map<String, String> filtros = parsearParametros(texto);

        // 2️⃣ Construir URL con los filtros
        String url = BASE_URL + construirQueryString(filtros);

        // 3️⃣ Llamar al backend
        ResponseEntity<HechoDTO[]> response = restTemplate.getForEntity(url, HechoDTO[].class);
        HechoDTO[] hechos = response.getBody();

        // 4️⃣ Formatear resultados
        String mensaje = formatearHechos(hechos);

        // 5️⃣ Enviar resultado al chat
        SendMessage sendMessage = new SendMessage(chatId, mensaje);
        sendMessage.setParseMode("HTML"); // para usar negrita, emojis, etc.
        telegramClient.execute(sendMessage);

      } catch (Exception e) {
        e.printStackTrace();
        enviarError(chatId, "❌ Ocurrió un error al realizar la búsqueda: " + e.getMessage());
      }

    } else {
      // Si no manda /busqueda, mostrar ayuda
      enviarMensaje(chatId, """
          👋 Hola! Soy el bot de búsqueda de hechos.

          Usá el comando:
          <pre>/busqueda titulo=robo categoria=DELITO page=1</pre>

          Podes filtrar por:
          • titulo
          • categoria
          • ubicacion
          • page
          """);
    }
  }

  private Map<String, String> parsearParametros(String texto) {
    Map<String, String> filtros = new HashMap<>();
    String[] partes = texto.split("\\s+"); // separa por espacios

    for (int i = 1; i < partes.length; i++) { // salta "/busqueda"
      String[] kv = partes[i].split("=", 2);
      if (kv.length == 2) {
        filtros.put(kv[0].toLowerCase(), kv[1]);
      }
    }
    return filtros;
  }

  private String construirQueryString(Map<String, String> filtros) {
    if (filtros.isEmpty()) return "";
    StringBuilder sb = new StringBuilder("?");
    filtros.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  private String formatearHechos(HechoDTO[] hechos) {
    if (hechos == null || hechos.length == 0) {
      return "⚠️ No se encontraron hechos con esos filtros.";
    }

    StringBuilder sb = new StringBuilder("📋 <b>Resultados de la búsqueda:</b>\n\n");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    for (HechoDTO h : hechos) {
      sb.append("📰 <b>").append(h.titulo()).append("</b>\n");

      if (h.categoria() != null)
        sb.append("🏷️ ").append(h.categoria()).append("\n");

      if (h.ubicacion() != null)
        sb.append("📍 ").append(h.ubicacion()).append("\n");

      if (h.fecha() != null)
        sb.append("🗓️ ").append(h.fecha().format(formatter)).append("\n");

      sb.append("\n──────────────────────\n\n");
    }

    return sb.toString();
  }

  private void enviarMensaje(String chatId, String texto) {
    try {
      SendMessage sendMessage = new SendMessage(chatId, texto);
      sendMessage.setParseMode("HTML");
      telegramClient.execute(sendMessage);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  private void enviarError(String chatId, String texto) {
    enviarMensaje(chatId, "⚠️ " + texto);
  }
}
