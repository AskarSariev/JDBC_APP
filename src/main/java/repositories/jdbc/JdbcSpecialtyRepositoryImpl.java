package repositories.jdbc;

import models.Specialty;
import repositories.SpecialtyRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSpecialtyRepositoryImpl implements SpecialtyRepository {
    private PreparedStatement preparedStatement;

    @Override
    public List<Specialty> getAll() {
        List<Specialty> specialties = new ArrayList<>();

        String selectAll = "SELECT * FROM specialties";

        preparedStatement = JdbcUtils.getPreparedStatement(selectAll);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Specialty specialty = new Specialty();
                specialty.setId(resultSet.getInt("id"));
                specialty.setSpecialtyName(resultSet.getString("name"));
                specialties.add(specialty);
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
        return specialties;
    }

    @Override
    public Long create(Specialty specialty) {
        if (isPresentByName(specialty.getSpecialtyName())) {
            return getOneByName(specialty.getSpecialtyName());
        } else {
            long id = -1;

            String create = "INSERT INTO specialties (name) VALUES (?)";

            preparedStatement = JdbcUtils.getPreparedStatement(create);

            try {
                preparedStatement.setString(1, specialty.getSpecialtyName());
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
    public void update(Specialty specialty) {
        if (isPresentByName(specialty.getSpecialtyName())) {
            String update = "UPDATE specialties SET name = ? WHERE id = ?";

            preparedStatement = JdbcUtils.getPreparedStatement(update);

            try {
                preparedStatement.setString(1, specialty.getSpecialtyName());
                preparedStatement.setLong(2, specialty.getId());
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
            throw new RuntimeException("This specialty with this ID not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        if (isPresentById(id)) {
            String deleteFromDevelopersTable = "UPDATE developers SET specialty_id = null WHERE specialty_id = ?";

            preparedStatement = JdbcUtils.getPreparedStatement(deleteFromDevelopersTable);

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

            String delete = "DELETE specialties FROM specialties WHERE id = ?";

            preparedStatement = JdbcUtils.getPreparedStatement(delete);

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
            throw new RuntimeException("This specialty with this ID not found");
        }
    }

    private Long getOneByName(String name) {
        return getAll().stream()
                .filter(specialty -> specialty.getSpecialtyName().equals(name))
                .findFirst().get().getId();
    }

    private boolean isPresentByName(String name) {
        Optional<Specialty> foundSpecialty = getAll().stream()
                .filter(skill -> skill.getSpecialtyName().equals(name))
                .findFirst();
        return foundSpecialty.isPresent();
    }

    private boolean isPresentById(long id) {
        Optional<Specialty> foundSpecialty = getAll().stream()
                .filter(specialty -> specialty.getId() == id)
                .findFirst();
        return foundSpecialty.isPresent();
    }
}
