package mx.edu.uteq.carreras.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mx.edu.uteq.carreras.dto.AspiranteReciente;
import mx.edu.uteq.carreras.dto.DivisionDashboard;
import mx.edu.uteq.carreras.dto.ResumenAdmin;
import mx.edu.uteq.carreras.entity.Carrera;
import mx.edu.uteq.carreras.entity.Division;
import mx.edu.uteq.carreras.entity.Rol;
import mx.edu.uteq.carreras.entity.Usuario;
import mx.edu.uteq.carreras.repository.CarreraRepository;
import mx.edu.uteq.carreras.repository.DivisionRepository;
import mx.edu.uteq.carreras.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reglas del área de administración de divisiones:
 * <ul>
 *   <li>HU-05: dashboard con el resumen de divisiones, carreras y aspirantes.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class AdminDivisionService {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final DivisionRepository divisionRepository;
    private final CarreraRepository carreraRepository;
    private final UsuarioRepository usuarioRepository;

    /** HU-05: totales de las tarjetas superiores del dashboard. */
    @Transactional(readOnly = true)
    public ResumenAdmin resumen() {
        return new ResumenAdmin(
                divisionRepository.count(),
                divisionRepository.countByActivaTrue(),
                carreraRepository.count(),
                carreraRepository.countByActivaTrueAndDivisionActivaTrue(),
                usuarioRepository.countByRol(Rol.ASPIRANTE),
                usuarioRepository.countByRolAndCarreraInteresIsNull(Rol.ASPIRANTE));
    }

    /** HU-05: todas las divisiones con sus carreras y aspirantes interesados. */
    @Transactional(readOnly = true)
    public List<DivisionDashboard> listarDivisiones() {
        List<DivisionDashboard> filas = divisionRepository.findAllByOrderByNombreAsc().stream()
                .map(this::resumir)
                .toList();
        long maximoAspirantes = filas.stream().mapToLong(DivisionDashboard::aspirantes).max().orElse(0);
        if (maximoAspirantes == 0) {
            return filas;
        }
        // La barra de cada división es relativa a la división con más aspirantes.
        return filas.stream()
                .map(fila -> new DivisionDashboard(fila.id(), fila.clave(), fila.nombre(), fila.activa(),
                        fila.totalCarreras(), fila.carrerasActivas(), fila.aspirantes(),
                        (int) Math.round(fila.aspirantes() * 100.0 / maximoAspirantes)))
                .toList();
    }

    /** HU-05: los cinco aspirantes registrados más recientemente. */
    @Transactional(readOnly = true)
    public List<AspiranteReciente> aspirantesRecientes() {
        return usuarioRepository.findTop5ByRolOrderByFechaRegistroDesc(Rol.ASPIRANTE).stream()
                .map(this::aAspiranteReciente)
                .toList();
    }

    private DivisionDashboard resumir(Division division) {
        Long id = division.getId();
        return new DivisionDashboard(id, division.getClave(), division.getNombre(), division.isActiva(),
                carreraRepository.countByDivisionId(id),
                carreraRepository.countByDivisionIdAndActivaTrue(id),
                usuarioRepository.countByRolAndCarreraInteresDivisionId(Rol.ASPIRANTE, id),
                0);
    }

    private AspiranteReciente aAspiranteReciente(Usuario usuario) {
        Carrera carrera = usuario.getCarreraInteres();
        return new AspiranteReciente(
                usuario.getNombre() + " " + usuario.getApellidos(),
                usuario.getCorreo(),
                carrera == null ? null : carrera.getNombre(),
                carrera == null ? null : carrera.getDivision().getClave(),
                usuario.getFechaRegistro() == null ? "—" : usuario.getFechaRegistro().format(FORMATO_FECHA));
    }
}
