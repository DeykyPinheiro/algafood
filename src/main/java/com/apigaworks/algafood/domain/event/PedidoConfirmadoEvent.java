package com.apigaworks.algafood.domain.event;

import com.apigaworks.algafood.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//usamos os nomes no passado, pq é algo que ja aconteceu, boas praticas
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoConfirmadoEvent {

    private Pedido pedido;


}
