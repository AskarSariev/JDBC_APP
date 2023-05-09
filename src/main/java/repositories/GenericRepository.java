package repositories;

import models.Developer;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, Long> {
    List<T> getAll();
    long create(T t);
    void update(T t);
    void deleteById(long id);

    Optional<T> getOneById(long id);
}
