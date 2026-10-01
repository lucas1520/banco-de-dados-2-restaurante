package com.lucas.sysrestaurant.repository;

import com.lucas.sysrestaurant.model.Pedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByCliente_IdCliente(Long idCliente);

    List<Pedido> findByCliente_Mesa_IdMesaAndCliente_PagoFalseOrderByIdPedido(Long idMesa);

    boolean existsByCliente_IdClienteAndEntregueFalse(Long idCliente);

    @Query("select p from Pedido p where p.funcionario.matricula = :matricula "
            + "and p.cliente.dataChegada >= :inicio and p.cliente.dataChegada < :fim order by p.idPedido")
    List<Pedido> findByFuncionarioNoPeriodo(@Param("matricula") Long matricula,
                                            @Param("inicio") LocalDateTime inicio,
                                            @Param("fim") LocalDateTime fim);
}
