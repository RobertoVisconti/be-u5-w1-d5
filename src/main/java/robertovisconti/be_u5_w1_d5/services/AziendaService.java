package robertovisconti.be_u5_w1_d5.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import robertovisconti.be_u5_w1_d5.entities.Edificio;
import robertovisconti.be_u5_w1_d5.entities.Postazione;
import robertovisconti.be_u5_w1_d5.exceptions.EmptyFieldException;
import robertovisconti.be_u5_w1_d5.exceptions.ExistException;
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


    // salvataggio edificio
    public Edificio salvaEdificio(Edificio edificio) {
        // controllo campi vuoto
        if (edificio.getNomeEdificio() == null || edificio.getNomeEdificio().isBlank()) {
            throw new EmptyFieldException("Il campo Nome Edificio non può essere vuoto.");
        }
        if (edificio.getCitta() == null || edificio.getCitta().isBlank()) {
            throw new EmptyFieldException("Il campo Città non può essere vuoto.");
        }
        if (edificio.getIndirizzo() == null || edificio.getIndirizzo().isBlank()) {
            throw new EmptyFieldException("Il campo Indirizzo non può essere vuoto.");
        }
        // controllo se esiste già quell'edificio in quella citta
        if (edificioRepositories.existsByNomeEdificioAndIndirizzo(edificio.getNomeEdificio(), edificio.getIndirizzo())) {
            throw new ExistException("L'edificio: " + edificio.getNomeEdificio() + " esiste già all'indirizzo: " + edificio.getIndirizzo());
        }
        return edificioRepositories.save(edificio);
    }

    // Salvataggio postazione + associazione
    public Postazione salvaPostazione(Postazione postazione, UUID idEdificio) {
        // controllo campi vuoto
        if (postazione.getStatoPostazione() == null) {
            throw new EmptyFieldException("Il campo Stato Postazione non può essere vuoto.");
        }
        if (postazione.getTipoPostazione() == null) {
            throw new EmptyFieldException("Il campo Tipo postazione non può essere vuoto.");
        }
        if (postazione.getNumeroMassimoOccupanti() == null || postazione.getNumeroMassimoOccupanti() <= 0) {
            throw new EmptyFieldException("Il campo Indirizzo non può essere vuoto, ma deve essere specificato.");
        }
        // controllo se l'edificio esiste
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
