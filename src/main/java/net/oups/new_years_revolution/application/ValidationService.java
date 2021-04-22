package net.oups.new_years_revolution.application;


import net.oups.new_years_revolution.infrastructure.dto.ResolutionDTO;
import net.oups.new_years_revolution.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationService {

    private final ValidationRepository repository;
    private final ResolutionRepository resolutionRepository;
    private final AccountRepository accountRepository;

    public ValidationService(ValidationRepository repository, ResolutionRepository resolutionRepository, AccountRepository accountRepository) {
        this.repository = repository;
        this.resolutionRepository = resolutionRepository;
        this.accountRepository = accountRepository;
    }

    public List<Validation> getAllValidations() {
        return repository.findAll();
    }

    public List<Validation> getAllValidationsForResolution(Resolution resolution) {
        return repository.findByResolutionId(resolution.getId());
    }

    public List<Validation> getAllValidationsForAccount(Account account) {
        return repository.findByCreatorId(account.getId());
    }
}