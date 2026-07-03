package robertovisconti.be_u5_w1_d5.entities;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor

@Table(name = "utente")
public class Utente {

    @Id
    @GeneratedValue
    @Column(name = "id_utente")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "nome_completo", unique = true)
    private String nomeCompleto;

    @Column(nullable = false, unique = true)
    private String email;

    public Utente(String username, String nomeCompleto, String email) {
        this.username = username;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Utente " +
                " id= " + id +
                " | username= '" + username + '\'' +
                " | nomeCompleto= '" + nomeCompleto + '\'' +
                " | email= '" + email + '\'';
    }
}
