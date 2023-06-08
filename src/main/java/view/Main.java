package view;

import models.Developer;
import models.Skill;
import models.Specialty;
import models.Status;
import repositories.DeveloperRepository;
import repositories.hibernate.HibernateDeveloperRepositoryImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DeveloperRepository developerRepository = new HibernateDeveloperRepositoryImpl();
//        Specialty specialty = new Specialty("TechLead");
//        List<Skill> skills = List.of(new Skill("Collection2"), new Skill("Core2"));
//        Developer developer = new Developer(6L, "Test3", "Test3", skills, specialty, Status.ACTIVE);
//        developerRepository.update(developer);
        developerRepository.deleteById(6L);

        System.out.println(developerRepository.getAll());

//        SkillRepository skillRepository = new HibernateSkillRepositoryImpl();
//        Skill skill = new Skill("CSS");
//        skillRepository.create(skill);
////        skillRepository.deleteById(50L);
//        System.out.println(skillRepository.getAll());

//        SpecialtyRepository specialtyRepository = new HibernateSpecialtyRepositoryImpl();
//        Specialty specialty = new Specialty("Senior");
//        specialtyRepository.create(specialty);
//        System.out.println(specialtyRepository.getAll());
     }
}
