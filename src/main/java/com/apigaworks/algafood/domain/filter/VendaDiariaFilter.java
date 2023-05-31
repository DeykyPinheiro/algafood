package com.apigaworks.algafood.domain.filter;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.Date;

public record VendaDiariaFilter(

        Long restauranteId,

//        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) uso essa anotacao
//        pq sem ela o spring nao consegue conveter para data
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        Date dataCriacaoInicio,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        Date dataCriacaofim
) {
}
