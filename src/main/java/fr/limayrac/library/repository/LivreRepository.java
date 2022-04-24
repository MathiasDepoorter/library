package fr.limayrac.library.repository;

import fr.limayrac.library.model.Livres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivreRepository extends JpaRepository<Livres, Long> {

}
