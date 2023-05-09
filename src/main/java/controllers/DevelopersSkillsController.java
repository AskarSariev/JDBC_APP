package controllers;

import models.Developer;
import models.Status;
import repositories.DeveloperRepository;

import java.util.List;

public class DevelopersSkillsController {
    private final DeveloperRepository developerRepository;

    public DevelopersSkillsController(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    public List<Developer> getAll() {
        return developerRepository.getAll();
    }

    public long create(Developer developer) {
        return developerRepository.create(developer);
    }

    public void update(Developer developer) {
        developerRepository.update(developer);
    }

    public void deleteById(long id) {
        developerRepository.deleteById(id);
    }

    public Developer getOneById(long id) {
        return developerRepository.getOneById(id).orElseThrow(
                () -> new RuntimeException("Developer with this ID not found")
        );
    }

    public void addSpecialtyToDeveloper(long developerId, long specialtyId) {
        developerRepository.addSpecialtyToDeveloper(developerId, specialtyId);
    }

    public void changeStatus(long id, Status newStatus) {
        developerRepository.changeStatus(id, newStatus);
    }
}
