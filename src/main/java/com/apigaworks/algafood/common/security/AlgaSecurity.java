package com.apigaworks.algafood.common.security;

import com.apigaworks.algafood.domain.dto.pedido.PedidoDto;
import com.apigaworks.algafood.domain.model.Restaurante;
import com.apigaworks.algafood.domain.repository.PedidoRepository;
import com.apigaworks.algafood.domain.repository.RestauranteRepository;
import com.apigaworks.algafood.domain.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AlgaSecurity {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    //    busca e retorna o contexto
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Long getUsuarioId() {
//        quando vier um cliente_credencials ele vem sem id, então da erro de null
        Jwt jwt = (Jwt) getAuthentication().getPrincipal();
        String id = jwt.getClaim("usuario_id");

//        se realizar a conversao direta vai dar erro, pq um nao herda do outro
        Long longID = Long.parseLong(id);
        return longID;
    }

    //    metodo que vai ser chamado no checksegurity
    public boolean gerenciaRestaurante(Long restauranteId) {
        return restauranteRepository.existsResponvavel(restauranteId, getUsuarioId());
    }

//    nao usaremos essas funcoes
//    public boolean clienteDoPedido(Long pedidoId){
//        return true;
//    }
//
//    public boolean gerenciaRestauranteDoPedido(Long pedidoId){
//        return true;
//    }

    public boolean gerenciaRestauranteDoPedido(Long pedidoId) {
        Long restauranteId = pedidoRepository.findById(pedidoId).get().getRestaurante().getId();
        return restauranteRepository.existsResponvavel(restauranteId, getUsuarioId());

    }

}
