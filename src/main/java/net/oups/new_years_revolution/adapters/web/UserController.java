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
import java.util.Locale;

@RestController
public class UserController {

    private final ResolutionService resolutionService;
    private final AccountService accountService;
    private final ValidationService validationService;
    private final InscriptionService inscriptionService;

    public UserController(ResolutionService resolutionService, AccountService accountService, ValidationService validationService, InscriptionService inscriptionService) {
        this.resolutionService = resolutionService;
        this.accountService = accountService;
        this.validationService = validationService;
        this.inscriptionService = inscriptionService;
    }

    // Raccourci pour la page de l'utilisateur courant
    @GetMapping("/dashboard/user/me")
    public ModelAndView getMyUserPage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username != null)
            return new ModelAndView("redirect:/dashboard/user/" + username);
        else return new ModelAndView("redirect:/dashboard/", "error", "Erreur : Votre compte n'existe pas.");
    }

    // Pages utilisateurs
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = "/dashboard/user/{username}")
    public ModelAndView showUserPageByName(Model model, @PathVariable(value = "username") String username) {
        ModelAndView modelAndViewUser = new ModelAndView("user");
        modelAndViewUser.addObject("username", username);

        Account account;
        try {
            account = accountService.getAccountByLogin(username);
        } catch (AccountDoesNotExistException ex) {
            modelAndViewUser.addObject("accountNotExists", true);
            return modelAndViewUser;
        }

        // Si on arrive ici, alors le compte existe, on le signale
        modelAndViewUser.addObject("accountNotExists", false);
        modelAndViewUser.addObject("account", account);

        // Liste des résolutions
        modelAndViewUser.addObject("inscriptionsList", inscriptionService.getInscriptionsForAccount(account));

        return modelAndViewUser;
    }
}
