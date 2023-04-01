package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.model.Estado;
import com.apigaworks.algafood.domain.repository.EstadoRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class EstadoServiceTest {

    @MockBean
    private EstadoRepository estadoRepository;

    @Autowired
    EstadoService estadoService = new EstadoService(estadoRepository);

    private Long id;

    private List<Estado> listaEstados;

    private Estado estado;


    @BeforeEach
    public void setUp() {
        startValues();
        startMocks();
    }

    @Test
    @DisplayName("salvar estado")
    public void testSalvarEstado() {
        Estado e = estadoService.salvar(this.estado);
        Assertions.assertEquals(this.id, e.getId());
    }

    @Test
    @DisplayName("listar estados")
    public void testListarEstados() {
        List<Estado> listaEstados = estadoService.listar();
        Assertions.assertEquals(this.listaEstados, listaEstados);
        Assertions.assertEquals(this.listaEstados.size(), listaEstados.size());
        Assertions.assertEquals(this.listaEstados.getClass(), listaEstados.getClass());
    }

    @Test
    @DisplayName("buscar estado por id")
    public void testEstadoPorId(){
        Estado e = estadoService.buscar(this.id);
        Assertions.assertEquals(this.id, e.getId());
        Assertions.assertEquals(this.estado, e);
    }

    @Test
    @DisplayName("remover estado por id")
    public void testRemoverEstadoPorId(){
        estadoService.remover(this.id);
        Mockito.verify(estadoRepository).remover(this.estado);
    }

    public void startValues() {
        this.id = 1L;
        this.estado = new Estado(this.id, "Estado teste");
        this.listaEstados = new ArrayList<>();
        this.listaEstados.add(estado);
        this.listaEstados.add(estado);
    }

    public void startMocks() {
        Mockito.when(estadoRepository.salvar(this.estado)).thenReturn(this.estado);
        Mockito.when(estadoRepository.listar()).thenReturn(this.listaEstados);
        Mockito.when(estadoRepository.buscar(this.id)).thenReturn(this.estado);
    }
}
