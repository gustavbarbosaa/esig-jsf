package services;

import config.JPAConfig;
import dao.PessoaDAO;
import dao.PessoaSalarioConsolidadoDAO;
import enums.TipoVencimento;
import models.Cargo;
import models.Pessoa;
import models.PessoaSalarioConsolidado;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class PessoaSalarioConsolidadoService {
    @Inject
    private PessoaDAO pessoaDAO;

    @Inject
    private PessoaSalarioConsolidadoDAO pessoaSalarioConsolidadoDAO;

    public PessoaSalarioConsolidado getById(int id) {
        return this.pessoaSalarioConsolidadoDAO.getById(id);
    }

    public List<PessoaSalarioConsolidado> getAll() {
        return this.pessoaSalarioConsolidadoDAO.getAll();
    }

    public List<PessoaSalarioConsolidado> getPaginatedList(int first, int pageSize, String sortField, boolean ascending) {
        return this.pessoaSalarioConsolidadoDAO.getPaginated(first, pageSize, sortField, ascending);
    }

    public Long countAll() {
        return this.pessoaSalarioConsolidadoDAO.countAll();
    }

    @Transactional
    public void removeSalarioDaPessoaSelecionada(PessoaSalarioConsolidado pessoaSalarioConsolidado) {
        this.pessoaSalarioConsolidadoDAO.deleteSalario(pessoaSalarioConsolidado);
    }

    @Transactional
    public void removeTodosSalarios() {
        this.pessoaSalarioConsolidadoDAO.deleteAllSalarios();
    }

    public void calcularSalarioPessoa(PessoaSalarioConsolidado pessoaSalarioConsolidado) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            em.getTransaction().begin();

            Pessoa pessoa = em.find(Pessoa.class, pessoaSalarioConsolidado.getPessoaId());

            if (pessoaSalarioConsolidado.getSalario() == null
                    || BigDecimal.ZERO.compareTo(pessoaSalarioConsolidado.getSalario()) == 0) {

                BigDecimal salario = calcularSalario(pessoa);
                pessoaSalarioConsolidado.setSalario(salario);
                em.merge(pessoaSalarioConsolidado);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Transactional
    public void preencheDadosBasicosPessoaSalarioConsolidado() {
        this.pessoaSalarioConsolidadoDAO.deleteAll();

        List<Pessoa> pessoas = this.pessoaDAO.getAll();

        pessoas.forEach(pessoa -> {
            PessoaSalarioConsolidado pessoaSalarioConsolidado = new PessoaSalarioConsolidado();
            pessoaSalarioConsolidado.setNome_pessoa(pessoa.getNome());
            pessoaSalarioConsolidado.setPessoaId(pessoa.getId());

            if (pessoa.getCargo() != null) {
                Cargo cargo = pessoa.getCargo();
                pessoaSalarioConsolidado.setNome_cargo(cargo.getNome());

                pessoaSalarioConsolidado.setSalario(this.calcularSalario(pessoa));
            }

            this.pessoaSalarioConsolidadoDAO.saveOrUpdate(pessoaSalarioConsolidado);
        });
    }

    private BigDecimal calcularSalario(Pessoa pessoa) {
        final BigDecimal[] salario = {BigDecimal.ZERO};
        pessoa.getCargo().getVencimentos().forEach(vencimento -> {
            if (vencimento.getVencimento().getTipoVencimento() == TipoVencimento.CREDITO) {
                salario[0] = salario[0].add(vencimento.getVencimento().getValor());
            } else {
                salario[0] = salario[0].subtract(vencimento.getVencimento().getValor());
            }
        });

        return salario[0];
    }
}
