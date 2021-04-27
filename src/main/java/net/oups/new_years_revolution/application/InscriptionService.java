package net.oups.new_years_revolution.application;


import net.oups.new_years_revolution.infrastructure.dto.ResolutionDTO;
import net.oups.new_years_revolution.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public ArrayList<Resolution> getAccountResolutions(Account account) {
        ArrayList<Resolution> resolutions = new ArrayList<>();
        List<Inscription> inscrits = repository.findByInscritId(account.getId());
        for (Inscription inscrit : inscrits) {
            resolutions.add(inscrit.getResolution());
        }
        return resolutions;
    }

    public ArrayList<Account> getResolutionInscrits(Resolution resolution) {
        ArrayList<Account> accounts = new ArrayList<>();
        List<Inscription> inscrits = repository.findByResolutionId(resolution.getId());
        for (Inscription inscrit : inscrits) {
            accounts.add(inscrit.getInscrit());
        }
        return accounts;
    }
}