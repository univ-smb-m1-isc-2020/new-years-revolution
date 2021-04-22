package net.oups.new_years_revolution.adapters.web;

import net.oups.new_years_revolution.application.AccountService;
import net.oups.new_years_revolution.application.ResolutionService;
import net.oups.new_years_revolution.infrastructure.dto.ResolutionDTO;
import net.oups.new_years_revolution.infrastructure.exceptions.AccountAlreadyExistsException;
import net.oups.new_years_revolution.infrastructure.exceptions.AccountDoesNotExistException;
import net.oups.new_years_revolution.infrastructure.exceptions.AccountPasswordNotMatchException;
import net.oups.new_years_revolution.infrastructure.persistence.Account;
import net.oups.new_years_revolution.infrastructure.persistence.Resolution;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
public class DashboardController {

    private final ResolutionService resolutionService;
    private final AccountService accountService;

    public DashboardController(ResolutionService resolutionService, AccountService accountService) {
        this.resolutionService = resolutionService;
        this.accountService = accountService;
    }

    @GetMapping("/dashboard/")
    public ModelAndView showDefaultDashboard(Model model, @RequestParam(required = false) String resolutionCree) {
        ModelAndView dashboard = new ModelAndView("dashboard");

        List<Resolution> listRandomResolutions = resolutionService.randomResolutions(5);

        // Si on a créé une nouvelle résolution
        if (resolutionCree != null) {
            dashboard.addObject("resolutionCree", resolutionCree);
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
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Account account = accountService.getAccountByLogin(username);
            Resolution resolution = resolutionService.registerNewResolution(resolutionDTO, account);
            return new ModelAndView("redirect:/dashboard/", "resolutionCree", resolution.getContenu());
        } catch (AccountDoesNotExistException e) {
            return new ModelAndView("redirect:/dashboard/create", "error", "Erreur : Votre compte n'a pas été trouvé. Veuillez vous déconnecter et vous reconnecter.");
        }

    }
}