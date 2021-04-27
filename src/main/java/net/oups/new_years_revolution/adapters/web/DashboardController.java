package net.oups.new_years_revolution.adapters.web;

import net.oups.new_years_revolution.application.AccountService;
import net.oups.new_years_revolution.application.InscriptionService;
import net.oups.new_years_revolution.application.ResolutionService;
import net.oups.new_years_revolution.application.ValidationService;
import net.oups.new_years_revolution.infrastructure.exceptions.AccountDoesNotExistException;
import net.oups.new_years_revolution.infrastructure.persistence.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class DashboardController {

    private final ResolutionService resolutionService;
    private final AccountService accountService;
    private final ValidationService validationService;
    private final InscriptionService inscriptionService;


    public DashboardController(ResolutionService resolutionService, AccountService accountService, ValidationService validationService, InscriptionService inscriptionService) {
        this.resolutionService = resolutionService;
        this.accountService = accountService;
        this.validationService = validationService;
        this.inscriptionService = inscriptionService;
    }

    @GetMapping({"/dashboard", "/dashboard/"})
    public ModelAndView showDefaultDashboard(Model model, @RequestParam(required = false) String resolutionCree, @RequestParam(required = false) String error) {
        ModelAndView dashboard = new ModelAndView("dashboard");

        Account account;
        try {
            account = accountService.getAccountByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (AccountDoesNotExistException e) {
            // On est connectés à un compte qui n'existe pas, on le déconnecte
            return new ModelAndView("redirect:/logout");
        }

        // Mes résolutions
        dashboard.addObject("mesInscriptionsList", inscriptionService.getInscriptionsForAccount(account));

        // Résolutions les plus prises
        dashboard.addObject("resolutionsPopulairesList", inscriptionService.getResolutionsCount(5));

        // Résolutions aléatoires
        dashboard.addObject("randomResolutionsList", resolutionService.randomResolutions(5));

        // Si on a créé une nouvelle résolution
        if (resolutionCree != null) {
            dashboard.addObject("resolutionCree", resolutionCree);
        }

        // Si on a eu une erreur
        if (error != null) {
            dashboard.addObject("error", error);
        }

        return dashboard;
    }
}