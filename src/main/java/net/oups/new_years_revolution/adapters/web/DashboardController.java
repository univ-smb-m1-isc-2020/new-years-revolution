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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ModelAndView showDefaultDashboard(Model model) {
        ModelAndView dashboard = new ModelAndView("dashboard");

        List<Resolution> listRandomResolutions = resolutionService.randomResolutions(5);

        // Si on a créé une nouvelle résolution
        System.out.println(model.containsAttribute("resolutionCree"));
        if (model.containsAttribute("resolutionCree")) {
            dashboard.addObject("resolutionCree", model.getAttribute("resolutionCree").toString());
        }

        dashboard.addObject("resolutionsList", listRandomResolutions);
        return dashboard;
    }

    @GetMapping("/dashboard/create")
    public ModelAndView createNewResolution(Model model) {
        ModelAndView ajoutResolution = new ModelAndView("ajoutResolution");
        ResolutionDTO resolutionDTO = new ResolutionDTO();

        ajoutResolution.addObject("resolution", resolutionDTO);

        // Si on a eu une erreur
        System.out.println(model.containsAttribute("error"));
        if (model.containsAttribute("error")) {
            ajoutResolution.addObject("error", model.getAttribute("error").toString());
        }

        return ajoutResolution;
    }

    @PostMapping(value = "/dashboard/creationResolution")
    public View creationNewResolution(@ModelAttribute("resolution") ResolutionDTO resolutionDTO) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Account account = accountService.getAccountByLogin(username);
            Resolution resolution = resolutionService.registerNewResolution(resolutionDTO, account);
            RedirectView redirectViewDashboard = new RedirectView("/dashboard/", true);
            redirectViewDashboard.addStaticAttribute("resolutionCree", resolution.getContenu());
            return redirectViewDashboard;
        } catch (AccountDoesNotExistException e) {
            RedirectView redirectViewCreate = new RedirectView("/dashboard/create");
            redirectViewCreate.addStaticAttribute("error", "Erreur : Votre compte n'a pas été trouvé. Veuillez vous déconnecter et vous reconnecter.");
            return redirectViewCreate;
        }

    }
}