package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.domain.model.Cidade;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.model.Estado;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
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
public class CozinhaServiceTest {

    @MockBean
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CozinhaService cozinhaService = new CozinhaService(cozinhaRepository);

    private List<Cozinha> listaCozinhas;

    private Cozinha cozinha;

    private Long id;

    @BeforeEach
    public void setUp() {
        startValues();
        startMocks();
    }

    @Test
    @DisplayName("salvar cozinha")
    public void testSalvarCozinha() {
        Cozinha c = cozinhaService.salvar(this.cozinha);
        Assertions.assertEquals(c.getClass(), this.cozinha.getClass());
        Assertions.assertEquals(c.getId(), this.cozinha.getId());
    }

    @Test
    @DisplayName("listar cozinhas")
    public void testListarCozinhas(){
        List<Cozinha> cozinhaList = cozinhaService.listar();
        Assertions.assertEquals(2, cozinhaList.size());
    }

    @Test
    @DisplayName("buscar cozinha por id")
    public void testBuscarPorId(){
        Cozinha c = cozinhaService.buscarPorId(this.id);
        Assertions.assertEquals(this.id, c.getId());
    }

    @Test
    @DisplayName("remover cozinha")
    public void testRemoverCozinha(){
        cozinhaService.remover(this.id);
        Mockito.verify(cozinhaRepository).remover(this.cozinha);
    }

    private void startMocks() {
        Mockito.when(cozinhaRepository.salvar(this.cozinha)).thenReturn(this.cozinha);
        Mockito.when(cozinhaRepository.listar()).thenReturn(this.listaCozinhas);
        Mockito.when(cozinhaRepository.listar()).thenReturn(this.listaCozinhas);
        Mockito.when(cozinhaRepository.buscar(this.id)).thenReturn(this.cozinha);
    }

    public void startValues() {
        this.id = 1L;
        this.cozinha = new Cozinha(1L, "Indiana ana");
        this.listaCozinhas = new ArrayList<>();
        this.listaCozinhas.add(cozinha);
        this.listaCozinhas.add(cozinha);
    }
}
