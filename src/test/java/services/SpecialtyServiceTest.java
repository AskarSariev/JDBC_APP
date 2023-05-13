package services;

import controllers.SpecialtyController;
import models.Specialty;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SpecialtyServiceTest {
    private final SpecialtyService specialtyService = Mockito.mock(SpecialtyService.class);
    private final SpecialtyController specialtyController = new SpecialtyController(specialtyService);

    @Test
    void testGetAllSpecialties() {
        when(specialtyService.getAllSpecialties()).thenReturn(getSpecialties());
        List<Specialty> specialties = specialtyService.getAllSpecialties();
        assertEquals(specialties.size(), 2);
    }

    @Test
    void testCreateSpecialty() {
        Specialty specialty = new Specialty();
        when(specialtyService.createSpecialty(specialty)).thenReturn(1L);
        long specialtyId = specialtyController.createSpecialty(specialty);
        assertEquals(specialtyId, 1L);
    }

    @Test
    void testUpdateSpecialty() {
        Specialty specialty = new Specialty();
        doNothing().when(specialtyService).updateSpecialty(specialty);
        specialtyService.updateSpecialty(specialty);
        verify(specialtyService, times(1)).updateSpecialty(specialty);
    }

    @Test
    void testDeleteSpecialtyById() {
        long id = 1L;
        doNothing().when(specialtyService).deleteSpecialtyById(id);
        specialtyService.deleteSpecialtyById(id);
        verify(specialtyService, times(1)).deleteSpecialtyById(id);
    }

    private List<Specialty> getSpecialties() {
        return List.of(
                new Specialty("Junior"),
                new Specialty("Middle")
        );
    }
}