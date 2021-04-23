package net.oups.new_years_revolution.adapters.web;

import net.oups.new_years_revolution.application.AccountService;
import net.oups.new_years_revolution.application.ResolutionService;
import net.oups.new_years_revolution.application.ValidationService;
import net.oups.new_years_revolution.infrastructure.dto.ResolutionDTO;
import net.oups.new_years_revolution.infrastructure.dto.ValidationDTO;
import net.oups.new_years_revolution.infrastructure.exceptions.AccountDoesNotExistException;
import net.oups.new_years_revolution.infrastructure.persistence.Account;
import net.oups.new_years_revolution.infrastructure.persistence.Resolution;
import net.oups.new_years_revolution.infrastructure.persistence.Validation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
public class DashboardController {

    private final ResolutionService resolutionService;
    private final AccountService accountService;
    private final ValidationService validationService;

    public DashboardController(ResolutionService resolutionService, AccountService accountService, ValidationService validationService) {
        this.resolutionService = resolutionService;
        this.accountService = accountService;
        this.validationService = validationService;
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

    @GetMapping("/dashboard/create")
    public ModelAndView createNewResolution(Model model, @RequestParam(required = false) String error) {
        ModelAndView ajoutResolution = new ModelAndView("ajoutResolution");
        ResolutionDTO resolutionDTO = new ResolutionDTO();

        ajoutResolution.addObject("resolution", resolutionDTO);

        // Si on a eu une erreur
        if (error != null) {
            ajoutResolution.addObject("error", error);
        }

        return ajoutResolution;
    }

    @PostMapping(value = "/dashboard/creationResolution")
    public ModelAndView creationNewResolution(@ModelAttribute("resolution") ResolutionDTO resolutionDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            Account account = accountService.getAccountByLogin(username);
            Resolution resolution = resolutionService.registerNewResolution(resolutionDTO, account);
            return new ModelAndView("redirect:/dashboard/", "resolutionCree", resolution.getContenu());
        } catch (AccountDoesNotExistException e) {
            return new ModelAndView("redirect:/dashboard/", "error", "Erreur : Le compte \"" + username + "\" n'existe pas.");
        }

    }

    @GetMapping("/dashboard/resolution/{resId}")
    public ModelAndView showResolutionById(Model model, @PathVariable(value = "resId") String resId, @RequestParam(required = false) String error, @RequestParam(required = false) String success) {
        ModelAndView modelAndViewRes = new ModelAndView("resolution");
        Resolution resolution = resolutionService.getResolutionById(Long.parseLong(resId));

        // Si la résolution n'existe pas
        if (resolution == null) {
            return new ModelAndView("redirect:/dashboard/", "error", "Erreur : La résolution n°" + resId + " n'existe pas.");
        }

        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(resolution.getCreator().getLogin())) {
            modelAndViewRes.addObject("isMyResolution", true);
            ValidationDTO validationDTO = new ValidationDTO();
            modelAndViewRes.addObject("validationDTO", validationDTO);
        }

        // Si on a eu une erreur
        if (error != null) {
            modelAndViewRes.addObject("error", error);
        }

        // Si on a réussi quelque chose
        if (success != null) {
            modelAndViewRes.addObject("success", success);
        }

        modelAndViewRes.addObject("resolution", resolution);
        modelAndViewRes.addObject("validationsList", validationService.getAllValidationsForResolution(resolution));

        return modelAndViewRes;
    }

    @PostMapping("/dashboard/resolution/{resId}")
    public ModelAndView validateResolutionById(Model model, @PathVariable(value = "resId") String resId, @ModelAttribute("validationDTO") ValidationDTO validationDTO) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Resolution resolution;
        Account account;
        try {
        resolution = resolutionService.getResolutionById(Long.parseLong(resId));
        account = accountService.getAccountByLogin(username);
        } catch (AccountDoesNotExistException e) {
            return new ModelAndView("redirect:/dashboard/", "error", "Erreur : Le compte \"" + username + "\" n'existe pas.");
        }

        // Si la résolution n'existe pas
        if (resolution == null) {
            return new ModelAndView("redirect:/dashboard/", "error", "Erreur : La résolution n°" + resId + " n'existe pas.");
        }

        // Si le compte ne matche pas à celui qui détient la résolution
        if (account.getId() != resolution.getCreator().getId()) {
            return new ModelAndView("redirect:/dashboard/resolution/"+resId, "error", "Erreur : La résolution ne vous appartient pas.");
        }

        // Calcul de la date limite (une semaine avant)
        LocalDate sevenDaysBeforeLD = LocalDate.now().minusDays(7);
        Date sevenDaysBefore = Date.from(sevenDaysBeforeLD.atStartOfDay().toInstant(OffsetDateTime.now().getOffset()));

        // On refuse la validation si la date est trop ancienne (plus d'une semaine)
        if (validationDTO.getDate().compareTo(sevenDaysBefore) < 0) {
            return new ModelAndView("redirect:/dashboard/resolution/"+resId, "error", "Erreur : Vous ne pouvez pas valider à une date inférieure au " + DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.FRENCH).format(sevenDaysBefore) + ".");
        }

        // Calcul de la date limite (après aujourd'hui)
        LocalDate nowLD = LocalDate.now();
        Date now = Date.from(nowLD.atStartOfDay().toInstant(OffsetDateTime.now().getOffset()));

        // On refuse la validation si la date est trop ancienne (plus d'une semaine)
        if (validationDTO.getDate().compareTo(now) > 0) {
            return new ModelAndView("redirect:/dashboard/resolution/"+resId, "error", "Erreur : Vous ne pouvez pas valider à une date supérieure au " + DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.FRENCH).format(now) + ".");
        }

        // Création ou mise à jour de la validation
        Validation validation = validationService.createOrUpdateValidation(account, resolution, validationDTO);

        return new ModelAndView("redirect:/dashboard/resolution/"+resId, "success","Validation réussie.");
    }
}