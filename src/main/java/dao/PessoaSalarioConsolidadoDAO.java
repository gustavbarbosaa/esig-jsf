package dao;

import config.JPAConfig;
import models.PessoaSalarioConsolidado;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class PessoaSalarioConsolidadoDAO {

    public PessoaSalarioConsolidado getById(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.find(PessoaSalarioConsolidado.class, id);
        } finally {
            em.close();
        }
    }

    public List<PessoaSalarioConsolidado> getAll() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<PessoaSalarioConsolidado> query =
                    em.createQuery("SELECT p FROM PessoaSalarioConsolidado p", PessoaSalarioConsolidado.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void saveOrUpdate(PessoaSalarioConsolidado pessoaSalarioConsolidado) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(pessoaSalarioConsolidado);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao salvar ou atualizar o registro.", e);
        } finally {
            em.close();
        }
    }

    public void delete(PessoaSalarioConsolidado pessoaSalarioConsolidado) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(em.contains(pessoaSalarioConsolidado)
                    ? pessoaSalarioConsolidado
                    : em.merge(pessoaSalarioConsolidado));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao remover o registro.", e);
        } finally {
            em.close();
        }
    }

    public void deleteAll() {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.createQuery("DELETE FROM PessoaSalarioConsolidado").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao apagar todos os registros.", e);
        } finally {
            em.close();
        }
    }
}