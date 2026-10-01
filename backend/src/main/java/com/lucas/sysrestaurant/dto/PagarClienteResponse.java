package com.lucas.sysrestaurant.dto;

import com.lucas.sysrestaurant.model.Cliente;
import java.math.BigDecimal;

public record PagarClienteResponse(Cliente cliente, BigDecimal total, boolean mesaLiberada,
                                   int clientesPendentesNaMesa) {
}
