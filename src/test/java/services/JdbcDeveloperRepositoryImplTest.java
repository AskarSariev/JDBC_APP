package services;

import models.Developer;
import models.Specialty;
import models.Status;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import repositories.DeveloperRepository;
import repositories.SpecialtyRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JdbcDeveloperRepositoryImplTest {
    private DeveloperRepository developerRepository;
    private SpecialtyRepository specialtyRepository;

    @BeforeEach
    void setUp() {
        developerRepository = new JdbcDeveloperRepositoryImpl();
        specialtyRepository = new JdbcSpecialtyRepositoryImpl();
    }

    @ParameterizedTest
    @MethodSource(value = "testDataForGetAll")
    void testGetAll(Developer developer) {
        developer.setSkills(List.of());
        long id = developerRepository.create(developer);
        developer.setId(id);

        List<Developer> expectedDevelopers = List.of(developer);
        List<Developer> actualDevelopers = developerRepository.getAll();

        developerRepository.deleteById(id);

        Assert.assertArrayEquals(expectedDevelopers.toArray(), actualDevelopers.toArray());
    }

    @ParameterizedTest
    @MethodSource(value = "testPositiveDataCreatingDeveloper")
    void shouldBeCreatedDeveloper(Developer inputDeveloper, Developer expectedDeveloper) {
        long generatedId = developerRepository.create(inputDeveloper);

        inputDeveloper.setId(generatedId);
        expectedDeveloper = inputDeveloper;

        List<Developer> actualDevelopers = developerRepository.getAll();
        long actualId = 0;
        for (Developer developer : actualDevelopers) {
            if (generatedId == developer.getId()) {
                actualId = developer.getId();
            }
        }
        Developer actualDeveloper = developerRepository.getOneById(actualId).get();

        developerRepository.deleteById(generatedId);

        assertEquals(expectedDeveloper, actualDeveloper);
    }

    @ParameterizedTest
    @MethodSource(value = "testNegativeDataCreatingDeveloper")
    void shouldNotBeCreatedDeveloper(Developer inputDeveloper) {
        Exception exception = assertThrows(RuntimeException.class, () -> developerRepository.create(inputDeveloper));

        String expectedMessage = "FirstName or LastName cannot be null or empty. Enter firstName or lastName correctly";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @MethodSource(value = "testDataForUpdateDeveloper")
    void testUpdate(Developer developerBeforeUpdating) {
        long developerId = developerRepository.create(developerBeforeUpdating);

        Developer updatedDeveloper = developerRepository.getOneById(developerId).get();
        updatedDeveloper.setFirstName("TestFirstName2");
        updatedDeveloper.setLastName("TestLastName2");
        Specialty specialty = new Specialty("TestSpecialty");
        long specialtyId = specialtyRepository.create(specialty);
        updatedDeveloper.setSpecialty(specialty);
        updatedDeveloper.setStatus(Status.DELETED);
        developerRepository.update(updatedDeveloper);

        List<Developer> expectedDevelopers = List.of(updatedDeveloper);
        List<Developer> actualDevelopers = developerRepository.getAll();

        developerRepository.deleteById(developerId);
        specialtyRepository.deleteById(specialtyId);

        Assert.assertArrayEquals(expectedDevelopers.toArray(), actualDevelopers.toArray());
    }

    @Test
    void deleteById() {
        List<Developer> emptyList = developerRepository.getAll();
        Assert.assertTrue(emptyList.isEmpty());

        Developer developer = new Developer("TestFirstName", "TestLastName");
        long id = developerRepository.create(developer);
        List<Developer> notEmptyList = developerRepository.getAll();
        Assert.assertFalse(notEmptyList.isEmpty());

        developerRepository.deleteById(id);
        emptyList = developerRepository.getAll();

        Assert.assertTrue(emptyList.isEmpty());
    }

    @Test
    void getOneById() {
        Developer developer = new Developer("TestFirstName", "TestLastName");
        long generatedId = developerRepository.create(developer);

        long foundId = developerRepository.getAll().stream()
                .filter(dev -> dev.getId() == generatedId).findFirst().get().getId();

        developerRepository.deleteById(generatedId);

        Assert.assertTrue(generatedId == foundId);
    }

    @Test
    void addSpecialtyToDeveloper() {
        Developer developer = new Developer("TestFirstName1", "TestLastName1", null, Status.ACTIVE);
        long developerId = developerRepository.create(developer);

        Specialty specialty = new Specialty("TestSpecialty");
        long specialtyId = specialtyRepository.create(specialty);

        developerRepository.addSpecialtyToDeveloper(developerId, specialtyId);

        developer = developerRepository.getOneById(developerId).get();

        developerRepository.deleteById(developerId);
        specialtyRepository.deleteById(specialtyId);

        assertTrue(developer.getSpecialty().getSpecialtyName().equals("TestSpecialty"));
    }

    @Test
    void changeStatus() {
        Developer developer = new Developer("TestFirstName1", "TestLastName1", null, Status.ACTIVE);
        long developerId = developerRepository.create(developer);

        developerRepository.changeStatus(developerId, Status.DELETED);
        developer = developerRepository.getOneById(developerId).get();

        developerRepository.deleteById(developerId);

        assertTrue(developer.getStatus().equals(Status.DELETED));
    }

    private static Stream<Arguments> testDataForGetAll() {
        Developer developer = new Developer("TestFirstName1", "TestLastName1", null, Status.ACTIVE);
        return Stream.of(Arguments.of(developer));
    }

    private static Stream<Arguments> testPositiveDataCreatingDeveloper() {
        return Stream.of(
                Arguments.of(new Developer("TestFirstName1", "TestLastName1"), new Developer("TestFirstName1", "TestLastName1"))
        );
    }

    private static Stream<Arguments> testNegativeDataCreatingDeveloper() {
        return Stream.of(
                Arguments.of(new Developer()),
                Arguments.of(new Developer("", "TestLastName")),
                Arguments.of(new Developer("TestFirstName", ""))
        );
    }

    private static Stream<Arguments> testDataForUpdateDeveloper() {
        Developer developerBeforeUpdating = new Developer("TestFirstName1", "TestLastName1");
        return Stream.of(
                Arguments.of(developerBeforeUpdating)
        );
    }
}