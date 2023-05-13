package controllers;

import models.Skill;
import services.SkillService;

import java.util.List;

public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    public List<Skill> getAllSkills() {
        return skillService.getAllSkills();
    }

    public Long createSkill(Skill skill) {
        return skillService.createSkill(skill);
    }

    public void updateSkill(Skill skill) {
        skillService.updateSkill(skill);
    }

    public void deleteSkillById(long id) {
        skillService.deleteSkillById(id);
    }
}
