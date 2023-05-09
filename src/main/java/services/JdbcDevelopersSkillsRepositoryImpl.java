package services;

import repositories.DevelopersSkillsRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcDevelopersSkillsRepositoryImpl implements DevelopersSkillsRepository {
    private Connection connection = GettingConnectionAndStatement.getConnection();
    private PreparedStatement preparedStatement;

    @Override
    public void addSkillToDeveloper(long developerId, long skillId) {
        String create = "INSERT INTO developers_skills (developer_id, skill_id) VALUES (?, ?)";
        preparedStatement = GettingConnectionAndStatement.getPreparedStatement(create);
        try {
            preparedStatement.setLong(1, developerId);
            preparedStatement.setLong(2, skillId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void deleteSkillFromDeveloper(long developerId, long skillId) {
        String delete = "DELETE FROM developers_skills WHERE developer_id = ? AND skill_id = ?";
        preparedStatement = GettingConnectionAndStatement.getPreparedStatement(delete);
        try {
            preparedStatement.setLong(1, developerId);
            preparedStatement.setLong(2, skillId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
