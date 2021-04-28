package net.oups.new_years_revolution.adapters.web;

import net.oups.new_years_revolution.Application;
import net.oups.new_years_revolution.application.AccountService;
import net.oups.new_years_revolution.application.InscriptionService;
import net.oups.new_years_revolution.application.ResolutionService;
import net.oups.new_years_revolution.application.ValidationService;
import net.oups.new_years_revolution.infrastructure.dto.AccountDTO;
import net.oups.new_years_revolution.infrastructure.exceptions.AccountDoesNotExistException;
import net.oups.new_years_revolution.infrastructure.exceptions.AccountPasswordNotMatchException;
import net.oups.new_years_revolution.infrastructure.persistence.Account;
import net.oups.new_years_revolution.infrastructure.persistence.Inscription;
import net.oups.new_years_revolution.infrastructure.persistence.Resolution;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
public class AdminController {

    private final ResolutionService resolutionService;
    private final AccountService accountService;
    private final InscriptionService inscriptionService;
    private final ValidationService validationService;

    public AdminController(ResolutionService resolutionService, AccountService accountService, InscriptionService inscriptionService, ValidationService validationService) {
        this.resolutionService = resolutionService;
        this.accountService = accountService;
        this.inscriptionService = inscriptionService;
        this.validationService = validationService;
    }

    @GetMapping("/admin")
    public ModelAndView admin() {
        ModelAndView modelAndView = new ModelAndView("admin");

        // Stats web
        modelAndView.addObject("resolutionsCount", resolutionService.getResolutionCount());
        modelAndView.addObject("accountsCount", accountService.getAccountCount());

        // Stats serveur
        double totalMemory = (double) (Runtime.getRuntime().totalMemory()) / 1048576;
        double usedMemory =  (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;

        Date startDate = Application.getStartDate();

        modelAndView.addObject("usedMemory", new DecimalFormat("#.00").format(usedMemory));
        modelAndView.addObject("totalMemory", new DecimalFormat("#.00").format(totalMemory));
        modelAndView.addObject("startDate", startDate);

        return modelAndView;
    }

    //////////////////
    // UTILISATEURS //
    //////////////////

    @GetMapping("/admin/user")
    public ModelAndView adminUserList(@RequestParam(required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("adminUserList");

        List<Account> accountList = accountService.getAllAccounts();
        accountList.sort(Comparator.comparing(Account::getLogin));
        modelAndView.addObject("userList", accountList);

        // Si on a eu une erreur
        if (error != null) {
            modelAndView.addObject("error", error);
        }

        return modelAndView;
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = "/admin/user/{username}")
    public ModelAndView showUserPageByName(Model model, @PathVariable(value = "username") String username, @RequestParam(required = false) String error, @RequestParam(required = false) String success) {
        ModelAndView modelAndViewUser = new ModelAndView("adminUser");
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

        // Administration
        modelAndViewUser.addObject("accountDTO", new AccountDTO());

        // Si on a eu une erreur
        if (error != null) {
            modelAndViewUser.addObject("error", error);
        }

        // Si on a réussi quelque chose
        if (success != null) {
            modelAndViewUser.addObject("success", success);
        }

        return modelAndViewUser;
    }

    @PostMapping(value = "/admin/user/{username}/changePassword")
    public ModelAndView adminChangeUserPassword(@PathVariable(value = "username") String username, @ModelAttribute("accountDTO") AccountDTO accountDTO) {

        Account account;
        try {
            account = accountService.getAccountByLogin(username);
        } catch (AccountDoesNotExistException ex) {
            return new ModelAndView("redirect:/admin/user", "error", "Erreur : l'utilisateur " + username + " n'existe pas.");
        }

        try {
            accountService.changePassword(account, accountDTO);
        } catch (AccountPasswordNotMatchException passwordNotMatchEx){
            return new ModelAndView("redirect:/admin/user/" + username, "error", "Erreur : les mots de passe ne sont pas identiques.");
        }

        return new ModelAndView("redirect:/admin/user/" + username, "success", "Succès : Mot de passe du compte modifié.");

    }

    /////////////////
    // RESOLUTIONS //
    /////////////////

    @GetMapping("/admin/resolution")
    public ModelAndView adminResolutionList(@RequestParam(required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("adminResolutionList");

        List<Resolution> resolutionList = resolutionService.getAllResolutions();
        resolutionList.sort(Comparator.comparing(Resolution::getId));
        modelAndView.addObject("resolutionList", resolutionList);

        // Si on a eu une erreur
        if (error != null) {
            modelAndView.addObject("error", error);
        }

        return modelAndView;
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = "/admin/resolution/{resId}")
    public ModelAndView adminShowResolution(Model model, @PathVariable(value = "resId") String resId, @RequestParam(required = false) String error, @RequestParam(required = false) String success) {
        ModelAndView modelAndViewRes = new ModelAndView("adminResolution");
        Resolution resolution = resolutionService.getResolutionById(Long.parseLong(resId));

        // Si la résolution n'existe pas
        if (resolution == null) {
            return new ModelAndView("redirect:/admin/resolution/", "error", "Erreur : La résolution n°" + resId + " n'existe pas.");
        }

        List<Inscription> inscrits = inscriptionService.getInscritsForResolution(resolution);

        modelAndViewRes.addObject("inscrits", inscrits);
        modelAndViewRes.addObject("inscritsCount", inscrits.size());

        // Si on a eu une erreur
        if (error != null) {
            modelAndViewRes.addObject("error", error);
        }

        // Si on a réussi quelque chose
        if (success != null) {
            modelAndViewRes.addObject("success", success);
        }

        modelAndViewRes.addObject("resolution", resolution);

        return modelAndViewRes;
    }

    @PostMapping(value = "/admin/resolution/{resId}/supprimer")
    public ModelAndView adminDeleteResolution(@PathVariable(value = "resId") String resId) {

        Resolution resolution = resolutionService.getResolutionById(Long.parseLong(resId));

        // Si la résolution n'existe pas
        if (resolution == null) {
            return new ModelAndView("redirect:/admin/resolution", "error", "Erreur : La résolution n°" + resId + " n'existe pas.");
        }

        // Suppression de toutes les validations
        validationService.deleteAllValidationsForResolution(resolution);

        // Suppression de tous les inscrits à une résolution
        inscriptionService.deleteAllSubscribersForResolution(resolution);

        // Une fois les validations supprimées, suppression de la résolution en elle-même
        resolutionService.deleteResolution(resolution);

        return new ModelAndView("redirect:/admin/resolution", "success", "Succès : La résolution n°" + resId + " (" + resolution.getContenu() + ") a bien été supprimée.");

    }
}
