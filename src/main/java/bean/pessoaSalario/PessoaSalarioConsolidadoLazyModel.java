package bean.pessoaSalario;

import models.PessoaSalarioConsolidado;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import services.PessoaSalarioConsolidadoService;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Dependent
public class PessoaSalarioConsolidadoLazyModel extends LazyDataModel<PessoaSalarioConsolidado> implements Serializable {

    private final PessoaSalarioConsolidadoService pessoaSalarioConsolidadoService;

    @Inject
    public PessoaSalarioConsolidadoLazyModel(PessoaSalarioConsolidadoService service) {
        this.pessoaSalarioConsolidadoService = service;
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        try {
            return pessoaSalarioConsolidadoService.countAll().intValue();
        } catch (Exception e) {
            return 0;
        }
    }


    @Override
    public List<PessoaSalarioConsolidado> load(int first, int pageSize,
                                               Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {

        String sortField = "pessoaId";
        boolean sortAsc = true;

        if (!sortBy.isEmpty()) {
            SortMeta sortMeta = sortBy.values().iterator().next();
            sortField = sortMeta.getField();
            sortAsc = sortMeta.getOrder().isAscending();
        }

        this.setRowCount(count(filterBy));

        return pessoaSalarioConsolidadoService.getPaginatedList(first, pageSize, sortField, sortAsc);
    }

    @Override
    public PessoaSalarioConsolidado getRowData(String rowKey) {
        if (rowKey == null || rowKey.isEmpty()) {
            return null;
        }
        int pessoaId = Integer.parseInt(rowKey);
        return pessoaSalarioConsolidadoService.getById(pessoaId);
    }

    @Override
    public String getRowKey(PessoaSalarioConsolidado pessoaSalarioConsolidado) {
        if (pessoaSalarioConsolidado == null) {
            return null;
        }
        return String.valueOf(pessoaSalarioConsolidado.getPessoaId());
    }
}
