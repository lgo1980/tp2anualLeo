package ar.edu.utn.dds.k3003;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    /*try {
      String botToken = "8271535623:AAFq1CupmqZYgZJOWBJUxXbhwacuDpNN5iQ";
      TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
      botsApplication.registerBot(botToken, new MiBot());
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }*/
    SpringApplication.run(Application.class, args);
  }
}
