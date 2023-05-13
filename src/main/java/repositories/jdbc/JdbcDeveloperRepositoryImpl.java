package repositories.jdbc;

import models.Developer;
import models.Skill;
import models.Specialty;
import models.Status;
import repositories.DeveloperRepository;
import repositories.SkillRepository;
import repositories.SpecialtyRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcDeveloperRepositoryImpl implements DeveloperRepository {
    private PreparedStatement preparedStatement;

    private final SkillRepository skillRepository;
    private final SpecialtyRepository specialtyRepository;

    public JdbcDeveloperRepositoryImpl(SkillRepository skillRepository, SpecialtyRepository specialtyRepository) {
        this.skillRepository = skillRepository;
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public List<Developer> getAll() {
        List<Developer> developers = new ArrayList<>();

        String select = "SELECT developers.id, developers.first_name, developers.last_name, skills.name, specialties.name, developers.status " +
                      "FROM developers " +
                      "LEFT OUTER JOIN developers_skills " +
                      "ON developers.id = developers_skills.developer_id " +
                      "LEFT OUTER JOIN skills " +
                      "ON developers_skills.skill_id = skills.id " +
                      "LEFT JOIN specialties " +
                      "ON developers.specialty_id = specialties.id";

        preparedStatement = JdbcUtils.getPreparedStatement(select);

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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return developers;
    }

    @Override
    public Long create(Developer developer) {
        long developerId = -1;

        List<Skill> skills = developer.getSkills();
        List<Long> skillIds = new ArrayList<>();

        String createToDevelopersTable = "INSERT INTO developers (first_name, last_name, specialty_id, status) " +
                "SELECT ?, ?, specialties.id, ? " +
                "FROM specialties " +
                "WHERE specialties.id = ?";

        preparedStatement = JdbcUtils.getPreparedStatement(createToDevelopersTable);

        try {
            for (Skill skill : skills) {
                skillIds.add(skillRepository.create(skill));
            }

            long specialtyId = specialtyRepository.create(developer.getSpecialty());

            preparedStatement.setString(1, developer.getFirstName());
            preparedStatement.setString(2, developer.getLastName());
            preparedStatement.setString(3, developer.getStatus().toString());
            preparedStatement.setLong(4, specialtyId);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                developerId = generatedKeys.getLong(1);
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

        String createToDevelopersSkillsTable = "INSERT INTO developers_skills (developer_id, skill_id) VALUES (?, ?)";

        for (Long skillId : skillIds) {
            preparedStatement = JdbcUtils.getPreparedStatement(createToDevelopersSkillsTable);

            try {
                preparedStatement.setLong(1, developerId);
                preparedStatement.setLong(2, skillId);
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
        }
        return developerId;
    }

    @Override
    public void update(Developer updatedDeveloper) {
        long developerId = updatedDeveloper.getId();

        if (isPresentById(developerId)) {
            List<Skill> skills = updatedDeveloper.getSkills();
            List<Long> skillIds = new ArrayList<>();

            String update = "UPDATE developers " +
                    "JOIN specialties ON developers.specialty_id = specialties.id " +
                    "SET first_name = ?, last_name = ?, specialty_id = ?, status = ? " +
                    "WHERE developers.id = ?";

            preparedStatement = JdbcUtils.getPreparedStatement(update);

            try {
                for (Skill skill : skills) {
                    skillIds.add(skillRepository.create(skill));
                }

                long specialtyId = specialtyRepository.create(updatedDeveloper.getSpecialty());

                preparedStatement.setString(1, updatedDeveloper.getFirstName());
                preparedStatement.setString(2, updatedDeveloper.getLastName());
                preparedStatement.setLong(3, specialtyId);
                preparedStatement.setString(4, updatedDeveloper.getStatus().toString());
                preparedStatement.setLong(5, updatedDeveloper.getId());
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

            String deleteFromOuterTable = "DELETE developers_skills FROM developers_skills " +
                    "WHERE developer_id = ?";

            preparedStatement = JdbcUtils.getPreparedStatement(deleteFromOuterTable);

            try {
                preparedStatement.setLong(1, developerId);
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

            String createToDevelopersSkillsTable = "INSERT INTO developers_skills (developer_id, skill_id) VALUES (?, ?)";

            for (Long skillId : skillIds) {
                preparedStatement = JdbcUtils.getPreparedStatement(createToDevelopersSkillsTable);

                try {
                    preparedStatement.setLong(1, developerId);
                    preparedStatement.setLong(2, skillId);
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
            }
        } else {
            throw new RuntimeException("This developer with this ID not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        if (isPresentById(id)) {
            String deleteFromOuterTable = "DELETE developers_skills FROM developers_skills " +
                    "WHERE developer_id = ?";

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

            String deleteDeveloper = "DELETE developers FROM developers " +
                    "JOIN specialties " +
                    "ON developers.specialty_id = specialties.id " +
                    "WHERE developers.id = ?";

            preparedStatement = JdbcUtils.getPreparedStatement(deleteDeveloper);

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
            throw new RuntimeException("This developer with this ID not found");
        }
    }

    private boolean isPresentById(long id) {
        Optional<Developer> foundDeveloper = getAll().stream()
                .filter(dev -> dev.getId() == id).findFirst();
        return foundDeveloper.isPresent();
    }
}
