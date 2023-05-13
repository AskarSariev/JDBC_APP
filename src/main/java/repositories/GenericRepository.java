package repositories;

import java.util.List;

public interface GenericRepository<T, ID> {
    List<T> getAll();
    ID create(T t);
    void update(T t);
    void deleteById(ID id);
}
