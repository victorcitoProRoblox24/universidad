package mx.edu.uteq.carreras.dto;

/**
 * Totales de las tarjetas del dashboard del administrador (HU-05).
 */
public record ResumenAdmin(
        long totalDivisiones,
        long divisionesActivas,
        long totalCarreras,
        long carrerasPublicadas,
        long totalAspirantes,
        long aspirantesSinCarrera) {
}
