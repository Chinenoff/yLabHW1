package lesson05.eventsourcing.api;

import java.util.List;
import lesson05.eventsourcing.Person;

public interface PersonApi {
  void deletePerson(Long personId);

  void savePerson(Long personId, String firstName, String lastName, String middleName);

  Person findPerson(Long personId);

  List<Person> findAll();
}
