package fr.limayrac.library.repository;

import fr.limayrac.library.model.Emprunts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmpruntRepository extends JpaRepository<Emprunts, Long> {

}
