package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.filter.VendaDiariaFilter;
import net.sf.jasperreports.engine.JRException;

public interface VendaReportService {

    byte[] emitirVendarDiarias(VendaDiariaFilter filter) throws JRException;
}
