package mx.edu.uteq.carreras.repository;

import java.util.List;
import java.util.Optional;
import mx.edu.uteq.carreras.entity.Rol;
import mx.edu.uteq.carreras.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreoIgnoreCase(String correo);
    boolean existsByCorreoIgnoreCase(String correo);
    long countByRol(Rol rol);
    long countByCarreraInteresId(Long carreraInteresId);
    long countByRolAndCarreraInteresIsNull(Rol rol);
    long countByRolAndCarreraInteresDivisionId(Rol rol, Long divisionId);
    List<Usuario> findTop5ByRolOrderByFechaRegistroDesc(Rol rol);
}
