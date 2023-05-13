package services;

import controllers.DeveloperController;
import models.Developer;
import models.Skill;
import models.Status;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DeveloperServiceTest {
    private final DeveloperService developerService = Mockito.mock(DeveloperService.class);
    private final DeveloperController developerController = new DeveloperController(developerService);

    @Test
    void testGetAllDevelopers() {
        when(developerService.getAllDevelopers()).thenReturn(getDevelopers());
        List<Developer> developers = developerController.getAllDevelopers();
        assertEquals(developers.size(), 2);
    }

    @Test
    void testCreateDeveloper() {
        Developer developer = new Developer();
        when(developerService.createDeveloper(developer)).thenReturn(1L);
        long developerId = developerController.createDeveloper(developer);
        assertEquals(developerId, 1L);
    }

    @Test
    void testUpdateDeveloper() {
        Developer developer = new Developer();
        doNothing().when(developerService).updateDeveloper(developer);
        developerService.updateDeveloper(developer);
        verify(developerService, times(1)).updateDeveloper(developer);
    }

    @Test
    void testDeleteDeveloperById() {
        long id = 1L;
        doNothing().when(developerService).deleteDeveloperById(id);
        developerService.deleteDeveloperById(id);
        verify(developerService, times(1)).deleteDeveloperById(id);
    }

    private List<Developer> getDevelopers() {
        return List.of(
                new Developer("TestFirstName1", "TestLastName1",
                        List.of(new Skill("Java")), null, Status.ACTIVE),
                new Developer("TestFirstName2", "TestLastName2",
                        List.of(new Skill("Spring")), null, Status.ACTIVE)
        );
    }
}