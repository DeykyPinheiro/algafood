package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.filter.VendaDiariaFilter;
import com.apigaworks.algafood.domain.model.dto.VendaDiaria;

import java.util.List;

public interface VendaQueryService {

    List<VendaDiaria> consultarVendaDiarias(VendaDiariaFilter filter);
}
