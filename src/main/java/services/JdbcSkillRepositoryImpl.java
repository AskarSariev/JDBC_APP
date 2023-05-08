package services;

import models.Skill;
import repositories.SkillRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSkillRepositoryImpl implements SkillRepository {
    private Connection connection = GettingConnectionAndStatement.getConnection();
    private PreparedStatement preparedStatement;

    @Override
    public List<Skill> getAll() {
        List<Skill> skills = new ArrayList<>();
        String selectAll = "SELECT * FROM skills";
        preparedStatement = GettingConnectionAndStatement.getPreparedStatement(selectAll);
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Skill skill = new Skill();
                skill.setId(resultSet.getInt("id"));
                skill.setSkillName(resultSet.getString("name"));
                skills.add(skill);
            }
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
        return skills;
    }

    @Override
    public void create(Skill skill) {
        String create = "INSERT INTO skills (name) VALUES (?)";
        preparedStatement = GettingConnectionAndStatement.getPreparedStatement(create);
        try {
            preparedStatement.setString(1, skill.getSkillName());
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
    public void update(Skill skill) {
        Optional<Skill> foundSkill = getOneById(skill.getId());
        if (foundSkill.isPresent()) {
            String update = "UPDATE skills SET name = ? WHERE id = ?";
            preparedStatement = GettingConnectionAndStatement.getPreparedStatement(update);
            try {
                preparedStatement.setString(1, skill.getSkillName());
                preparedStatement.setLong(2, skill.getId());
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
        } else {
            throw new RuntimeException("Skill with this ID not found");
        }
    }

    @Override
    public void deleteById(long id) {
        Optional<Skill> foundSkill = getOneById(id);
        if (foundSkill.isPresent()) {
            String deleteFromOuterTable = "DELETE developers_skills FROM developers_skills " +
                    "WHERE skill_id = ?";
            preparedStatement = GettingConnectionAndStatement.getPreparedStatement(deleteFromOuterTable);

            try {
                preparedStatement.setLong(1, id);
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

            String update = "DELETE skills FROM skills WHERE id = ?";
            preparedStatement = GettingConnectionAndStatement.getPreparedStatement(update);
            try {
                preparedStatement.setLong(1, id);
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
        } else {
            throw new RuntimeException("Skill with this ID not found");
        }
    }

    @Override
    public Optional<Skill> getOneById(long id) {
        return getAll().stream().filter(skill -> skill.getId() == id).findFirst();
    }
}
