package com.apigaworks.algafood.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

//Object pq posso validar qualquer coisa e nao só restaurante
public class ValorZeroIncluiDescricaoValidator implements ConstraintValidator<ValorZeroIncluiDescricao, Object> {

    private String valorField;
    private String descricaoField;

    private String descricaoObrigatoria;


    @Override
    public void initialize(ValorZeroIncluiDescricao constraint) {
        valorField = constraint.valorField();
        descricaoField = constraint.descricaoField();
        descricaoObrigatoria = constraint.descricaoObrigatoria();
    }

    @Override
    public boolean isValid(Object objetoValidacao, ConstraintValidatorContext context) {

        Boolean ehValido = true;

//        buscar o valor, da taxaFrete, ja que dessa vez é um restaurante
//        mas posso usar em qualquer outro
        try {
            BigDecimal valor = (BigDecimal) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), valorField)
                    .getReadMethod().invoke(objetoValidacao);

            String descricao = (String) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), descricaoField)
                    .getReadMethod().invoke(objetoValidacao);

            if (valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null) {
                ehValido = descricao.toLowerCase().contains(descricaoObrigatoria.toLowerCase());
            }

            return ehValido;

        } catch (Exception e) {
            throw new ValidationException(e);
        }


    }
}
