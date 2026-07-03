package robertovisconti.be_u5_w1_d5.runners;


import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import robertovisconti.be_u5_w1_d5.entities.Edificio;
import robertovisconti.be_u5_w1_d5.entities.Postazione;
import robertovisconti.be_u5_w1_d5.entities.Utente;
import robertovisconti.be_u5_w1_d5.enums.StatoPostazione;
import robertovisconti.be_u5_w1_d5.enums.TipoPostazione;
import robertovisconti.be_u5_w1_d5.exceptions.SaveErrorException;
import robertovisconti.be_u5_w1_d5.services.AziendaService;
import robertovisconti.be_u5_w1_d5.services.PrenotazioneService;

import java.util.List;

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
        // popolo il DB con controllo
        List<Edificio> edificiDB = aziendaService.recuperoEdifici();
        boolean utentiInseriti = prenotazioneService.utentiPresenti();

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
