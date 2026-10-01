package com.lucas.sysrestaurant.dto;

import java.math.BigDecimal;

public record IngredienteUsoResponse(Long idIngrediente, String nome, BigDecimal quantidade) {
}
