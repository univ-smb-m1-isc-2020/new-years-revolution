package net.oups.new_years_revolution.infrastructure.persistence;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
public class Initializer {

        private final AccountRepository accountRepository;
        private final ResolutionRepository resolutionRepository;
        private final InscriptionRepository inscriptionRepository;
        private final PasswordEncoder passwordEncoder;

        public Initializer(AccountRepository accountRepository, ResolutionRepository resolutionRepository, InscriptionRepository inscriptionRepository, PasswordEncoder passwordEncoder) {
            this.accountRepository = accountRepository;
            this.resolutionRepository = resolutionRepository;
            this.inscriptionRepository = inscriptionRepository;
            this.passwordEncoder = passwordEncoder;
        }

        @PostConstruct
        public void initialize() {
            if (accountRepository.findAll().isEmpty() && resolutionRepository.findAll().isEmpty() && inscriptionRepository.findAll().isEmpty()) {

                // Valeurs par défaut
                Account admin = new Account("admin", passwordEncoder.encode("admin12345"), AccountRole.ROLE_ADMIN);
                Account user = new Account("user", passwordEncoder.encode("user12345"), AccountRole.ROLE_USER);

                Resolution res1 = new Resolution("Je dois me coucher avant minuit au moins 5 fois par semaine", 5, 1, admin);
                Resolution res2 = new Resolution("Je dois faire du sport 2 fois par semaine", 2, 1, admin);
                Resolution res3 = new Resolution("Je dois lire un livre une fois par mois", 1, 4, admin);

                Inscription ins1 = new Inscription(admin, res1, new Date());
                Inscription ins2 = new Inscription(admin, res2, new Date());
                Inscription ins3 = new Inscription(admin, res3, new Date());

                // Définition des comptes par défaut
                accountRepository.saveAndFlush(admin);
                accountRepository.saveAndFlush(user);

                // Définition des résolutions par défaut
                resolutionRepository.saveAndFlush(res1);
                resolutionRepository.saveAndFlush(res2);
                resolutionRepository.saveAndFlush(res3);

                // Définition des inscrits par défaut
                inscriptionRepository.saveAndFlush(ins1);
                inscriptionRepository.saveAndFlush(ins2);
                inscriptionRepository.saveAndFlush(ins3);
            }

        }
}
