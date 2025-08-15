package bean.pessoaSalario;

import lombok.Data;
import models.PessoaSalarioConsolidado;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import services.PessoaSalarioConsolidadoService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
@Data
public class PessoaSalarioConsolidadoBean implements Serializable {
    @Inject
    private PessoaSalarioConsolidadoService pessoaSalarioConsolidadoService;

    private LazyDataModel<PessoaSalarioConsolidado> lazyModel;

    @PostConstruct
    public void init() {
        this.lazyModel = new PessoaSalarioConsolidadoLazyModel(pessoaSalarioConsolidadoService);
    }

    public void recalcularSalarios() {
        this.pessoaSalarioConsolidadoService.preencheDadosBasicosPessoaSalarioConsolidado();
    }
}