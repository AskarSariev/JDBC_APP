package repositories.hibernate;

import models.Skill;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import repositories.SkillRepository;

import java.util.List;
import java.util.Optional;

public class HibernateSkillRepositoryImpl implements SkillRepository {
    private Session session;

    @Override
    public List<Skill> getAll() {
        session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        transaction = session.beginTransaction();

        List<Skill> skills = session.createQuery("FROM Skill", Skill.class).getResultList();

        if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
            transaction.commit();
        }
        session.close();

        return skills;
    }

    @Override
    public Long create(Skill skill) {
        long id = -1;
        Transaction transaction = null;

        if (isPresentByName(skill.getSkillName())) {
            id = getIdByName(skill.getSkillName());
        } else {
            session = HibernateUtils.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(skill);
            session.getTransaction().commit();
            id = skill.getId();
        }

        if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
            transaction.commit();
        }
        session.close();

        return id;
    }

    @Override
    public void update(Skill skill) {
        if (isPresentById(skill.getId())) {
            session = HibernateUtils.getSessionFactory().openSession();
            Transaction transaction = null;
            transaction = session.beginTransaction();

            session.update(skill);

            if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
                transaction.commit();
            }
            session.close();
        } else {
            throw new RuntimeException("This skill with this ID not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        if (isPresentById(id)) {
            session = HibernateUtils.getSessionFactory().openSession();
            Transaction transaction = null;
            transaction = session.beginTransaction();

            String deleteFromOuterTable = "DELETE FROM developers_skills " +
                    "WHERE skill_id = :id";

            SQLQuery query = session.createSQLQuery(deleteFromOuterTable);
            query.setParameter("id", id);
            query.executeUpdate();

            Skill skill = session.get(Skill.class, id);
            session.delete(skill);

            if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
                transaction.commit();
            }
            session.close();
        } else {
            throw new RuntimeException("This skill with this ID not found");
        }
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

    private long getIdByName(String name) {
        return getAll().stream()
                .filter(skill -> skill.getSkillName().equals(name))
                .findFirst()
                .get().getId();
    }
}
