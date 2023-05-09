package controllers;

import repositories.DevelopersSkillsRepository;

public class DeveloperSkillController {
    private final DevelopersSkillsRepository developersSkillsRepository;

    public DeveloperSkillController(DevelopersSkillsRepository developersSkillsRepository) {
        this.developersSkillsRepository = developersSkillsRepository;
    }

    public void addSkillToDeveloper(long developerId, long skillId) {
        developersSkillsRepository.addSkillToDeveloper(developerId, skillId);
    }

    public void deleteSkillFromDeveloper(long developerId, long skillId) {
        developersSkillsRepository.deleteSkillFromDeveloper(developerId, skillId);
    }
}
