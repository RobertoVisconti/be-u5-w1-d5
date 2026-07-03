package robertovisconti.be_u5_w1_d5.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter

@Table(name = "prenotazione",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id_postazione", "data"}),
                @UniqueConstraint(columnNames = {"id_utente", "data"})
        })


public class Prenotazione {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id_prenotazione")
    private UUID id;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "id_postazione")
    private Postazione postazione;

    public Prenotazione(LocalDateTime data, Utente utente, Postazione postazione) {
        this.data = data;
        this.utente = utente;
        this.postazione = postazione;
    }

    @Override
    public String toString() {
        return "Prenotazione : " +
                " id= " + id +
                " | data= " + data;
    }
}
