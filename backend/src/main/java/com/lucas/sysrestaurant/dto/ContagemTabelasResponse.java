package com.lucas.sysrestaurant.dto;

public record ContagemTabelasResponse(long mesas, long clientes, long funcionarios, long pratos, long ingredientes,
                                      long composicoes, long pedidos, long itens) {
}
