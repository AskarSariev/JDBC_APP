package services;

import models.Developer;
import repositories.DeveloperRepository;

import java.util.List;

public class DeveloperService {
    private final DeveloperRepository developerRepository;

    public DeveloperService(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    public List<Developer> getAllDevelopers() {
        return developerRepository.getAll();
    }

    public Long createDeveloper(Developer developer) {
        return developerRepository.create(developer);
    }

    public void updateDeveloper(Developer developer) {
        developerRepository.update(developer);
    }

    public void deleteDeveloperById(long id) {
        developerRepository.deleteById(id);
    }
}
