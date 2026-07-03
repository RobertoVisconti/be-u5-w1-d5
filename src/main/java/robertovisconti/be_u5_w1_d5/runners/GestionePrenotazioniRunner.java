package robertovisconti.be_u5_w1_d5.runners;


import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import robertovisconti.be_u5_w1_d5.entities.Edificio;
import robertovisconti.be_u5_w1_d5.entities.Postazione;
import robertovisconti.be_u5_w1_d5.entities.Prenotazione;
import robertovisconti.be_u5_w1_d5.entities.Utente;
import robertovisconti.be_u5_w1_d5.enums.StatoPostazione;
import robertovisconti.be_u5_w1_d5.enums.TipoPostazione;
import robertovisconti.be_u5_w1_d5.exceptions.ErrorTimeException;
import robertovisconti.be_u5_w1_d5.exceptions.SaveErrorException;
import robertovisconti.be_u5_w1_d5.services.AziendaService;
import robertovisconti.be_u5_w1_d5.services.PrenotazioneService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class GestionePrenotazioniRunner implements CommandLineRunner {


    private final AziendaService aziendaService;
    private final PrenotazioneService prenotazioneService;
    private final Faker faker;

    @Autowired
    public GestionePrenotazioniRunner(AziendaService aziendaService, PrenotazioneService prenotazioneService, Faker faker) {
        this.aziendaService = aziendaService;
        this.prenotazioneService = prenotazioneService;
        this.faker = faker;
    }


    @Override
    public void run(String... args) throws Exception {
        List<Edificio> edificiDB = aziendaService.recuperoEdifici();
        boolean utentiInseriti = prenotazioneService.utentiPresenti();


        // prenotazione avvenuta
        try {
            System.out.println("***** PRENOTAZIONE AVVENUTA *****");
            List<Postazione> postazioneDisponibili = prenotazioneService.cercaPostazioni(TipoPostazione.OPENSPACE, "Roma");
            System.out.println("Postazioni disponibili in base alla ricerca " + postazioneDisponibili);

            String usernameDB = "gianriccardo_ferretti_6463";
            UUID idPostazioneDB = UUID.fromString("de9fa2b2-6584-4340-ab2d-0b1168d29b94");
            LocalDateTime dataPrenotazione = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);

            Prenotazione nuovaPrenotazione = prenotazioneService.prenotaPostazione(usernameDB, idPostazioneDB, dataPrenotazione);

            System.out.println("Prenotazione effettuata con successo! ID: " + nuovaPrenotazione.getId());
        } catch (SaveErrorException ex) {
            System.out.println("Impossibile effettuare la prenotazione:" + ex.getMessage());
        }

        // prenotazione con verifica dello stato
        try {
            System.out.println("***** PRENOTAZIONE CON VERIFICA DELLO STATO*****");

            UUID idPosDB = UUID.fromString("de9fa2b2-6584-4340-ab2d-0b1168d29b94");

            // stato postazione
            StatoPostazione statoOra = prenotazioneService.statoAttuale(idPosDB, LocalDateTime.now());
            System.out.println("Stato ora: " + statoOra);

            //Prenotazione che parte ora
            String usernameDb = "gianriccardo_ferretti_6463";
            prenotazioneService.prenotaPostazione(usernameDb, idPosDB, LocalDateTime.now());
            System.out.println(" Creazione riuscita in questo esatto momento.");

            // controllo lo stato dopo la prenotazione
            StatoPostazione statoDopoPren = prenotazioneService.statoAttuale(idPosDB, LocalDateTime.now());
            System.out.println("Stato dopo la prenotazione: " + statoDopoPren);

            // controllo dopo 25 ore
            LocalDateTime afterH24 = LocalDateTime.now().plusHours(25);
            StatoPostazione statoAfterH24 = prenotazioneService.statoAttuale(idPosDB, afterH24);
            System.out.println("Stato dopo le 24 ore: " + statoAfterH24 + " Se si vuole far risultare libero per provare il metodo commentare il primo test di prenotazione.");

        } catch (ErrorTimeException ex) {
            System.out.println("Erroe durante il test delle 24 ore passate: " + ex.getMessage());
        }


        // popolo il DB con controllo
        if (!edificiDB.isEmpty() || utentiInseriti) {
            System.out.println(" Database già popolato");
            return;
        }

        // genero edifici
        for (int i = 0; i < 10; i++) {
            Edificio ed = new Edificio();
            ed.setNomeEdificio("Sede " + faker.company().name());
            ed.setCitta(faker.address().city());
            ed.setIndirizzo(faker.address().streetAddress());
            try {
                aziendaService.salvaEdificio(ed);
            } catch (SaveErrorException ex) {
                System.out.println("Errore durante il salvataggio per l'edificio : " + ed.getNomeEdificio() + " Motivo: " + ex.getMessage());
            }
        }

        // genero e associo le postazioni
        List<Edificio> edificiDB2 = aziendaService.recuperoEdifici();
        for (int i = 0; i < 15; i++) {
            Postazione pos = new Postazione();

            pos.setDescrizione("Postazione " + faker.options().option("Premium", "Standard", "Executive") + "con monitor " + faker.options().option("24\"", "27\"", "32\""));
            pos.setTipoPostazione(faker.options().option(TipoPostazione.OPENSPACE, TipoPostazione.PRIVATO, TipoPostazione.SALA_RIUNIONI));
            pos.setNumeroMassimoOccupanti(faker.number().numberBetween(1, 20));
            pos.setStatoPostazione(StatoPostazione.LIBERO);

            Edificio edificioAss = edificiDB2.get(faker.number().numberBetween(0, edificiDB2.size()));
            try {
                aziendaService.salvaPostazione(pos, edificioAss.getId());
            } catch (SaveErrorException ex) {
                System.out.println("Errore durante il salvataggio e l'associazione per : " + pos + " Motivo: " + ex.getMessage());
            }

        }

        // genero utenti
        for (int i = 0; i < 15; i++) {
            Utente ut = new Utente();
            String nome = faker.name().firstName();
            String cognome = faker.name().lastName();
            String numeroUnico = faker.number().digits(4);

            ut.setNomeCompleto(nome + " " + cognome);
            ut.setUsername((nome + "_" + cognome + "_" + numeroUnico).toLowerCase().replaceAll("\\s+", ""));
            ut.setEmail((faker.internet().username() + "_" + numeroUnico + "@" + faker.internet().domainName()).toLowerCase().replaceAll("\\s+", ""));

            try {
                prenotazioneService.salvaUtente(ut);
            } catch (SaveErrorException ex) {
                System.out.println("Errore durante il salvataggio per : " + ut.getUsername() + " Motivo: " + ex.getMessage());
            }
        }


    }
}
