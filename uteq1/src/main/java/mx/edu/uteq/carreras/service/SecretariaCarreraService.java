package mx.edu.uteq.carreras.service;

import lombok.RequiredArgsConstructor;
import mx.edu.uteq.carreras.entity.Carrera;
import mx.edu.uteq.carreras.entity.Division;
import mx.edu.uteq.carreras.repository.CarreraRepository;
import mx.edu.uteq.carreras.repository.DivisionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecretariaCarreraService {

    private final DivisionRepository divisionRepository;
    private final CarreraRepository carreraRepository;

    public Division buscarDivision(Long id) {
        return divisionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La división no existe."));
    }

    public List<Carrera> carrerasDeDivision(Long divisionId) {
        return carreraRepository.findByDivisionIdOrderByNombreAsc(divisionId);
    }

}
