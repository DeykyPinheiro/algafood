package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.model.Estado;
import com.apigaworks.algafood.domain.repository.CidadeRepository;
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
class CidadeServiceTest {

    @MockBean
    private CidadeRepository cidadeRepository;

    @Autowired
    private CidadeService cidadeService = new CidadeService(cidadeRepository);

    private List<Cidade> listaCidades = new ArrayList<>();
    private Cidade cidade;
    private Estado estado;

    private Long id;

    @BeforeEach
    public void setUp() {
        startValues();
        startMocks();
    }

    @Test
    @DisplayName("salvar cidade")
    void testSalvarCidade() {
        Cidade c = cidadeService.salvar(this.cidade);
        Assertions.assertEquals(this.cidade, c);
        Assertions.assertEquals(this.cidade.getClass(), c.getClass());
        Mockito.verify(cidadeRepository).save(this.cidade);
    }

    @Test
    @DisplayName("buscar por id")
    void testBuscarPorId() {
        Cidade c = cidadeService.buscarPorId(this.id);
        Assertions.assertEquals(this.cidade.getClass(), c.getClass());
        Assertions.assertEquals(this.id, c.getId());
    }

    @Test
    @DisplayName("listar cidades")
    void listar() {
        List<Cidade> listaCidades = cidadeService.listar();
        Mockito.verify(cidadeRepository).findAll();
        Assertions.assertEquals(this.listaCidades.getClass(), listaCidades.getClass());
        Assertions.assertEquals(this.listaCidades.size(), listaCidades.size());
    }

    @Test
    @DisplayName("remover cidade")
    void remover() {
        cidadeService.remover(this.id);
        Mockito.verify(cidadeRepository).delete(this.cidade);
    }

    private void startValues() {
        this.id = 1L;
        this.estado = new Estado(1l);
        this.cidade = new Cidade(1L, "teste", estado);

        listaCidades.add(this.cidade);
        listaCidades.add(this.cidade);
        listaCidades.add(this.cidade);
        listaCidades.add(this.cidade);
    }

    private void startMocks() {
        Mockito.when(cidadeRepository.save(cidade)).thenReturn(cidade);
        Mockito.when(cidadeRepository.findById(this.id)).thenReturn(Optional.ofNullable(cidade));
        Mockito.when(cidadeRepository.findAll()).thenReturn(this.listaCidades);
    }

}