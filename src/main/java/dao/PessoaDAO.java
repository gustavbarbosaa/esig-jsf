package dao;

import config.JPAConfig;
import models.Pessoa;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class PessoaDAO {

    private final BCryptPasswordEncoder enconder = new BCryptPasswordEncoder();

    public Pessoa saveOrUpdate(Pessoa pessoa) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            Pessoa existingPessoa = em.find(Pessoa.class, pessoa.getId());
            if (existingPessoa != null) {
                existingPessoa.setNome(pessoa.getNome());
                existingPessoa.setUsuario(pessoa.getUsuario());
                existingPessoa.setSenha(pessoa.getSenha());
                existingPessoa.setCargo(pessoa.getCargo());
                em.merge(existingPessoa);
            } else {
                em.persist(pessoa);
            }
            em.getTransaction().commit();
            return pessoa;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Pessoa getById(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        Pessoa pessoa = em.find(Pessoa.class, id);
        em.close();
        return pessoa;
    }

    public List<Pessoa> getAll() {
        EntityManager em = JPAConfig.getEntityManager();
        TypedQuery<Pessoa> query = em.createQuery(
                "SELECT p FROM Pessoa p",
                Pessoa.class
        );
        return query.getResultList();
    }

    public Pessoa findPessoaWithVencimentos(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Pessoa p " +
                                    "LEFT JOIN FETCH p.cargo c " +
                                    "LEFT JOIN FETCH c.vencimentos " +
                                    "WHERE p.id = :id", Pessoa.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public Pessoa getByUsername(String username) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Pessoa> query = em.createQuery(
                    "SELECT p FROM Pessoa p WHERE p.usuario = :usuario", Pessoa.class);
            query.setParameter("usuario", username);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public void alterarSenha(Pessoa pessoa, String senha) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            em.getTransaction().begin();
            Pessoa existingPessoa = em.find(Pessoa.class, pessoa.getId());
            if (existingPessoa != null) {
                String senhaCodificada = this.enconder.encode(senha);
                existingPessoa.setSenha(senhaCodificada);
                em.merge(existingPessoa);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}