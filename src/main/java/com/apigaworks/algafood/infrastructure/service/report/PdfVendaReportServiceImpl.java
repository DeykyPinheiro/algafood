package com.apigaworks.algafood.infrastructure.service.report;

import com.apigaworks.algafood.domain.filter.VendaDiariaFilter;
import com.apigaworks.algafood.domain.service.VendaReportService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class PdfVendaReportServiceImpl  implements VendaReportService {


    @Override
    public byte[] emitirVendarDiarias(VendaDiariaFilter filter) {
        return null;
    }
}
