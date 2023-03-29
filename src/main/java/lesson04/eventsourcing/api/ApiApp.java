package lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import java.util.Random;
import java.util.Scanner;
import lesson04.RabbitMQUtil;
import lesson04.eventsourcing.Person;

public class ApiApp {
  public static void main(String[] args) throws Exception {
    ConnectionFactory connectionFactory = initMQ();
    PersonApi personApi = new PersonApiImpl(connectionFactory);
    Random random = new Random();

    personApi.savePerson(1111L, "Ivan", "Ivanov", "Ivanovich");
    personApi.savePerson(2222L, "Ivanka", "Ivankovna", "Ivanovich");

    for (int i = 0; i < 10; i++) {
      personApi.savePerson(random.nextLong(), generateBotName() , generateBotName(), generateBotName());
    }
    try {
      Thread.sleep(60000);
    } catch(InterruptedException ex) {}

    //personApi.deletePerson(1111L);
    //System.out.println(personApi.findPerson(2222L));
    personApi.findAll();

  }
  private static ConnectionFactory initMQ() throws Exception {
    return RabbitMQUtil.buildConnectionFactory();
  }
  /*private static void testMenu() throws Exception {
    Random random = new Random();
    ConnectionFactory connectionFactory = initMQ();
    PersonApi personApi = new PersonApiImpl(connectionFactory);
    boolean cheaker = true;
    while(cheaker){
      Scanner scan = new Scanner(System.in);

      System.out.println("Menu");
      System.out.println("1 - Add new person" + "\n" + "2 - Show massive" + "\n" + "3 - Show number of elements" + "\n" +
          "4 - Show cards by the value" + "\n" + "5 - Show cards by color" + "\n" + "6 - Remove" + "\n" + "7 - Quit" + "\n");

      byte functuion = scan.nextByte();

      switch (functuion){

        case 1 : {
          System.out.println("Enter ID person: ");
          Long idPerson = scan.nextLong();

          System.out.println("Enter name person: ");
          String namePerson = scan.nextLine();

          System.out.println("Enter lastName person: ");
          String lastNamePerson = scan.nextLine();

          System.out.println("Enter middleName person: ");
          String middleNamePerson = scan.nextLine();

          personApi.savePerson(idPerson,namePerson,lastNamePerson,middleNamePerson);
          break;
        }

        case 2 : {
          System.out.println("Enter ID person for remove: ");
          Long idPerson = scan.nextLong();
          personApi.deletePerson(idPerson);
        }


        case 3 : break;


        case 4 : break;


        case 5 : break;


        case 6 : break;


        case 7 : break;

      }

    }

  }*/

  private static String generateBotName (){
    Random random = new Random();
    return "Person-" + random.nextInt();
  }
}
