package mx.edu.uteq.carreras.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.uteq.carreras.entity.Carrera;
import mx.edu.uteq.carreras.entity.Division;
import mx.edu.uteq.carreras.service.SecretariaCarreraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/secretaria")
@RequiredArgsConstructor
public class SecretariaCarreraController {

    private final SecretariaCarreraService secretariaCarreraService;

    @GetMapping("/divisiones/{id}/carreras")
    public String carreras(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("division", secretariaCarreraService.buscarDivision(id));
            model.addAttribute("carreras", secretariaCarreraService.carrerasDeDivision(id));
            return "secretaria/carreras";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/secretaria";
        }
    }

    @PostMapping("/carreras/{id}/toggle")
    public String cambiarEstado(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Carrera carrera = secretariaCarreraService.cambiarEstadoCarrera(id);
            Division division = carrera.getDivision();

            String mensaje;
            if (carrera.isActiva()) {
                mensaje = "Se habilitó la carrera «" + carrera.getNombre() + "».";
                if (!division.isActiva()) {
                    mensaje += " Se verá cuando su división esté habilitada.";
                }
            } else {
                mensaje = "Se deshabilitó la carrera «" + carrera.getNombre() + "».";
            }
            redirectAttributes.addFlashAttribute("mensajeExito", mensaje);
            return "redirect:/secretaria/divisiones/" + division.getId() + "/carreras";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/secretaria";
        }
    }
}
