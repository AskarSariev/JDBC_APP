package repositories.hibernate;

import models.Developer;
import models.Skill;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import repositories.DeveloperRepository;
import repositories.SpecialtyRepository;

import java.util.List;
import java.util.Optional;

public class HibernateDeveloperRepositoryImpl implements DeveloperRepository {
    private Session session;

    @Override
    public List<Developer> getAll() {
        session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();

        String select = "FROM Developer";
        List<Developer> developers = session.createQuery(select).getResultList();

        if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
            transaction.commit();
        }
        session.close();

        return developers;
    }

    @Override
    public Long create(Developer developer) {
        session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();

        session.persist(developer);

        if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
            transaction.commit();
        }

        return developer.getId();
    }

    @Override
    public void update(Developer developer) {
        if (isPresentById(developer.getId())) {
            session = HibernateUtils.getSessionFactory().getCurrentSession();
            Transaction transaction = null;

            transaction = session.beginTransaction();
            Developer foundDeveloper = session.get(Developer.class, developer.getId());
            foundDeveloper.setFirstName(developer.getFirstName());
            foundDeveloper.setLastName(developer.getLastName());
            foundDeveloper.setSkills(developer.getSkills());
            foundDeveloper.setSpecialty(developer.getSpecialty());
            foundDeveloper.setStatus(developer.getStatus());
            session.update(foundDeveloper);

            if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
                transaction.commit();
            }
            session.close();
        } else {
            throw new RuntimeException("This developer with this ID not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        if (isPresentById(id)) {
            session = HibernateUtils.getSessionFactory().getCurrentSession();
            Transaction transaction = null;

            transaction = session.beginTransaction();
            Developer developer = session.get(Developer.class, id);
            session.delete(developer);

            if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
                transaction.commit();
            }
            session.close();
        } else {
            throw new RuntimeException("This developer with this ID not found");
        }
    }

    private boolean isPresentById(long id) {
        Optional<Developer> foundDeveloper = getAll().stream()
                .filter(dev -> dev.getId() == id)
                .findFirst();
        return foundDeveloper.isPresent();
    }
}
