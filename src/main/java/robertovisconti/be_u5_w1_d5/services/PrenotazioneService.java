package robertovisconti.be_u5_w1_d5.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import robertovisconti.be_u5_w1_d5.entities.Postazione;
import robertovisconti.be_u5_w1_d5.entities.Prenotazione;
import robertovisconti.be_u5_w1_d5.entities.Utente;
import robertovisconti.be_u5_w1_d5.exceptions.ExistPrenotationException;
import robertovisconti.be_u5_w1_d5.exceptions.NotFoundException;
import robertovisconti.be_u5_w1_d5.repositories.PostazioneRepository;
import robertovisconti.be_u5_w1_d5.repositories.PrenotazioneRepository;
import robertovisconti.be_u5_w1_d5.repositories.UtenteRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final PostazioneRepository postazioneRepository;
    private final UtenteRepository utenteRepository;
    private final AziendaService aziendaService;

    @Autowired

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository, PostazioneRepository postazioneRepository, UtenteRepository utenteRepository, AziendaService aziendaService) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.postazioneRepository = postazioneRepository;
        this.utenteRepository = utenteRepository;
        this.aziendaService = aziendaService;
    }

    // salva utente
    public Utente salvaUtente(Utente utente) {
        return utenteRepository.save(utente);
    }

    // cerca utente
    public Utente cercaUtente(String username) {
        return utenteRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Utente con username: " + username + " non è stato trovato"));
    }


    // effettua prenotazione
    public Prenotazione prenotaPostazione(String username, UUID codiceUnivoco, LocalDateTime data) {

        Utente utente = cercaUtente(username);
        Postazione postazione = aziendaService.cercaPostazione(codiceUnivoco);
        if (prenotazioneRepository.existsByPostazioneAndData(postazione, data)) {
            throw new ExistPrenotationException("La postazione : " + codiceUnivoco + " è già prenotata per il giorno: " + data);
        }

        if (prenotazioneRepository.existsByUtenteAndData(utente, data)) {
            throw new ExistPrenotationException("L'utente " + username + " ha già una prenotazione per il giorno: " + data);
        }


        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUtente(utente);
        prenotazione.setPostazione(postazione);
        prenotazione.setData(data);

        return prenotazioneRepository.save(prenotazione);
    }
    
}
