package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.filter.VendaDiariaFilter;

public interface VendaReportService {

    byte[] emitirVendarDiarias(VendaDiariaFilter filter);
}
