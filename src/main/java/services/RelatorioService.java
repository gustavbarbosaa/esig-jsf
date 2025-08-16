package services;

import models.PessoaSalarioConsolidado;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

@RequestScoped
public class RelatorioService {
    @Inject
    private PessoaSalarioConsolidadoService pessoaSalarioConsolidadoService;

    public void gerarRelatoriosSalariosPDF() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

            InputStream inputStream = getClass().getResourceAsStream("/relatorios/salarios.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            List<PessoaSalarioConsolidado> listaDeDados = pessoaSalarioConsolidadoService.getAll();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listaDeDados);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"relatorio_salarios.pdf\"");

            OutputStream outputStream = response.getOutputStream();
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            exporter.exportReport();

            context.responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
