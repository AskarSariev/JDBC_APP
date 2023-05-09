package repositories;

public interface DevelopersSkillsRepository {
    void addSkillToDeveloper(long developerId, long skillId);

    void deleteSkillFromDeveloper(long developerId, long skillId);
}
