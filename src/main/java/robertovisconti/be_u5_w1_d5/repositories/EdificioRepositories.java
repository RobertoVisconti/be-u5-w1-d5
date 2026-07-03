package robertovisconti.be_u5_w1_d5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import robertovisconti.be_u5_w1_d5.entities.Edificio;

import java.util.UUID;

public interface EdificioRepositories extends JpaRepository<Edificio, UUID> {

    boolean existsByNomeEdificioAndIndirizzo(String nomeEdificio, String indirizzo);
}
