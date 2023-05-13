package controllers;

import models.Specialty;
import services.SpecialtyService;

import java.util.List;

public class SpecialtyController {
    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    public List<Specialty> getAllSpecialties() {
        return specialtyService.getAllSpecialties();
    }

    public Long createSpecialty(Specialty specialty) {
        return specialtyService.createSpecialty(specialty);
    }

    public void updateSpecialty(Specialty specialty) {
        specialtyService.updateSpecialty(specialty);
    }

    public void deleteSpecialtyById(long id) {
        specialtyService.deleteSpecialtyById(id);
    }
}
