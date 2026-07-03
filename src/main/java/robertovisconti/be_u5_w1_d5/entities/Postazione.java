package robertovisconti.be_u5_w1_d5.entities;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import robertovisconti.be_u5_w1_d5.enums.StatoPostazione;
import robertovisconti.be_u5_w1_d5.enums.TipoPostazione;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "postazione")
public class Postazione {

    @Id
    @GeneratedValue
    @Column(name = "id_postazione")
    private UUID id;

    @Column(name = "codice_univoco", nullable = false, unique = true)
    private UUID codiceUnivoco = UUID.randomUUID();

    @Column(nullable = false, length = 200)
    private String descrizione;

    @Column(name = "tipo_postazione", nullable = false)
    private TipoPostazione tipoPostazione;

    @Column(name = "stato_postazione", nullable = false)
    private StatoPostazione statoPostazione;

    @Column(name = "numero_massimo_occupanti", nullable = false)
    private int numeroMassimoOccupanti;

    @ManyToOne
    @JoinColumn(name = "id_edificio")
    private Edificio edificio;

    public Postazione(UUID codiceUnivoco, String descrizione, TipoPostazione tipoPostazione, StatoPostazione statoPostazione, int numeroMassimoOccupanti) {
        this.codiceUnivoco = codiceUnivoco;
        this.descrizione = descrizione;
        this.tipoPostazione = tipoPostazione;
        this.statoPostazione = statoPostazione;
        this.numeroMassimoOccupanti = numeroMassimoOccupanti;
    }

    @Override
    public String toString() {
        return "Postazione " +
                " id= " + id +
                " | codiceUnivoco=" + codiceUnivoco +
                " | descrizione='" + descrizione + '\'' +
                " | tipoPostazione=" + tipoPostazione +
                " | statoPostazione=" + statoPostazione +
                " | numeroMassimoOccupanti=" + numeroMassimoOccupanti +
                " | edificio=" + edificio;
    }
}
