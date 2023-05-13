package services;

import models.Skill;
import repositories.SkillRepository;

import java.util.List;

public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAllSkills() {
        return skillRepository.getAll();
    }

    public Long createSkill(Skill skill) {
        return skillRepository.create(skill);
    }

    public void updateSkill(Skill skill) {
        skillRepository.update(skill);
    }

    public void deleteSkillById(long id) {
        skillRepository.deleteById(id);
    }
}
