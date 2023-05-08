package services;

import models.Developer;
import models.Skill;
import models.Specialty;
import models.Status;
import repositories.DeveloperRepository;

import java.sql.*;
import java.util.*;

public class JdbcDeveloperRepositoryImpl implements DeveloperRepository {
    private Connection connection = GettingConnectionAndStatement.getConnection();
    private PreparedStatement preparedStatement;

    @Override
    public List<Developer> getAll() {
        List<Developer> developers = new ArrayList<>();

        String selectAll = "SELECT developers.id, developers.first_name, developers.last_name, skills.name, specialties.name, developers.status " +
                      "FROM developers " +
                      "LEFT OUTER JOIN developers_skills " +
                      "ON developers.id = developers_skills.developer_id " +
                      "LEFT OUTER JOIN skills " +
                      "ON developers_skills.skill_id = skills.id " +
                      "LEFT JOIN specialties " +
                      "ON developers.specialty_id = specialties.id";

        preparedStatement = GettingConnectionAndStatement.getPreparedStatement(selectAll);
        try {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Developer developer = new Developer();

                developer.setId(resultSet.getInt("id"));
                developer.setFirstName(resultSet.getString("first_name"));
                developer.setLastName(resultSet.getString("last_name"));

                Skill skill = new Skill();
                skill.setSkillName(resultSet.getString("skills.name"));

                Specialty specialty = new Specialty();
                specialty.setSpecialtyName(resultSet.getString("specialties.name"));
                developer.setSpecialty(specialty);

                developer.setStatus(Status.valueOf(resultSet.getString("developers.status")));

                if (!developers.contains(developer)) {
                    List<Skill> skills = new ArrayList<>();
                    skills.add(skill);
                    developer.setSkills(skills);
                    developers.add(developer);
                } else {
                    int index = developers.indexOf(developer);
                    developers.get(index).getSkills().add(skill);
                }
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
        return developers;
    }

    @Override
    public void create(Developer developer) {
        String create = "INSERT INTO developers (first_name, last_name) VALUES (?, ?)";
        preparedStatement = GettingConnectionAndStatement.getPreparedStatement(create);
        try {
            preparedStatement.setString(1, developer.getFirstName());
            preparedStatement.setString(2, developer.getLastName());
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
    public void update(Developer updatedDeveloper) {
        Optional<Developer> foundDeveloper = getOneById(updatedDeveloper.getId());
        if (foundDeveloper.isPresent()) {
            String update = "UPDATE developers " +
                    "JOIN specialties ON developers.specialty_id = specialties.id " +
                    "SET first_name = ?, last_name = ?, specialty_id = ?, status = ? " +
                    "WHERE developers.id = ?";
            preparedStatement = GettingConnectionAndStatement.getPreparedStatement(update);
            try {
                preparedStatement.setString(1, updatedDeveloper.getFirstName());
                preparedStatement.setString(2, updatedDeveloper.getLastName());
                preparedStatement.setLong(3, updatedDeveloper.getSpecialty().getId());
                preparedStatement.setString(4, updatedDeveloper.getStatus().toString());
                preparedStatement.setLong(5, updatedDeveloper.getId());
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
        } else {
            throw new RuntimeException("Developer with this ID not found");
        }
    }

    @Override
    public void deleteById(long id) {
        Optional<Developer> foundDeveloper = getOneById(id);
        if (foundDeveloper.isPresent()) {
            String deleteFromOuterTable = "DELETE developers_skills FROM developers_skills " +
                    "WHERE developer_id = ?";
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

            String deleteDeveloper = "DELETE developers FROM developers " +
                    "JOIN specialties " +
                    "ON developers.specialty_id = specialties.id " +
                    "WHERE developers.id = ?";
            preparedStatement = GettingConnectionAndStatement.getPreparedStatement(deleteDeveloper);
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
            throw new RuntimeException("Developer with this ID not found");
        }
    }

    @Override
    public Optional<Developer> getOneById(long id) {
        return getAll().stream().filter(developer -> developer.getId() == id).findFirst();
    }

    @Override
    public void addSpecialtyToDeveloper(long developerId, long specialtyId) {
        String setSpecialty = "UPDATE developers SET specialty_id = ? WHERE id = ?";
        preparedStatement = GettingConnectionAndStatement.getPreparedStatement(setSpecialty);
        try {
            preparedStatement.setLong(1, specialtyId);
            preparedStatement.setLong(2, developerId);
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
    public void changeStatus(long id, Status newStatus) {
        Optional<Developer> foundDeveloper = getOneById(id);
        if (foundDeveloper.isPresent()) {
            String update = "UPDATE developers SET status = ? WHERE id = ?";
            preparedStatement = GettingConnectionAndStatement.getPreparedStatement(update);
            try {
                preparedStatement.setString(1, newStatus.toString());
                preparedStatement.setLong(2, id);
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
}
