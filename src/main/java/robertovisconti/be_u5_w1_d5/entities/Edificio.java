package robertovisconti.be_u5_w1_d5.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter


@Table(name = "edificio")
public class Edificio {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id_edificio")
    private UUID id;

    @Column(name = "nome_edificio", nullable = false, length = 30)
    private String nomeEdificio;

    @Column(nullable = false)
    private String indirizzo;

    @Column(nullable = false)
    private String citta;

    @OneToMany(mappedBy = "edificio")
    private List<Postazione> postazioni;

    public Edificio(String nomeEdificio, String indirizzo, String citta) {
        this.nomeEdificio = nomeEdificio;
        this.indirizzo = indirizzo;
        this.citta = citta;
    }


    @Override
    public String toString() {
        return "Edificio " +
                "id= " + id +
                " | nomeEdificio= '" + nomeEdificio + '\'' +
                " | indirizzo= '" + indirizzo + '\'' +
                " | citta= '" + citta + '\'';
    }


}
