package net.oups.new_years_revolution.adapters.web;

import net.oups.new_years_revolution.application.AccountService;
import net.oups.new_years_revolution.infrastructure.exceptions.AccountAlreadyExistsException;
import net.oups.new_years_revolution.infrastructure.exceptions.AccountPasswordNotMatchException;
import net.oups.new_years_revolution.infrastructure.persistence.Account;
import net.oups.new_years_revolution.infrastructure.dto.AccountDTO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;

@Controller
public class LoginController {

    private final AccountService accountService;

    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping(value = "/register")
    public ModelAndView registerForm() {
        ModelAndView modelAndView = new ModelAndView("register");
        AccountDTO accountDTO = new AccountDTO();
        modelAndView.addObject("account", accountDTO);

        modelAndView.addObject("label.login", "Identifiant");

        return modelAndView;
    }

    @PostMapping(value = "/registrationUser")
    public ModelAndView registerUser(@ModelAttribute("account") AccountDTO accountDTO) {
        ModelAndView modelAndViewRegister = new ModelAndView("register");

        try {
            Account registered = accountService.registerNewAccountUser(accountDTO);
        } catch (AccountAlreadyExistsException alreadyExistsEx) {
            modelAndViewRegister.addObject("login_error", "Ce login est déjà utilisé par un autre compte.");
            return modelAndViewRegister;
        } catch (AccountPasswordNotMatchException passwordNotMatchEx) {
            modelAndViewRegister.addObject("password_error", "Les mots de passe ne sont pas identiques.");
            return modelAndViewRegister;
        }

        return new ModelAndView("login", "registerLogin", accountDTO.getLogin());
    }

}