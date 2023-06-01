package com.apigaworks.algafood.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {


    //    serve para fazermos a conversao de bytes
    private DataSize maxSize;


    //    estou convertendo a string para um tamanho de memoria valido
    @Override
    public void initialize(FileSize constraintAnnotation) {
        this.maxSize = DataSize.parse(constraintAnnotation.max());
    }

    //    esse sou obrigado a implementar, pq ele é quem faz a validacao das propriedade
//    value é o valor que eu recebo pela chamada do endpoint
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || value.getSize() <= maxSize.toBytes();
    }
}
