package services;

import models.Developer;
import models.Skill;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.DeveloperRepository;
import repositories.DevelopersSkillsRepository;
import repositories.SkillRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcDevelopersSkillsRepositoryImplTest {
    private DeveloperRepository developerRepository;
    private SkillRepository skillRepository;
    private DevelopersSkillsRepository developersSkillsRepository;

    @BeforeEach
    void setUp() {
        developerRepository = new JdbcDeveloperRepositoryImpl();
        skillRepository = new JdbcSkillRepositoryImpl();
        developersSkillsRepository = new JdbcDevelopersSkillsRepositoryImpl();
    }

    @Test
    void addSkillToDeveloperPositive() {
        Developer developer = new Developer("TestFirstName", "TestLastName");
        Skill skill = new Skill("TestSkill");

        long developerId = developerRepository.create(developer);
        long skillId = skillRepository.create(skill);

        developersSkillsRepository.addSkillToDeveloper(developerId, skillId);

        List<Skill> actualSkillListOdDeveloper = developerRepository.getOneById(developerId).get().getSkills();
        List<Skill> expectedSkillListOfDeveloper = List.of(skill);

        developersSkillsRepository.deleteSkillFromDeveloper(developerId, skillId);

        Assert.assertArrayEquals(expectedSkillListOfDeveloper.toArray(), actualSkillListOdDeveloper.toArray());
    }
}