package robertovisconti.be_u5_w1_d5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import robertovisconti.be_u5_w1_d5.entities.Postazione;
import robertovisconti.be_u5_w1_d5.entities.Prenotazione;
import robertovisconti.be_u5_w1_d5.entities.Utente;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {

    boolean existsByPostazioneAndData(Postazione postazione, LocalDateTime data);

    boolean existsByUtenteAndData(Utente utente, LocalDateTime data);

    boolean existsByPostazione_CodiceUnivocoAndDataBetween(UUID codiceUnivoco, LocalDateTime start, LocalDateTime end);
}
