package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.model.Restaurante;
import com.apigaworks.algafood.domain.repository.RestauranteRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class RestauranteServiceTest {

    @MockBean
    private RestauranteRepository restauranteRepository;

    @Autowired
    private RestauranteService restaurenteService = new RestauranteService(restauranteRepository);

    @Autowired
    private CozinhaService cozinhaService;

    private Restaurante restaurante;
    private Long id;

    private List<Restaurante> listaRestaurentes;

    @BeforeEach
    public void setUp() {
        startValues();
        startMocks();
    }

//    @Test
//    @DisplayName("salvar restaurante")
//    public void testSalvarRestaurente() {
//        Restaurante r = restaurenteService.salvar(this.restaurante);
//        Assertions.assertNotNull(r.getId());
//        Assertions.assertEquals(this.restaurante.getId(), r.getId());
//    }

    @Test
    @DisplayName("lista restaurante")
    public void testListarRestaurante() {
        List<Restaurante> listaRestaurentes = restauranteRepository.findAll();

        Assertions.assertEquals(this.listaRestaurentes, listaRestaurentes);
        Assertions.assertEquals(2, listaRestaurentes.size());
    }

    @Test
    @DisplayName("buscar restaurante por id")
    public void testBuscarRestaurantePorId() {
        Restaurante r = restaurenteService.buscar(this.id);
        Assertions.assertEquals(this.id, r.getId());
        Assertions.assertEquals(this.restaurante, r);
    }

    @Test
    @DisplayName("remover restaunte por id")
    public void testRemoverRestaurantePorId() {
        restaurenteService.remover(this.id);
        Mockito.verify(restauranteRepository).delete(this.restaurante);
    }


    private void startValues() {
        this.id = 1L;
        this.restaurante = new Restaurante(id, "nome restaurante", new Cozinha(1L, "teste"));
        this.listaRestaurentes = new ArrayList<>();
        this.listaRestaurentes.add(this.restaurante);
        this.listaRestaurentes.add(this.restaurante);
    }

    private void startMocks() {
        Mockito.when(restauranteRepository.save(this.restaurante)).thenReturn(this.restaurante);
        Mockito.when(restauranteRepository.findAll()).thenReturn(this.listaRestaurentes);
        Mockito.when(restauranteRepository.findById(this.id)).thenReturn(Optional.ofNullable(this.restaurante));
    }


}
