package net.oups.new_years_revolution.application;


import net.oups.new_years_revolution.infrastructure.dto.ResolutionDTO;
import net.oups.new_years_revolution.infrastructure.dto.ValidationDTO;
import net.oups.new_years_revolution.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public List<Validation> getAllValidationsForAccountAndResolution(Account account, Resolution resolution) {
        return repository.findByCreatorIdAndResolutionId(account.getId(), resolution.getId());
    }

    public Validation getValidationByCreatorIdAndResolutionIdAndDate(Long creaId, Long resId, Date date) {
        Optional<Validation> val = repository.findByCreatorIdAndResolutionIdAndDate(creaId, resId, date);
        if (val.isPresent()) {
            return val.get();
        } else {
            return null;
        }
    }

    public Validation createOrUpdateValidation(Account creator, Resolution resolution, ValidationDTO validationDTO) {
        Validation val = getValidationByCreatorIdAndResolutionIdAndDate(creator.getId(), resolution.getId(), validationDTO.getDate());
        if (val == null) {
            // Créer la nouvelle validation
            val = new Validation(creator, resolution, validationDTO.getDate(), validationDTO.getOccurence());
            repository.saveAndFlush(val);
        } else {
            // Modifier la validation existante
            val.setOccurence(validationDTO.getOccurence());
            repository.save(val);
        }
        return val;
    }


}