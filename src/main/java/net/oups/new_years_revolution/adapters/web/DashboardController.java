package net.oups.new_years_revolution.adapters.web;

import net.oups.new_years_revolution.application.HelloService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class DashboardController {

    @GetMapping("/dashboard/")
    public ModelAndView showDefaultDashboard(Model model) {
        ModelAndView dashboard = new ModelAndView("dashboard");
        return dashboard;
    }
}