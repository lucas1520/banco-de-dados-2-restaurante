package com.lucas.sysrestaurant.repository;

import com.lucas.sysrestaurant.model.ItemPedido;
import com.lucas.sysrestaurant.model.ItemPedidoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPedidoId> {

    List<ItemPedido> findByPedido_IdPedido(Long idPedido);

    List<ItemPedido> findByPedido_Cliente_IdCliente(Long idCliente);

    List<ItemPedido> findByPedido_Cliente_Mesa_IdMesaAndPedido_Cliente_PagoFalse(Long idMesa);

    @Query("select i from ItemPedido i where i.pedido.cliente.dataChegada >= :inicio "
            + "and i.pedido.cliente.dataChegada < :fim")
    List<ItemPedido> findByChegadaNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("select i from ItemPedido i where i.pedido.cliente.pago = true "
            + "and i.pedido.cliente.dataChegada >= :inicio and i.pedido.cliente.dataChegada < :fim")
    List<ItemPedido> findPagosByChegadaNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("select i from ItemPedido i where i.pedido.funcionario.matricula = :matricula "
            + "and i.pedido.cliente.dataChegada >= :inicio and i.pedido.cliente.dataChegada < :fim")
    List<ItemPedido> findByFuncionarioNoPeriodo(@Param("matricula") Long matricula,
                                                @Param("inicio") LocalDateTime inicio,
                                                @Param("fim") LocalDateTime fim);
}
