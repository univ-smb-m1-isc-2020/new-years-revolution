package net.oups.new_years_revolution.adapters.web;

import net.oups.new_years_revolution.application.AccountService;
import net.oups.new_years_revolution.application.InscriptionService;
import net.oups.new_years_revolution.application.ResolutionService;
import net.oups.new_years_revolution.application.ValidationService;
import net.oups.new_years_revolution.infrastructure.dto.ResolutionDTO;
import net.oups.new_years_revolution.infrastructure.dto.ValidationDTO;
import net.oups.new_years_revolution.infrastructure.exceptions.AccountDoesNotExistException;
import net.oups.new_years_revolution.infrastructure.persistence.Account;
import net.oups.new_years_revolution.infrastructure.persistence.Inscription;
import net.oups.new_years_revolution.infrastructure.persistence.Resolution;
import net.oups.new_years_revolution.infrastructure.persistence.Validation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        List<Resolution> listRandomResolutions = resolutionService.randomResolutions(5);

        // Si on a créé une nouvelle résolution
        if (resolutionCree != null) {
            dashboard.addObject("resolutionCree", resolutionCree);
        }

        // Si on a eu une erreur
        if (error != null) {
            dashboard.addObject("error", error);
        }

        dashboard.addObject("resolutionsList", listRandomResolutions);
        return dashboard;
    }
}