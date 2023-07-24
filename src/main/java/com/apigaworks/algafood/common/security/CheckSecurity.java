package com.apigaworks.algafood.common.security;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface CheckSecurity {

    public @interface Cozinhas {

        @PreAuthorize("hasAuthority('EDITAR_COZINHAS')")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {
        }

        @PreAuthorize("isAuthenticated()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }
    }

    public @interface Restaurantes {
        @PreAuthorize("hasAuthority('EDITAR_RESTAURANTES')")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeGerenciarCadastro {
        }

        @PreAuthorize("isAuthenticated()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }

        //         tem um bean chamado "AlgaSecurity" que o spring esta injetando
//        restauranteId vem da funcao onde é chamado, dai eu passo o valor pra funcao
        @PreAuthorize("hasAuthority('EDITAR_RESTAURANTES') or" +
                "@algaSecurity.gerenciaRestaurante(#restauranteId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeGerenciarFuncionamento {
        }
    }

    public @interface Pedidos {

////        isso é só pra saber que da pra fazer desse jeito
//        @PreAuthorize("hasAuthority('CONSULTAR_PEDIDOS') or " +
////                "@algaSecurity.clienteDoPedido(#pedidoId) or" +
////                "@algaSecurity.gerenciaRestauranteDoPedido(#pedidoId)")
////        @Retention(RetentionPolicy.RUNTIME)
////        @Target(ElementType.METHOD)
////        public @interface PodeBuscar {
////        }
////    }

        //        o postauthroize checa depois da execucao do metodo
//        returnObject é o objeto padrao de retorno

        @PreAuthorize("hasAuthority('CONSULTAR_PEDIDOS') or " + // é o admin geral
                "@algaSecurity.usuarioAutenticadoIgual(#pedidoFilter.clienteId)  or" + //é do usuario
                "@algaSecurity.gerenciaRestaurante(#pedidoFilter.restauranteId)") // é gerenciado pelo user
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeBuscar {
        }

        @PreAuthorize("isAuthenticated()")
        @PostAuthorize("hasAuthority('CONSULTAR_PEDIDOS') or " + // é o admin geral
                "@algaSecurity.usuarioAutenticadoIgual( #pedidoFilter.clienteId) or" + //é do usuario
                "@algaSecurity.gerenciaRestaurante(#pedidoId)") // é gerenciado pelo user
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodePesquisar {
        }

        @PreAuthorize("isAuthenticated()")
        @PostAuthorize("hasAuthority('CONSULTAR_PEDIDOS') or " + // é o admin geral
                "@algaSecurity.gerenciaRestauranteDoPedido(#pedidoId)") // é gerenciado pelo user
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeGerenciarPedido {
        }
    }

    public @interface FormaPagamento {

        @PreAuthorize("isAuthenticated()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }

        @PreAuthorize("isAuthenticated() and hasAuthority('EDITAR_FORMAS_PAGAMENTO')")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {
        }

    }

    public @interface Cidade {

        @PreAuthorize("isAuthenticated()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }

        @PreAuthorize("isAuthenticated() and hasAuthority('EDITAR_CIDADES')")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {
        }

    }

    public @interface Estado {

        @PreAuthorize("isAuthenticated() ")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }

        @PreAuthorize("isAuthenticated() and hasAuthority('EDITAR_ESTADOS')")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {
        }

    }

    public @interface UsuarioGruposPermissoes {

        @PreAuthorize("isAuthenticated() and @algaSecurity.usuarioAutenticadoIgual(#id)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeAlterarPropriaSenha {
        }

        @PreAuthorize("hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES') or " +
                "@algaSecurity.usuarioAutenticadoIgual(#id)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeAlterarUsuario {
        }

        @PreAuthorize("hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES')")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeEditar {
        }

        @PreAuthorize("hasAuthority('CONSULTAR_USUARIOS_GRUPOS_PERMISSOES')")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }
    }


    public @interface Estatistica {

        @PreAuthorize("hasAuthority('GERAR_RELATORIOS')")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface PodeConsultar {
        }

    }

}
