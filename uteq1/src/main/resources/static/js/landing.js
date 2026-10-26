// HU-01: filtro de carreras por división. Sin JS se ven todas las divisiones.
document.addEventListener('DOMContentLoaded', () => {
    const chips = document.querySelectorAll('.landing-chip');
    const divisiones = document.querySelectorAll('.landing-division');

    chips.forEach((chip) => {
        chip.addEventListener('click', () => {
            const filtro = chip.dataset.filtro;
            chips.forEach((c) => c.classList.toggle('activo', c === chip));
            divisiones.forEach((d) => {
                d.hidden = filtro !== 'todas' && d.dataset.division !== filtro;
            });
        });
    });
});
