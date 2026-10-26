package mx.edu.uteq.carreras.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.uteq.carreras.entity.Division;
import mx.edu.uteq.carreras.service.AdminDivisionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Área de administración de divisiones. El acceso está restringido al rol
 * ADMIN por {@code AuthInterceptor} para las rutas {@code /admin/**}.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDivisionController {
    private final AdminDivisionService adminDivisionService;

    /** El formulario solo puede llenar estos campos; id, estado y fechas los decide el sistema. */
    @InitBinder("division")
    public void camposEditables(WebDataBinder binder) {
        binder.setAllowedFields("nombre", "clave", "descripcion");
    }

    /** HU-05: dashboard con el resumen de divisiones, carreras y aspirantes. */
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("resumen", adminDivisionService.resumen());
        model.addAttribute("divisiones", adminDivisionService.listarDivisiones());
        model.addAttribute("aspirantesRecientes", adminDivisionService.aspirantesRecientes());
        return "admin/dashboard";
    }

    /** HU-06: formulario para dar de alta una división. */
    @GetMapping("/divisiones/nueva")
    public String nueva(Model model) {
        model.addAttribute("division", new Division());
        return "admin/division-form";
    }

    /** HU-06: guarda la división nueva; queda deshabilitada hasta que Secretaría la habilite. */
    @PostMapping("/divisiones/guardar")
    public String guardar(@ModelAttribute("division") Division division, Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            Division creada = adminDivisionService.crear(division);
            redirectAttributes.addFlashAttribute("mensajeExito", "División " + creada.getClave()
                    + " creada. Quedará deshabilitada hasta que Secretaría Académica la habilite.");
            return "redirect:/admin";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "admin/division-form";
        }
    }
}
