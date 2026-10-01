package com.lucas.sysrestaurant.dto;

import com.lucas.sysrestaurant.model.Cliente;
import java.util.List;

public record ClientePedidosResponse(Cliente cliente, List<PedidoContaResponse> pedidos) {
}
