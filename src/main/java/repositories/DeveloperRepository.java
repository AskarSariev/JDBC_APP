package repositories;

import models.Developer;
import models.Status;

import java.util.List;

public interface DeveloperRepository extends GenericRepository<Developer, Long> {
    void addSpecialtyToDeveloper(long developerId, long specialtyId);

    void changeStatus(long id, Status newStatus);
}
