package com.apigaworks.algafood.infrastructure.service.report;

import com.apigaworks.algafood.domain.filter.VendaDiariaFilter;
import com.apigaworks.algafood.domain.service.VendaQueryService;
import com.apigaworks.algafood.domain.service.VendaReportService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;

@Service
public class PdfVendaReportServiceImpl implements VendaReportService {

    @Autowired
    private VendaQueryService vendaQueryService;


    @Override
    public byte[] emitirVendarDiarias(VendaDiariaFilter filter) throws JRException {
        try {
//            var inputStrem = this.getClass().
//                    getResourceAsStream("src/main/resources/relatorios/vendas-diarias.jasper");

            String pathToFile = "src/main/resources/relatorios/vendas-diarias.jasper";
            JasperReport inputStrem = (JasperReport) JRLoader.loadObjectFromFile(pathToFile);


            var parameters = new HashMap<String, Object>();
            parameters.put("REPORT_LOCALE", new Locale("pt", "BR"));

            var vendasDiarias = vendaQueryService.consultarVendaDiarias(filter);
            var dataSource = new JRBeanCollectionDataSource(vendasDiarias);

            var jasperPrint = JasperFillManager.fillReport(inputStrem, null, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
        catch (Exception e){
            throw new ReportException("Não foi possível emitir relatório de vendas diárias", e);
        }
    }
}
