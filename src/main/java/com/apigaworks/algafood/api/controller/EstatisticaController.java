package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.filter.VendaDiariaFilter;
import com.apigaworks.algafood.domain.model.dto.VendaDiaria;
import com.apigaworks.algafood.domain.service.VendaQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticaController {

    @Autowired
    private VendaQueryService vendaQueryService;

    @GetMapping("/vendas-diarias")
    public List<VendaDiaria> consulVendaDiarias(VendaDiariaFilter filter){
        return vendaQueryService.consultarVendaDiarias(filter);
    }
}
