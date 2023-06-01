package com.apigaworks.algafood.domain.dto.foto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record FotoDto(
        @NotNull
        MultipartFile arquivo,

        @NotBlank
        String descricao
) {
}
