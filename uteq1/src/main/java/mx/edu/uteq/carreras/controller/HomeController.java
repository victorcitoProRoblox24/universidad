package mx.edu.uteq.carreras.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.uteq.carreras.service.CatalogoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CatalogoService catalogoService;

    /** HU-01: oferta educativa agrupada por división. */
    @GetMapping("/")
    public String inicio(@RequestParam(value = "sinPermiso", required = false) String sinPermiso, Model model) {
        model.addAttribute("divisiones", catalogoService.divisionesVisibles());
        model.addAttribute("carreras", catalogoService.carrerasVisibles());
        if (sinPermiso != null) {
            model.addAttribute("mensajeError", "No tienes permiso para entrar a esa sección.");
        }
        return "index";
    }
}
