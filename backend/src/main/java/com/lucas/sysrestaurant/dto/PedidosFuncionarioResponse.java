package com.lucas.sysrestaurant.dto;

import com.lucas.sysrestaurant.model.Funcionario;
import java.time.LocalDate;
import java.util.List;

public record PedidosFuncionarioResponse(Funcionario funcionario, LocalDate inicio, LocalDate fim,
                                         List<ClientePedidosResponse> clientes) {
}
