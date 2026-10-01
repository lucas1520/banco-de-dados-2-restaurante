package com.lucas.sysrestaurant.dto;

import java.time.LocalDate;
import java.util.List;

public record DiaDemandaResponse(LocalDate dia, int clientes, List<PratoQuantidadeResponse> pratos,
                                 List<IngredienteUsoResponse> ingredientes) {
}
