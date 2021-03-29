package net.oups.new_years_revolution.adapters.api;

import net.oups.new_years_revolution.application.HelloService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @RequestMapping("/")
    public String index() {
        return "Hello World!";
    }

    @GetMapping("/bonsoir")
    public String bonsoir(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "bonsoir " + name;
    }

    @GetMapping("/bonsoirDB")
    public String bonsoir() {
        List<String> listLocations = helloService.locations()
            .stream()
            .map(p-> p.getName())
            .collect(Collectors.toList());
        String res = "Bonsoir " + String.join(", ", listLocations) + " ! Yeeeeeeaaaaaah";
        return res;
    }
}

