package app.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public abstract class AbstractEntityRepositoryJPA<E extends Identifiable> implements EntityRepository<E> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<E> theEntityClass;

    public AbstractEntityRepositoryJPA(Class<E> entityClass) {
        this.theEntityClass = entityClass;
        System.out.println("Created " + this.getClass().getName() +
                "<" + this.theEntityClass.getSimpleName() + ">");
    }

    @Override
    public List<E> findAll() {
        return entityManager.createQuery("SELECT e FROM " + theEntityClass.getSimpleName() + " e", theEntityClass)
                .getResultList();
    }

    @Override
    public E findById(long id) {
        return entityManager.find(theEntityClass, id);
    }

    @Transactional
    @Override
    public E save(E entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return entity;
    }

    @Transactional
    @Override
    public boolean deleteById(long id) {
        E entity = entityManager.find(theEntityClass, id);
        if (entity != null) {
            entityManager.remove(entity);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public List<E> findByQuery(String jpqlName, Object... params) {
        var query = entityManager.createNamedQuery(jpqlName, theEntityClass);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getResultList();
    }
}
