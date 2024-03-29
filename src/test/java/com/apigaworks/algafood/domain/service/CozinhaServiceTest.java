package com.apigaworks.algafood.domain.service;

import com.apigaworks.algafood.common.io.Base64ProtocolResolver;
import com.apigaworks.algafood.domain.model.Cozinha;
import com.apigaworks.algafood.domain.repository.CozinhaRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@ContextConfiguration(initializers = Base64ProtocolResolver.class)
public class CozinhaServiceTest {

    @MockBean
    private CozinhaRepository cozinhaRepository;


    // TODO lembrar de retirar o autowired
    @Autowired
    private CozinhaService cozinhaService = new CozinhaService(cozinhaRepository);

    private List<Cozinha> listaCozinhas;

    private Cozinha cozinha;
    private Cozinha cozinha2;

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
    @DisplayName("atualizar cozinha")
    public void testAtualizarCozinha() {
        Cozinha c = cozinhaService.atualizar(this.id, this.cozinha2);
        Assertions.assertEquals(c.getId(), this.cozinha2.getId());
//        Mockito.verify(cozinhaRepository).atualizar(this.cozinha2);
    }

//    @Test
//    @DisplayName("listar cozinhas")
//    public void testListarCozinhas() {
//        Pageable pageable = PageRequest.of(0, 2);
//        Page<Cozinha> cozinhaList = cozinhaService.listar(pageable);
//        Assertions.assertEquals(2, cozinhaList.getContent());
//    }

    @Test
    @DisplayName("buscar cozinha por id")
    public void testBuscarPorId() {
        Cozinha c = cozinhaService.buscarPorId(this.id);
        Assertions.assertEquals(this.id, c.getId());
    }

    @Test
    @DisplayName("remover cozinha")
    public void testRemoverCozinha() {
        cozinhaService.remover(this.id);
        Mockito.verify(cozinhaRepository).delete(this.cozinha);
    }

    private void startMocks() {
        Mockito.when(cozinhaRepository.save(this.cozinha)).thenReturn(this.cozinha);
        Mockito.when(cozinhaRepository.findAll()).thenReturn(this.listaCozinhas);
        Mockito.when(cozinhaRepository.findAll()).thenReturn(this.listaCozinhas);
        Mockito.when(cozinhaRepository.findById(this.id)).thenReturn(Optional.ofNullable(this.cozinha));
        Mockito.when(cozinhaRepository.save(this.cozinha2)).thenReturn(this.cozinha2);
    }

    public void startValues() {
        this.id = 1L;
        this.cozinha = new Cozinha(1L, "Indiana ana");
        this.cozinha2 = new Cozinha(1L, "cozinha atualizada");
        this.listaCozinhas = new ArrayList<>();
        this.listaCozinhas.add(cozinha);
        this.listaCozinhas.add(cozinha);
    }
}
