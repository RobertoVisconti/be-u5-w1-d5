package robertovisconti.be_u5_w1_d5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import robertovisconti.be_u5_w1_d5.entities.Utente;

import java.util.Optional;
import java.util.UUID;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {

    Optional<Utente> findByUsername(String username);
}
