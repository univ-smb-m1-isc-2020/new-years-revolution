package net.oups.new_years_revolution.application;


import net.oups.new_years_revolution.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InscriptionService {

    private final InscriptionRepository repository;
    private final ResolutionRepository resolutionRepository;
    private final AccountRepository accountRepository;

    public InscriptionService(InscriptionRepository repository, ResolutionRepository resolutionRepository, AccountRepository accountRepository) {
        this.repository = repository;
        this.resolutionRepository = resolutionRepository;
        this.accountRepository = accountRepository;
    }

    public Inscription registerNewInscription(Account account, Resolution resolution, Date date) {
        Inscription newIns = new Inscription(account, resolution, date);
        repository.saveAndFlush(newIns);
        return newIns;
    }

    public List<Inscription> getInscriptionsForAccount(Account account) {
        return repository.findByInscritId(account.getId());
    }

    public List<Inscription> getInscritsForResolution(Resolution resolution) {
        return repository.findByResolutionId(resolution.getId());
    }

    public List<ResolutionCount> getResolutionsCount(int nbInscriptionsAffichage) {
        List<ResolutionCount> resolutionCountList = repository.findInscriptionsOrderByCount();
        return resolutionCountList.subList(0, (nbInscriptionsAffichage < resolutionCountList.size() ? nbInscriptionsAffichage : resolutionCountList.size()));
    }

    public Inscription getInscriptionForAccountAndResolution(Account account, Resolution resolution) {
        Optional<Inscription> val = repository.findByInscritIdAndResolutionId(account.getId(), resolution.getId());
        if (val.isPresent()) {
            return val.get();
        } else {
            return null;
        }
    }
}