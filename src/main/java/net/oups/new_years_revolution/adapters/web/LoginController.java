package net.oups.new_years_revolution.adapters.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

}