package services;

import models.Developer;
import models.Skill;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import repositories.SkillRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSkillRepositoryImplTest {
    private SkillRepository skillRepository;

    @BeforeEach
    void setUp() {
        skillRepository = new JdbcSkillRepositoryImpl();
    }

    @ParameterizedTest
    @MethodSource(value = "testDataPositiveCreatingSkill")
    void getAll(Skill skill) {
        List<Skill> expectedSkillList = List.of(skill);

        long skillId = skillRepository.create(skill);
        List<Skill> actualSkillList = skillRepository.getAll();

        skillRepository.deleteById(skillId);

        Assert.assertArrayEquals(expectedSkillList.toArray(), actualSkillList.toArray());
    }

    @ParameterizedTest
    @MethodSource(value = "testDataPositiveCreatingSkill")
    void shouldBeCreatedSkill(Skill skill) {
        long skillId = skillRepository.create(skill);
        Skill expectedSkill = skill;
        Skill actualSkill = skillRepository.getOneById(skillId).get();

        skillRepository.deleteById(skillId);

        Assert.assertTrue(expectedSkill.equals(actualSkill));
    }

    @ParameterizedTest
    @MethodSource(value = "testDataNegativeCreatingSkill")
    void shouldNotBeCreatedSkill(Skill skill) {
        Exception exception = assertThrows(RuntimeException.class, () -> skillRepository.create(skill));

        String expectedMessage = "Skill name shouldn't be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Skill beforeUpdatedSkill = new Skill("TestSkill");
        long skillId = skillRepository.create(beforeUpdatedSkill);

        Skill updatedSkill = skillRepository.getOneById(skillId).get();
        updatedSkill.setSkillName("NewName");
        skillRepository.update(updatedSkill);

        Skill expectedSkill = new Skill("NewName");
        Skill actualSkill = skillRepository.getOneById(skillId).get();

        skillRepository.deleteById(skillId);

        Assert.assertTrue(expectedSkill.equals(actualSkill));
    }

    @Test
    void deleteById() {
        List<Skill> emptySkillList = skillRepository.getAll();
        Assert.assertTrue(emptySkillList.isEmpty());

        Skill testSkill = new Skill("TestSkill");
        long skillId = skillRepository.create(testSkill);
        List<Skill> notEmptyList = skillRepository.getAll();
        Assert.assertTrue(!notEmptyList.isEmpty());

        skillRepository.deleteById(skillId);
        emptySkillList = skillRepository.getAll();
        Assert.assertTrue(emptySkillList.isEmpty());
    }

    @Test
    void getOneById() {
        Skill testSkill = new Skill("TestSkill");
        long skillId = skillRepository.create(testSkill);

        Skill expectedSkill = testSkill;
        Skill actualSkill = skillRepository.getOneById(skillId).get();

        skillRepository.deleteById(skillId);

        Assert.assertTrue(expectedSkill.equals(actualSkill));
    }

    private static Stream<Arguments> testDataPositiveCreatingSkill() {
        return Stream.of(
                Arguments.of(new Skill("TestSkill1")),
                Arguments.of(new Skill("TestSkill2"))
        );
    }

    private static Stream<Arguments> testDataNegativeCreatingSkill() {
        return Stream.of(
                Arguments.of(new Skill()),
                Arguments.of(new Skill(""))
        );
    }
}