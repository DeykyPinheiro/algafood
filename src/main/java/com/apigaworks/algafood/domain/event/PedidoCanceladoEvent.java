package com.apigaworks.algafood.domain.event;

import com.apigaworks.algafood.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCanceladoEvent {

    private Pedido pedido;
}
