package com.lucas.sysrestaurant.repository;

import com.lucas.sysrestaurant.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByMesa_IdMesaAndPagoFalseAndIdClienteNot(Long idMesa, Long idCliente);

    long countByMesa_IdMesaAndPagoFalseAndIdClienteNot(Long idMesa, Long idCliente);

    List<Cliente> findByDataChegadaGreaterThanEqualAndDataChegadaLessThan(LocalDateTime inicio, LocalDateTime fim);

    long countByPagoTrueAndDataChegadaGreaterThanEqualAndDataChegadaLessThan(LocalDateTime inicio, LocalDateTime fim);
}
