package services;

import controllers.SkillController;
import models.Skill;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SkillServiceTest {
    private final SkillService skillService = Mockito.mock(SkillService.class);
    private final SkillController skillController = new SkillController(skillService);

    @Test
    void testGetAllSkills() {
        when(skillService.getAllSkills()).thenReturn(getSkills());
        List<Skill> skills = skillController.getAllSkills();
        assertEquals(skills.size(), 2);
    }

    @Test
    void testCreateSkill() {
        Skill skill = new Skill();
        when(skillService.createSkill(skill)).thenReturn(1L);
        long skillId = skillController.createSkill(skill);
        assertEquals(skillId, 1L);
    }

    @Test
    void testUpdateSkill() {
        Skill skill = new Skill();
        doNothing().when(skillService).updateSkill(skill);
        skillService.updateSkill(skill);
        verify(skillService, times(1)).updateSkill(skill);
    }

    @Test
    void testDeleteSkillById() {
        long id = 1L;
        doNothing().when(skillService).deleteSkillById(id);
        skillService.deleteSkillById(id);
        verify(skillService, times(1)).deleteSkillById(id);
    }

    private List<Skill> getSkills() {
        return List.of(
                new Skill("Java"),
                new Skill("JDBC")
        );
    }
}