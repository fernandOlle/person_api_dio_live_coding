package one.digitalinnovation.personapi.repositories;

import one.digitalinnovation.personapi.entities.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
