package net.oups.new_years_revolution.infrastructure.persistence;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class Initializer {

        private final AccountRepository accountRepository;
        private final ResolutionRepository resolutionRepository;
        private final PasswordEncoder passwordEncoder;

        public Initializer(AccountRepository accountRepository, ResolutionRepository resolutionRepository, PasswordEncoder passwordEncoder) {
            this.accountRepository = accountRepository;
            this.resolutionRepository = resolutionRepository;
            this.passwordEncoder = passwordEncoder;
        }

        @PostConstruct
        public void initialize() {
            // Définition des comptes par défaut
            if (accountRepository.findAll().isEmpty()) {
                accountRepository.saveAndFlush(new Account("admin", passwordEncoder.encode("admin12345"), "ADMIN"));
                accountRepository.saveAndFlush(new Account("user", passwordEncoder.encode("user12345"), "USER"));
            }

            // Définition des résolutions par défaut
            if (resolutionRepository.findAll().isEmpty()) {
                resolutionRepository.saveAndFlush(new Resolution("Je dois me coucher avant minuit au moins 5 fois par semaine", 5, 1, accountRepository.findByLogin("admin")));
                resolutionRepository.saveAndFlush(new Resolution("Je dois faire du sport 2 fois par semaine", 2, 1, accountRepository.findByLogin("admin")));
                resolutionRepository.saveAndFlush(new Resolution("Je dois lire un livre une fois par mois", 1, 4, accountRepository.findByLogin("admin")));
            }
        }
}
