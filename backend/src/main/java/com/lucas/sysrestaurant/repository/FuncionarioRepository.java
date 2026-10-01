package com.lucas.sysrestaurant.repository;

import com.lucas.sysrestaurant.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
