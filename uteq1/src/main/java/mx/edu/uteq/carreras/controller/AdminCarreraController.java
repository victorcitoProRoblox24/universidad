package mx.edu.uteq.carreras.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.uteq.carreras.entity.Carrera;
import mx.edu.uteq.carreras.entity.Division;
import mx.edu.uteq.carreras.entity.NivelCarrera;
import mx.edu.uteq.carreras.service.CarreraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCarreraController {
    private final CarreraService carreraService;

    @InitBinder({"nuevaCarrera", "carrera"})
    public void camposEditables(WebDataBinder binder) {
        binder.setAllowedFields("nombre", "clave", "nivel", "descripcion");
    }

    @GetMapping("/divisiones/{id}/carreras")
    public String listar(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Division division = carreraService.buscarDivision(id);
            model.addAttribute("division", division);
            model.addAttribute("carreras", carreraService.listarPorDivision(id));
            if (!model.containsAttribute("nuevaCarrera")) {
                model.addAttribute("nuevaCarrera", new Carrera());
            }
            model.addAttribute("niveles", NivelCarrera.values());
            return "admin/carreras";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/admin";
        }
    }

    @PostMapping("/divisiones/{id}/carreras/guardar")
    public String guardar(@PathVariable("id") Long id,
                          @ModelAttribute("nuevaCarrera") Carrera carrera, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Selecciona un nivel de carrera válido.");
            }
            carreraService.crear(id, carrera);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Carrera agregada. Quedará oculta hasta que Secretaría Académica la habilite.");
        } catch (IllegalArgumentException ex) {
            carrera.setId(null);
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            redirectAttributes.addFlashAttribute("nuevaCarrera", carrera);
        }
        return "redirect:/admin/divisiones/" + id + "/carreras";
    }

    @GetMapping("/carreras/{id}/editar")
    public String editar(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("carrera", carreraService.buscarPorId(id));
            model.addAttribute("niveles", NivelCarrera.values());
            return "admin/carrera-form";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/admin";
        }
    }

    @PostMapping("/carreras/{id}/actualizar")
    public String actualizar(@PathVariable("id") Long id,
                             @ModelAttribute("carrera") Carrera carrera, BindingResult bindingResult,
                             Model model, RedirectAttributes redirectAttributes) {
        Carrera original;
        try {
            original = carreraService.buscarPorId(id);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/admin";
        }

        try {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Selecciona un nivel de carrera válido.");
            }
            Carrera actualizada = carreraService.actualizar(id, carrera);
            redirectAttributes.addFlashAttribute("mensajeExito", "Carrera actualizada.");
            return "redirect:/admin/divisiones/" + actualizada.getDivision().getId() + "/carreras";
        } catch (IllegalArgumentException ex) {
            carrera.setId(id);
            carrera.setDivision(original.getDivision());
            model.addAttribute("carrera", carrera);
            model.addAttribute("niveles", NivelCarrera.values());
            model.addAttribute("mensajeError", ex.getMessage());
            return "admin/carrera-form";
        }
    }

    @PostMapping("/carreras/{id}/eliminar")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Long divisionId;
        try {
            divisionId = carreraService.buscarPorId(id).getDivision().getId();
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
            return "redirect:/admin";
        }

        try {
            carreraService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Carrera eliminada.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/admin/divisiones/" + divisionId + "/carreras";
    }
}
