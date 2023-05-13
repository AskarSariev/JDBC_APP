package repositories.jdbc;

import models.Skill;
import repositories.SkillRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSkillRepositoryImpl implements SkillRepository { ;
    private PreparedStatement preparedStatement;

    @Override
    public List<Skill> getAll() {
        List<Skill> skills = new ArrayList<>();

        String selectAll = "SELECT * FROM skills";

        preparedStatement = JdbcUtils.getPreparedStatement(selectAll);

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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return skills;
    }

    @Override
    public Long create(Skill skill) {
        if (isPresentByName(skill.getSkillName())) {
            return getOneByName(skill.getSkillName());
        } else {
            long id = -1;

            String create = "INSERT INTO skills (name) VALUES (?)";

            preparedStatement = JdbcUtils.getPreparedStatement(create);

            try {
                preparedStatement.setString(1, skill.getSkillName());
                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return id;
        }
    }

    @Override
    public void update(Skill skill) {
        if (isPresentByName(skill.getSkillName())) {
            String update = "UPDATE skills SET name = ? WHERE id = ?";

            preparedStatement = JdbcUtils.getPreparedStatement(update);

            try {
                preparedStatement.setString(1, skill.getSkillName());
                preparedStatement.setLong(2, skill.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new RuntimeException("This skill with this ID not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        if (isPresentById(id)) {
            String deleteFromOuterTable = "DELETE developers_skills FROM developers_skills WHERE skill_id = ?";

            preparedStatement = JdbcUtils.getPreparedStatement(deleteFromOuterTable);

            try {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            String update = "DELETE skills FROM skills WHERE id = ?";

            preparedStatement = JdbcUtils.getPreparedStatement(update);

            try {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new RuntimeException("This skill with this ID not found");
        }
    }

    private Long getOneByName(String name) {
        return getAll().stream()
                .filter(skill -> skill.getSkillName().equals(name))
                .findFirst()
                .get().getId();
    }

    private boolean isPresentByName(String name) {
        Optional<Skill> foundSkill = getAll().stream()
                .filter(skill -> skill.getSkillName().equals(name))
                .findFirst();
        return foundSkill.isPresent();
    }

    private boolean isPresentById(long id) {
        Optional<Skill> foundSkill = getAll().stream()
                .filter(skill -> skill.getId() == id)
                .findFirst();
        return foundSkill.isPresent();
    }
}
