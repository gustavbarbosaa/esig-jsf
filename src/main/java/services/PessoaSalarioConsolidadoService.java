package services;

import dao.PessoaDAO;
import dao.PessoaSalarioConsolidadoDAO;
import models.Pessoa;
import models.PessoaSalarioConsolidado;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class PessoaSalarioConsolidadoService {
    @Inject
    private PessoaDAO pessoaDAO;

    @Inject
    private PessoaSalarioConsolidadoDAO pessoaSalarioConsolidadoDAO;

    public List<PessoaSalarioConsolidado> getAll() {
        return this.pessoaSalarioConsolidadoDAO.getAll();
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
                pessoaSalarioConsolidado.setNome_cargo(pessoa.getCargo().getNome());
            }

            this.pessoaSalarioConsolidadoDAO.saveOrUpdate(pessoaSalarioConsolidado);
        });
    }
}
