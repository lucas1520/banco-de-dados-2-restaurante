package com.lucas.sysrestaurant.dto;

import com.lucas.sysrestaurant.model.Cliente;
import java.math.BigDecimal;
import java.util.List;

public record ContaClienteResponse(Cliente cliente, List<PedidoContaResponse> pedidos, BigDecimal total) {
}
