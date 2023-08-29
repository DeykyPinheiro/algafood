package com.apigaworks.algafood.domain.dto.foto;

import com.apigaworks.algafood.common.validation.FileContentType;
import com.apigaworks.algafood.common.validation.FileSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public record FotoDto(

//        o limite é definido aqui com a anotacao filesize

        @Schema(example = "fotos")
        @NotNull
        @FileSize(max = "500KB")
        @FileContentType(allowed = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
        MultipartFile arquivo

) {
}
