package bean.pessoaSalario;

import lombok.Data;
import models.PessoaSalarioConsolidado;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import services.PessoaSalarioConsolidadoService;
import services.RelatorioService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
@Data
public class PessoaSalarioConsolidadoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private transient PessoaSalarioConsolidadoService pessoaSalarioConsolidadoService;

    @Inject
    private transient RelatorioService relatorioService;

    private LazyDataModel<PessoaSalarioConsolidado> lazyModel;

    @PostConstruct
    public void init() {
        this.lazyModel = new PessoaSalarioConsolidadoLazyModel(pessoaSalarioConsolidadoService);
    }

    public void removerSalarioPessoa(PessoaSalarioConsolidado pessoaSalarioConsolidado) {
        this.pessoaSalarioConsolidadoService.removeSalarioDaPessoaSelecionada(pessoaSalarioConsolidado);
    }

    public void removeTodosSalarios() {
        this.pessoaSalarioConsolidadoService.removeTodosSalarios();
    }

    public void calcularSalarioPessoa(PessoaSalarioConsolidado pessoaSalarioConsolidado) {
        this.pessoaSalarioConsolidadoService.calcularSalarioPessoa(pessoaSalarioConsolidado);
    }

    public void recalcularSalarios() {
        this.pessoaSalarioConsolidadoService.preencheDadosBasicosPessoaSalarioConsolidado();
    }

    public StreamedContent baixarRelatorio() {
        return this.relatorioService.gerarRelatoriosSalariosPDF();
    }
}