package com.apigaworks.algafood.api.controller;

import com.apigaworks.algafood.domain.filter.VendaDiariaFilter;
import com.apigaworks.algafood.domain.model.dto.VendaDiaria;
import com.apigaworks.algafood.domain.service.VendaQueryService;
import com.apigaworks.algafood.domain.service.VendaReportService;
import net.sf.jasperreports.engine.JRException;
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
//    @GetMapping(value = "/vendas-diarias")
    public List<VendaDiaria> consulVendaDiarias(VendaDiariaFilter filter) {
        return vendaQueryService.consultarVendaDiarias(filter);
    }

    //    isso aqui é chamado quando o user aceita application/pdf
    @GetMapping(value = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
//    @GetMapping(value = "/vendas-diarias-pdf")
    public ResponseEntity<byte[]> consulVendaDiariasPdf(VendaDiariaFilter filter) throws JRException {
        byte[] bytesPdf = VendaReportService.emitirVendarDiarias(filter);
//
        var headers = new HttpHeaders();
//        attachment -> isso serve pra baixar direto
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "filename=vendas-diarias.pdf");


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .headers(headers)
                .body(bytesPdf);
    }
}

// para fazer a requisicao no navegador usamos a requisicao com o get e com o acept
// 'Accept': 'application/pdf'
//    fetch('http://localhost:8080/estatisticas/vendas-diarias', {
//        method: 'GET',
//                headers: {
//            'Accept-Encoding': 'gzip, deflate, br',
//                    'Accept': 'application/pdf'
//        }
//    })
//        .then(response => response.blob())
//        .then(blob => {
//        // Cria um objeto URL temporário para o blob
//        const url = URL.createObjectURL(blob);
//
//        // Cria um elemento <a> invisível para iniciar o download
//        const link = document.createElement('a');
//        link.href = url;
//        link.download = 'vendas-diarias.pdf';
//
//        // Adiciona o elemento <a> ao corpo do documento
//        document.body.appendChild(link);
//
//        // Clica no elemento <a> para iniciar o download
//        link.click();
//
//        // Remove o elemento <a> do corpo do documento
//        document.body.removeChild(link);
//
//        // Limpa o objeto URL temporário
//        URL.revokeObjectURL(url);
//        })
//        .catch(error => console.log(error));