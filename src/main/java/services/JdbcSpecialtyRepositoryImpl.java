package services;

import models.Skill;
import models.Specialty;
import repositories.SpecialtyRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSpecialtyRepositoryImpl implements SpecialtyRepository {
    private Connection connection = GettingConnectionAndStatement.getConnection();
    private PreparedStatement preparedStatement;

    @Override
    public List<Specialty> getAll() {
        List<Specialty> specialties = new ArrayList<>();
        String selectAll = "SELECT * FROM specialties";
        preparedStatement = GettingConnectionAndStatement.getPreparedStatement(selectAll);
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
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return specialties;
    }

    @Override
    public void create(Specialty specialty) {
        String create = "INSERT INTO specialties (name) VALUES (?)";
        preparedStatement = GettingConnectionAndStatement.getPreparedStatement(create);
        try {
            preparedStatement.setString(1, specialty.getSpecialtyName());
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
    public void update(Specialty specialty) {
        Optional<Specialty> foundSpecialty = getOneById(specialty.getId());
        if (foundSpecialty.isPresent()) {
            String update = "UPDATE specialties SET name = ? WHERE id = ?";
            preparedStatement = GettingConnectionAndStatement.getPreparedStatement(update);
            try {
                preparedStatement.setString(1, specialty.getSpecialtyName());
                preparedStatement.setLong(2, specialty.getId());
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
            throw new RuntimeException("Specialty with this ID not found");
        }
    }

    @Override
    public void deleteById(long id) {
        Optional<Specialty> foundSpecialty = getOneById(id);
        if (foundSpecialty.isPresent()) {
            String deleteFromDevelopersTable = "UPDATE developers SET specialty_id = null WHERE specialty_id = ?";
            preparedStatement = GettingConnectionAndStatement.getPreparedStatement(deleteFromDevelopersTable);
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

            String delete = "DELETE specialties FROM specialties WHERE id = ?";
            preparedStatement = GettingConnectionAndStatement.getPreparedStatement(delete);
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
            throw new RuntimeException("Specialty with this ID not found");
        }
    }

    @Override
    public Optional<Specialty> getOneById(long id) {
        return getAll().stream().filter(developer -> developer.getId() == id).findFirst();
    }
}
