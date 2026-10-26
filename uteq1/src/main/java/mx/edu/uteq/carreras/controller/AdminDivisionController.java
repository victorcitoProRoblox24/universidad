package mx.edu.uteq.carreras.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.uteq.carreras.service.AdminDivisionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Área de administración de divisiones. El acceso está restringido al rol
 * ADMIN por {@code AuthInterceptor} para las rutas {@code /admin/**}.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDivisionController {
    private final AdminDivisionService adminDivisionService;

    /** HU-05: dashboard con el resumen de divisiones, carreras y aspirantes. */
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("resumen", adminDivisionService.resumen());
        model.addAttribute("divisiones", adminDivisionService.listarDivisiones());
        model.addAttribute("aspirantesRecientes", adminDivisionService.aspirantesRecientes());
        return "admin/dashboard";
    }
}
