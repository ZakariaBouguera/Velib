package fr.projetVelin.controller;

import fr.projetVelin.dto.Station;
import fr.projetVelin.service.VelibService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class VelibController {

    private final VelibService velibService;

    public VelibController(VelibService velibService) {
        this.velibService = velibService;
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) Double latitude,
                        @RequestParam(required = false) Double longitude,
                        Model model) {
        if (latitude != null && longitude != null) {
            List<Station> stations = velibService.recupStations(latitude, longitude);
            model.addAttribute("stations", stations);
            model.addAttribute("hasLocation", true);
        } else {
            model.addAttribute("hasLocation", false);
        }
        return "index";
    }
}