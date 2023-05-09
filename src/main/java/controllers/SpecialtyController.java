package controllers;

import models.Specialty;
import repositories.SpecialtyRepository;

import java.util.List;

public class SpecialtyController {
    private final SpecialtyRepository specialtyRepository;

    public SpecialtyController(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    public List<Specialty> getAll() {
        return specialtyRepository.getAll();
    }

    public long create(Specialty specialty) {
        return specialtyRepository.create(specialty);
    }

    public void update(Specialty specialty) {
        specialtyRepository.update(specialty);
    }

    public void deleteById(long id) {
        specialtyRepository.deleteById(id);
    }

    public Specialty getOneById(long id) {
        return specialtyRepository.getOneById(id).orElseThrow(
                () -> new RuntimeException("Specialty with this ID not found")
        );
    }
}
