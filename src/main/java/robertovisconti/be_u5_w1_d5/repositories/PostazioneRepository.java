package robertovisconti.be_u5_w1_d5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import robertovisconti.be_u5_w1_d5.entities.Postazione;
import robertovisconti.be_u5_w1_d5.enums.TipoPostazione;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostazioneRepository extends JpaRepository<Postazione, String> {

    List<Postazione> findByTipoPostazioneAndEdificio_Citta(TipoPostazione tipo, String citta);

    Optional<Postazione> findByCodiceUnivoco(UUID codiveUnivoco);

}
