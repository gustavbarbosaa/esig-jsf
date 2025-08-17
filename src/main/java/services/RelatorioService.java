package services;

import models.PessoaSalarioConsolidado;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

@RequestScoped
public class RelatorioService {
    @Inject
    private PessoaSalarioConsolidadoService pessoaSalarioConsolidadoService;

    public StreamedContent gerarRelatoriosSalariosPDF() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/relatorios/salarios.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            List<PessoaSalarioConsolidado> listaDeDados = pessoaSalarioConsolidadoService.getAll();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listaDeDados);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
            exporter.exportReport();

            return DefaultStreamedContent.builder()
                    .name("relatorio_salarios.pdf")
                    .contentType("application/pdf")
                    .stream(() -> new ByteArrayInputStream(baos.toByteArray()))
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
