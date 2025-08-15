package bean.pessoaSalario;

import lombok.Data;
import models.PessoaSalarioConsolidado;
import services.PessoaSalarioConsolidadoService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@Data
@RequestScoped
public class PessoaSalarioConsolidadoBean implements Serializable {
    @Inject
    private PessoaSalarioConsolidadoService pessoaSalarioConsolidadoService;

    private List<PessoaSalarioConsolidado> pessoasConsolidadas;

    private int pessoaSelecionadaId;

    @PostConstruct
    public void init() {
        this.pessoaSalarioConsolidadoService.preencheDadosBasicosPessoaSalarioConsolidado();
        this.pessoasConsolidadas = this.pessoaSalarioConsolidadoService.getAll();
    }
}
