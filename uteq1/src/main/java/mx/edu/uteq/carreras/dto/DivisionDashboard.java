package mx.edu.uteq.carreras.dto;

/**
 * Fila de la tabla de divisiones del dashboard del administrador (HU-05).
 *
 * <p>{@code porcentajeAspirantes} es relativo a la división con más aspirantes
 * y solo se usa para dibujar la barra de comparación.</p>
 */
public record DivisionDashboard(
        Long id,
        String clave,
        String nombre,
        boolean activa,
        long totalCarreras,
        long carrerasActivas,
        long aspirantes,
        int porcentajeAspirantes) {
}
