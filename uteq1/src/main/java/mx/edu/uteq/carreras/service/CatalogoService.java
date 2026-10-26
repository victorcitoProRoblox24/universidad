package mx.edu.uteq.carreras.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mx.edu.uteq.carreras.entity.Carrera;
import mx.edu.uteq.carreras.entity.Division;
import mx.edu.uteq.carreras.repository.CarreraRepository;
import mx.edu.uteq.carreras.repository.DivisionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lectura de la oferta educativa publicada en la landing. Una carrera es
 * visible solo si ella y su división están habilitadas.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogoService {
    private final DivisionRepository divisionRepository;
    private final CarreraRepository carreraRepository;

    public List<Division> divisionesVisibles() {
        return divisionRepository.findByActivaTrueOrderByNombreAsc();
    }

    public List<Carrera> carrerasVisibles() {
        return carreraRepository.findByActivaTrueAndDivisionActivaTrueOrderByNombreAsc();
    }
}
