package repositories.hibernate;

import models.Skill;
import models.Specialty;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import repositories.SpecialtyRepository;

import java.util.List;
import java.util.Optional;

public class HibernateSpecialtyRepositoryImpl implements SpecialtyRepository {
    private Session session;

    @Override
    public List<Specialty> getAll() {
        session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        transaction = session.beginTransaction();

        List<Specialty> specialties = session.createQuery("FROM Specialty", Specialty.class).getResultList();

        if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
            transaction.commit();
        }
        session.close();

        return specialties;
    }

    @Override
    public Long create(Specialty specialty) {
        long id = -1;
        Transaction transaction = null;

        if (isPresentByName(specialty.getSpecialtyName())) {
            id = getIdByName(specialty.getSpecialtyName());
        } else {
            session = HibernateUtils.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(specialty);
            session.getTransaction().commit();
            id = specialty.getId();
        }

        if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
            transaction.commit();
        }
        session.close();

        return id;
    }

    @Override
    public void update(Specialty specialty) {
        if (isPresentById(specialty.getId())) {
            session = HibernateUtils.getSessionFactory().openSession();
            Transaction transaction = null;
            transaction = session.beginTransaction();

            session.update(specialty);

            if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
                transaction.commit();
            }
            session.close();
        } else {
            throw new RuntimeException("This specialty with this ID not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        if (isPresentById(id)) {
            session = HibernateUtils.getSessionFactory().openSession();
            Transaction transaction = null;
            transaction = session.beginTransaction();

            String deleteFromOuterTable = "DELETE FROM developers " +
                    "WHERE specialty_id = :id";

            SQLQuery query = session.createSQLQuery(deleteFromOuterTable);
            query.setParameter("id", id);
            query.executeUpdate();

            Specialty specialty = session.get(Specialty.class, id);
            session.delete(specialty);

            if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
                transaction.commit();
            }
            session.close();
        } else {
            throw new RuntimeException("This specialty with this ID not found");
        }
    }

    private boolean isPresentByName(String name) {
        Optional<Specialty> foundSpecialty = getAll().stream()
                .filter(specialty -> specialty.getSpecialtyName().equals(name))
                .findFirst();
        return foundSpecialty.isPresent();
    }

    private boolean isPresentById(long id) {
        Optional<Specialty> foundSpecialty = getAll().stream()
                .filter(specialty -> specialty.getId() == id)
                .findFirst();
        return foundSpecialty.isPresent();
    }

    private long getIdByName(String name) {
        return getAll().stream()
                .filter(specialty -> specialty.getSpecialtyName().equals(name))
                .findFirst()
                .get().getId();
    }
}
