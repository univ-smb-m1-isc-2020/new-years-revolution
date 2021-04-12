package net.oups.new_years_revolution.application;


import net.oups.new_years_revolution.infrastructure.persistence.AccountRepository;
import net.oups.new_years_revolution.infrastructure.persistence.Resolution;
import net.oups.new_years_revolution.infrastructure.persistence.ResolutionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ResolutionService {

    private final ResolutionRepository repository;
    private final AccountRepository accountRepository;

    public ResolutionService(ResolutionRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    public List<Resolution> locations() {
        return repository.findAll();
    }

    public List<Resolution> randomResolutions(int nbRandomRes) {
        return repository.findAll();
    }
}