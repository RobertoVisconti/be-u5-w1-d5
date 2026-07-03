package robertovisconti.be_u5_w1_d5.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import robertovisconti.be_u5_w1_d5.entities.Edificio;
import robertovisconti.be_u5_w1_d5.entities.Postazione;
import robertovisconti.be_u5_w1_d5.exceptions.NotFoundException;
import robertovisconti.be_u5_w1_d5.repositories.EdificioRepositories;
import robertovisconti.be_u5_w1_d5.repositories.PostazioneRepository;

import java.util.List;
import java.util.UUID;

@Service
public class AziendaService {


    private final EdificioRepositories edificioRepositories;
    private final PostazioneRepository postazioneRepository;

    @Autowired
    public AziendaService(EdificioRepositories edificioRepositories, PostazioneRepository postazioneRepository) {
        this.edificioRepositories = edificioRepositories;
        this.postazioneRepository = postazioneRepository;
    }


    // Salvataggio postazione + associazione
    public Postazione salvaPostazione(Postazione postazione, UUID idEdificio) {
        Edificio edificio = edificioRepositories.findById(idEdificio).orElseThrow(() -> new NotFoundException("Edificio con ID : " + idEdificio + " non trovato."));

        postazione.setEdificio(edificio);
        return postazioneRepository.save(postazione);

    }

    // cerca postazione
    public Postazione cercaPostazione(UUID codiceUnivoco) {
        return postazioneRepository.findByCodiceUnivoco(codiceUnivoco).orElseThrow(() -> new NotFoundException("La postazione con codice: " + codiceUnivoco + " non è stata trovata."));
    }

    // recupero di tutti gli edifici
    public List<Edificio> recuperoEdifici() {
        return edificioRepositories.findAll();
    }


}
