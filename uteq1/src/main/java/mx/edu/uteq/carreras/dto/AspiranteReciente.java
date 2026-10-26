package mx.edu.uteq.carreras.dto;

/**
 * Aspirante mostrado en la lista de registros recientes del dashboard (HU-05).
 */
public record AspiranteReciente(
        String nombreCompleto,
        String correo,
        String carrera,
        String division,
        String fechaRegistro) {
}
