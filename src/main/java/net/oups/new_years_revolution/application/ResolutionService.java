package net.oups.new_years_revolution.application;


import net.oups.new_years_revolution.infrastructure.dto.ResolutionDTO;
import net.oups.new_years_revolution.infrastructure.persistence.Account;
import net.oups.new_years_revolution.infrastructure.persistence.AccountRepository;
import net.oups.new_years_revolution.infrastructure.persistence.Resolution;
import net.oups.new_years_revolution.infrastructure.persistence.ResolutionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        System.out.println("salut je m'inscris");
        repository.saveAndFlush(newRes);

        return newRes;
    }

    public Resolution getResolutionById(Long id) {
        Optional<Resolution> res = repository.findById(id);
        if (res.isPresent()) {
            return res.get();
        } else {
            return null;
        }
    }

    public List<Resolution> randomResolutions(int nbRandomRes) {
        List<Resolution> temp = repository.findAll();
        int tempSize = temp.size();
        List<Resolution> resultList = new ArrayList<>();

        for(int i = 0; i < nbRandomRes; i++){
            // On itère x fois dans la liste pour vérifier que l'on ajoute pas deux fois la même résolution.
            // Si au bout de 10 essais nous n'avons pas de résolution unique, on saute l'ajout.
            for (int randCount = 0; randCount < 10; randCount++) {
                int randNb = (int)(Math.random() * tempSize);
                // Si l'entrée est unique, on l'ajoute à la resultList et on sort de la boucle
                if (!resultList.contains(temp.get(randNb))) {
                    resultList.add(temp.get(randNb));
                    break;
                }
            }
        }
        return resultList;
    }
}