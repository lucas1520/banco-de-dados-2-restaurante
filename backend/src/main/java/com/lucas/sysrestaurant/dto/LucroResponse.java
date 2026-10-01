package com.lucas.sysrestaurant.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LucroResponse(LocalDate inicio, LocalDate fim, int clientes, BigDecimal valorBruto,
                            BigDecimal custoIngredientes, BigDecimal lucro) {
}
