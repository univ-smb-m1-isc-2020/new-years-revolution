package net.oups.new_years_revolution.application;


import net.oups.new_years_revolution.infrastructure.dto.ResolutionDTO;
import net.oups.new_years_revolution.infrastructure.persistence.Account;
import net.oups.new_years_revolution.infrastructure.persistence.AccountRepository;
import net.oups.new_years_revolution.infrastructure.persistence.Resolution;
import net.oups.new_years_revolution.infrastructure.persistence.ResolutionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

@Service
public class ResolutionService {

    private final ResolutionRepository repository;
    private final AccountRepository accountRepository;

    public ResolutionService(ResolutionRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    public Resolution registerNewResolution(ResolutionDTO resolutionDTO, Account account) {
        Integer periode = (resolutionDTO.getPeriode() ? 1 : 0);
        Resolution newRes = new Resolution(resolutionDTO.getContenu(), resolutionDTO.getOccurence(), periode, account);
        repository.saveAndFlush(newRes);

        return newRes;
    }

    public List<Resolution> locations() {
        return repository.findAll();
    }

    public List<Resolution> randomResolutions(int nbRandomRes) {
        List<Resolution> temp = repository.findAll();
        int[] rando = new int[nbRandomRes];
        for(int i=0;i<nbRandomRes;i++){
            rando[i]= (int)(Math.random() * ((temp.size()) + 1));
        }
        //pour avoir une liste qui existe
        List<Resolution> tempTemp = temp;
        tempTemp.clear();
        //pas de test de doublon
        for(int i=0;i<nbRandomRes;i++){
            tempTemp.add(temp.get(rando[i]));
        }
        return tempTemp;
    }
}