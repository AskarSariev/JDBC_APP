package view;

import models.Developer;
import models.Skill;
import models.Specialty;
import models.Status;
import repositories.DeveloperRepository;
import services.JdbcDeveloperRepositoryImpl;
import services.JdbcDevelopersSkillsRepositoryImpl;
import services.JdbcSkillRepositoryImpl;
import services.JdbcSpecialtyRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        DeveloperRepository jdbcDeveloperRepository = new JdbcDeveloperRepositoryImpl();
//        Developer developer = new Developer("", "");
//        jdbcDeveloperRepository.create(developer);
//        System.out.println(jdbcDeveloperRepository.getAll());

        JdbcSkillRepositoryImpl jdbcSkillRepository = new JdbcSkillRepositoryImpl();
//        Skill skill = jdbcSkillRepository.getOneById(1).get();
//        skill.setSkillName("JDBC");
//        jdbcSkillRepository.create(skill);
//        jdbcSkillRepository.update(skill);
//        jdbcSkillRepository.deleteById(1);
//        System.out.println(jdbcSkillRepository.getAll());
//
        JdbcSpecialtyRepositoryImpl jdbcSpecialtyRepository = new JdbcSpecialtyRepositoryImpl();
//        Specialty specialty = new Specialty("Middle");
//        Specialty specialty = jdbcSpecialtyRepository.getOneById(2).get();
//        specialty.setSpecialtyName("Senior");
//        jdbcSpecialtyRepository.update(specialty);
//        jdbcSpecialtyRepository.create(specialty);
//        jdbcSpecialtyRepository.deleteById(1);
//        System.out.println(jdbcSpecialtyRepository.getAll());

        JdbcDevelopersSkillsRepositoryImpl jdbcDevelopersSkillsRepository = new JdbcDevelopersSkillsRepositoryImpl();
//        jdbcDevelopersSkillsRepository.addSkillToDeveloper(6, 1);

//        jdbcDeveloperRepository.addSpecialtyToDeveloper(6, 1);

//        Developer developer = jdbcDeveloperRepository.getOneById(6).get();
//        developer.setStatus(Status.ACTIVE);
//
//        Specialty specialty = jdbcSpecialtyRepository.getOneById(38).get();
        Developer updatedDeveloper = jdbcDeveloperRepository.getOneById(38).get();
        updatedDeveloper.setFirstName("TestFirstName2");
        updatedDeveloper.setLastName("TestFirstName2");
//        Specialty specialty = jdbcSpecialtyRepository.getOneById(4).get();
//        updatedDeveloper.setSpecialty(specialty);
//        updatedDeveloper.setStatus(Status.DELETED);
        System.out.println(updatedDeveloper);
        jdbcDeveloperRepository.update(updatedDeveloper);

//        jdbcDeveloperRepository.deleteById(22);

//        jdbcDeveloperRepository.changeStatus(6, Status.ACTIVE);

        System.out.println(jdbcDeveloperRepository.getAll());


    }
}
