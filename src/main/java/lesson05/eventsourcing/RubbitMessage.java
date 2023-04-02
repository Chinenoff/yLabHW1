package lesson05.eventsourcing;

public class RubbitMessage {
  String metod;
  String message;

  public RubbitMessage(String message) {
    String[] items = message.split(";");
    this.metod = items[0];;
    this.message = items[1];
  }

  public RubbitMessage(String metod, String message) {
    this.metod = metod;
    this.message = message;
  }

  private static String getMetodFromRubbitMessage(String message){
    String[] items = message.split(";");
    return items[0];
  }

  private static String getMessageFromRubbitMessage(String message){
    String[] items = message.split(";");
    return items[1];
  }

  @Override
  public String toString() {
    return metod +";"+message;
  }

  public String getMetod() {
    return metod;
  }

  public void setMetod(String metod) {
    this.metod = metod;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
