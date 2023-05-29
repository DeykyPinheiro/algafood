package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.filter.VendaDiariaFilter;
import com.apigaworks.algafood.domain.model.dto.VendaDiaria;
import com.apigaworks.algafood.domain.service.VendaQueryService;
import com.apigaworks.algafood.domain.service.VendaReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private VendaReportService VendaReportService;

    //    isso aqui é chamado quando o user aceita application/json
    @GetMapping(value = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VendaDiaria> consulVendaDiarias(VendaDiariaFilter filter) {
        return vendaQueryService.consultarVendaDiarias(filter);
    }

    //    isso aqui é chamado quando o user aceita application/pdf
    @GetMapping(value = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> consulVendaDiariasPdf(VendaDiariaFilter filter) {
        byte[] bytesPdf = VendaReportService.emitirVendarDiarias(filter);

        var headers = new HttpHeaders();
//        attachment -> isso serve pra baixar direto
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .headers(headers)
                .body(bytesPdf);
    }
}
