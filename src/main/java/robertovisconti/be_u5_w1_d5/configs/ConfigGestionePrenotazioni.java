package robertovisconti.be_u5_w1_d5.configs;


import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class ConfigGestionePrenotazioni {

    @Bean
    public Faker getFaker() {
        return new Faker(new Locale("it-IT"));
    }
    
}
