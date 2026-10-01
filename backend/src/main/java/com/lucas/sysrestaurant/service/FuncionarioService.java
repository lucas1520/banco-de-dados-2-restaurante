package com.lucas.sysrestaurant.service;

import com.lucas.sysrestaurant.model.Funcionario;
import com.lucas.sysrestaurant.repository.FuncionarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public List<Funcionario> findAll() {
        return repository.findAll();
    }

    public Funcionario findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionario não encontrado: " + id));
    }

    public Funcionario create(Funcionario funcionario) {
        funcionario.setMatricula(null);
        return repository.save(funcionario);
    }

    public Funcionario update(Long id, Funcionario funcionario) {
        findById(id);
        funcionario.setMatricula(id);
        return repository.save(funcionario);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
