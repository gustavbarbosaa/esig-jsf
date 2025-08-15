package dao;

import config.JPAConfig;
import lombok.RequiredArgsConstructor;
import models.Pessoa;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class PessoaDAO {

    public Pessoa getById(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        Pessoa pessoa = em.find(Pessoa.class, id);
        em.close();
        return pessoa;
    }

    public List<Pessoa> getAll() {
        EntityManager em = JPAConfig.getEntityManager();
        TypedQuery<Pessoa> query = em.createQuery("SELECT p FROM Pessoa p", Pessoa.class);
        List<Pessoa> pessoas = query.getResultList();
        em.close();
        return pessoas;
    }
}