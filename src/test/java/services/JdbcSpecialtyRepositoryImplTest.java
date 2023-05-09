package services;

import models.Skill;
import models.Specialty;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import repositories.SpecialtyRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSpecialtyRepositoryImplTest {
    private SpecialtyRepository specialtyRepository;

    @BeforeEach
    void setUp() {
        specialtyRepository = new JdbcSpecialtyRepositoryImpl();
    }

    @ParameterizedTest
    @MethodSource(value = "testDataPositiveCreatingSpecialty")
    void getAll(Specialty specialty) {
        List<Specialty> expectedSpecialtyList = List.of(specialty);

        long specialtyId = specialtyRepository.create(specialty);
        List<Specialty> actualSpecialtyList = specialtyRepository.getAll();

        specialtyRepository.deleteById(specialtyId);

        Assert.assertArrayEquals(expectedSpecialtyList.toArray(), actualSpecialtyList.toArray());
    }

    @ParameterizedTest
    @MethodSource(value = "testDataPositiveCreatingSpecialty")
    void shouldBeCreatedSkill(Specialty specialty) {
        long specialtyId = specialtyRepository.create(specialty);
        Specialty expectedSpecialty = specialty;
        Specialty actualSpecialty = specialtyRepository.getOneById(specialtyId).get();

        specialtyRepository.deleteById(specialtyId);

        Assert.assertTrue(expectedSpecialty.equals(actualSpecialty));
    }

    @ParameterizedTest
    @MethodSource(value = "testDataNegativeCreatingSpecialty")
    void shouldNotBeCreatedSkill(Specialty specialty) {
        Exception exception = assertThrows(RuntimeException.class, () -> specialtyRepository.create(specialty));

        String expectedMessage = "Specialty name shouldn't be null or empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Specialty beforeUpdatedSpecialty = new Specialty("TestSpecialty");
        long specialtyId = specialtyRepository.create(beforeUpdatedSpecialty);

        Specialty updatedSpecialty = specialtyRepository.getOneById(specialtyId).get();
        updatedSpecialty.setSpecialtyName("NewName");
        specialtyRepository.update(updatedSpecialty);

        Specialty expectedSpecialty = new Specialty("NewName");
        Specialty actualSpecialty = specialtyRepository.getOneById(specialtyId).get();

        specialtyRepository.deleteById(specialtyId);

        Assert.assertTrue(expectedSpecialty.equals(actualSpecialty));
    }

    @Test
    void deleteById() {
        List<Specialty> emptySpecialtyList = specialtyRepository.getAll();
        Assert.assertTrue(emptySpecialtyList.isEmpty());

        Specialty testSpecialty = new Specialty("TestSpecialty");
        long specialtyId = specialtyRepository.create(testSpecialty);
        List<Specialty> notEmptyList = specialtyRepository.getAll();
        Assert.assertTrue(!notEmptyList.isEmpty());

        specialtyRepository.deleteById(specialtyId);
        emptySpecialtyList = specialtyRepository.getAll();
        Assert.assertTrue(emptySpecialtyList.isEmpty());
    }

    @Test
    void getOneById() {
        Specialty testSpecialty = new Specialty("TestSpecialty");
        long specialtyId = specialtyRepository.create(testSpecialty);

        Specialty expectedSpecialty = testSpecialty;
        Specialty actualSpecialty = specialtyRepository.getOneById(specialtyId).get();

        specialtyRepository.deleteById(specialtyId);

        Assert.assertTrue(expectedSpecialty.equals(actualSpecialty));
    }

    private static Stream<Arguments> testDataPositiveCreatingSpecialty() {
        return Stream.of(
                Arguments.of(new Specialty("TestSpecialty1")),
                Arguments.of(new Specialty("TestSpecialty2"))
        );
    }

    private static Stream<Arguments> testDataNegativeCreatingSpecialty() {
        return Stream.of(
                Arguments.of(new Specialty()),
                Arguments.of(new Specialty(""))
        );
    }
}