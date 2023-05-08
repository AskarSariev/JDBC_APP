import models.Skill;
import models.Specialty;
import services.JdbcDeveloperRepositoryImpl;
import services.JdbcDevelopersSkillsRepositoryImpl;
import services.JdbcSkillRepositoryImpl;
import services.JdbcSpecialtyRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        JdbcDeveloperRepositoryImpl jdbcDeveloperRepository = new JdbcDeveloperRepositoryImpl();
//        Developer developer = new Developer("Askar", "Sariev");
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
        jdbcSpecialtyRepository.deleteById(1);
//        System.out.println(jdbcSpecialtyRepository.getAll());

        JdbcDevelopersSkillsRepositoryImpl jdbcDevelopersSkillsRepository = new JdbcDevelopersSkillsRepositoryImpl();
//        jdbcDevelopersSkillsRepository.addSkillToDeveloper(6, 1);

//        jdbcDeveloperRepository.addSpecialtyToDeveloper(6, 1);

//        Developer developer = jdbcDeveloperRepository.getOneById(6).get();
//        developer.setStatus(Status.ACTIVE);
//
//        Specialty specialty = jdbcSpecialtyRepository.getOneById(2).get();
//        Developer developer = new Developer(1, "Askar", "Sariev", specialty, Status.ACTIVE);
//        jdbcDeveloperRepository.update(developer);

//        jdbcDeveloperRepository.deleteById(1);

//        jdbcDeveloperRepository.changeStatus(6, Status.ACTIVE);

        System.out.println(jdbcDeveloperRepository.getAll());
    }
}
