package controllers;

import models.Developer;
import services.DeveloperService;

import java.util.List;

public class DeveloperController {
    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    public List<Developer> getAllDevelopers() {
        return developerService.getAllDevelopers();
    }

    public Long createDeveloper(Developer developer) {
        return developerService.createDeveloper(developer);
    }

    public void updateDeveloper(Developer developer) {
        developerService.updateDeveloper(developer);
    }

    public void deleteDeveloperById(long id) {
        developerService.deleteDeveloperById(id);
    }
}
