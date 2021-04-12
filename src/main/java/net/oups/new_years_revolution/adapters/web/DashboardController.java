package net.oups.new_years_revolution.adapters.web;

import net.oups.new_years_revolution.application.ResolutionService;
import net.oups.new_years_revolution.infrastructure.persistence.Resolution;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DashboardController {

    private final ResolutionService resolutionService;

    public DashboardController(ResolutionService resolutionService) {
        this.resolutionService = resolutionService;
    }

    @GetMapping("/dashboard/")
    public ModelAndView showDefaultDashboard(Model model) {
        ModelAndView dashboard = new ModelAndView("dashboard");

        List<Resolution> listRandomResolutions = resolutionService.randomResolutions(5);

        dashboard.addObject("resolutionsList", listRandomResolutions);
        return dashboard;
    }
}