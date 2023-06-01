package com.apigaworks.algafood.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

    private List<String> alloweds;


    //    estou convertendo a string para um tamanho de memoria valido
    @Override
    public void initialize(FileContentType constraintAnnotation) {
        alloweds = Arrays.stream(constraintAnnotation.allowed()).collect(Collectors.toList());
    }

    //    esse sou obrigado a implementar, pq ele é quem faz a validacao das propriedade
//    value é o valor que eu recebo pela chamada do endpoint
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext constraintValidatorContext) {
        return  value == null || alloweds.contains(value.getContentType())  ;
    }
}
