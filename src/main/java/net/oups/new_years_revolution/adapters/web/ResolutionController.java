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
public class ResolutionController {

    private final ResolutionService resolutionService;
    private final AccountService accountService;
    private final ValidationService validationService;
    private final InscriptionService inscriptionService;

    public ResolutionController(ResolutionService resolutionService, AccountService accountService, ValidationService validationService, InscriptionService inscriptionService) {
        this.resolutionService = resolutionService;
        this.accountService = accountService;
        this.validationService = validationService;
        this.inscriptionService = inscriptionService;
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
            Inscription inscription = inscriptionService.registerNewInscription(account, resolution, new Date());
            return new ModelAndView("redirect:/dashboard/", "resolutionCree", resolution.getContenu());
        } catch (AccountDoesNotExistException e) {
            return new ModelAndView("redirect:/dashboard/", "error", "Erreur : Le compte \"" + username + "\" n'existe pas.");
        }

    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = "/dashboard/resolution/{resId}")
    public ModelAndView showResolutionById(Model model, @PathVariable(value = "resId") String resId, @RequestParam(required = false) String error, @RequestParam(required = false) String success) {
        ModelAndView modelAndViewRes = new ModelAndView("resolution");
        Resolution resolution = resolutionService.getResolutionById(Long.parseLong(resId));
        Account account;
        try {
            account = accountService.getAccountByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (AccountDoesNotExistException ex) {
            return new ModelAndView("redirect:/dashboard/", "error", "Erreur : Votre compte n'existe pas.");
        }


        // Si la résolution n'existe pas
        if (resolution == null) {
            return new ModelAndView("redirect:/dashboard/", "error", "Erreur : La résolution n°" + resId + " n'existe pas.");
        }

        Inscription inscription = inscriptionService.getInscriptionForAccountAndResolution(account, resolution);
        if (inscription != null) {
            modelAndViewRes.addObject("isMyResolution", true);
            ValidationDTO validationDTO = new ValidationDTO();
            modelAndViewRes.addObject("validationDTO", validationDTO);
            modelAndViewRes.addObject("inscriptionInfo", inscription);
        } else {
            modelAndViewRes.addObject("shouldSubscribe", true);
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
        modelAndViewRes.addObject("validationsList", validationService.getAllValidationsForAccountAndResolution(account, resolution));

        return modelAndViewRes;
    }

    @PostMapping("/dashboard/resolution/{resId}/subscribe")
    public ModelAndView subscribeToResolution(Model model, @PathVariable(value = "resId") String resId) {
        Resolution resolution = resolutionService.getResolutionById(Long.parseLong(resId));
        Account account;
        try {
            account = accountService.getAccountByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (AccountDoesNotExistException ex) {
            return new ModelAndView("redirect:/dashboard/", "error", "Erreur : Votre compte n'existe pas.");
        }

        Inscription inscription = inscriptionService.getInscriptionForAccountAndResolution(account, resolution);
        if (inscription != null) {
            return new ModelAndView("redirect:/dashboard/resolution/"+resId, "error", "Erreur : Vous êtes déjà abonné à cette résolution.");
        } else {
            inscription = inscriptionService.registerNewInscription(account, resolution, new Date());
            return new ModelAndView("redirect:/dashboard/resolution/"+resId, "success", "Succès : Vous êtes maintenant abonné à cette résolution.");
        }
    }

    @PostMapping("/dashboard/resolution/{resId}/validation")
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

        Inscription inscription = inscriptionService.getInscriptionForAccountAndResolution(account, resolution);

        // Si le compte n'est pas inscrit/abonné à la résolution
        if (inscription == null) {
            return new ModelAndView("redirect:/dashboard/resolution/"+resId, "error", "Erreur : Vous n'êtes pas abonné à la résolution.");
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

        // On refuse la validation si la date est inférieure à la date d'abonnement
        if (validationDTO.getDate().compareTo(inscription.getDateInscription()) < 0) {
            return new ModelAndView("redirect:/dashboard/resolution/"+resId, "error", "Erreur : Vous ne pouvez pas valider à une date inférieure au " + DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.FRENCH).format(inscription.getDateInscription()) + " (date d'abonnement à la résolution).");
        }

        // Création ou mise à jour de la validation
        Validation validation = validationService.createOrUpdateValidation(account, resolution, validationDTO);

        return new ModelAndView("redirect:/dashboard/resolution/"+resId, "success","Validation réussie.");
    }
}
