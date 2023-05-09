package controllers;

import models.Skill;
import repositories.SkillRepository;

import java.util.List;

public class SkillController {
    private final SkillRepository skillRepository;

    public SkillController(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAll() {
        return skillRepository.getAll();
    }

    public long create(Skill skill) {
        return skillRepository.create(skill);
    }

    public void update(Skill skill) {
        skillRepository.update(skill);
    }

    public void deleteById(long id) {
        skillRepository.deleteById(id);
    }

    public Skill getOneById(long id) {
        return skillRepository.getOneById(id).orElseThrow(
                () -> new RuntimeException("Skill with this ID not found")
        );
    }
}
