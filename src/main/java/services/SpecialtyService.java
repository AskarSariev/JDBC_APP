package services;

import models.Specialty;
import repositories.SpecialtyRepository;

import java.util.List;

public class SpecialtyService {
    private final SpecialtyRepository specialtyRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    public List<Specialty> getAllSpecialties() {
        return specialtyRepository.getAll();
    }

    public Long createSpecialty(Specialty specialty) {
        return specialtyRepository.create(specialty);
    }

    public void updateSpecialty(Specialty specialty) {
        specialtyRepository.update(specialty);
    }

    public void deleteSpecialtyById(long id) {
        specialtyRepository.deleteById(id);
    }
}
