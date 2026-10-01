package com.lucas.sysrestaurant.repository;

import com.lucas.sysrestaurant.model.Mesa;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MesaRepository extends JpaRepository<Mesa, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Mesa m where m.idMesa = :id")
    Optional<Mesa> findByIdForUpdate(@Param("id") Long id);
}
