package ar.edu.utn.dds.k3003.bot;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class MiBot implements LongPollingSingleThreadUpdateConsumer {

  private final TelegramClient telegramClient = new OkHttpTelegramClient("8271535623:AAFq1CupmqZYgZJOWBJUxXbhwacuDpNN5iQ");

  @Override
  public void consume(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      System.out.println("Esto es lo que se recibio: " + update.getMessage().getText());
    }

    if (update.hasMessage() && update.getMessage().hasText()) {
      // Create your send message object
      String json = """
          {
            "id": 2,
            "nombre": "Leonardo"
          }
          """;
      SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), "<pre>" + json + "</pre>");
      sendMessage.setParseMode("HTML");

      try {
        // Execute it
        telegramClient.execute(sendMessage);
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }
  }
}
