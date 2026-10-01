package com.lucas.sysrestaurant.service;

import com.lucas.sysrestaurant.model.ComposicaoPrato;
import com.lucas.sysrestaurant.model.ComposicaoPratoId;
import com.lucas.sysrestaurant.repository.ComposicaoPratoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ComposicaoPratoService {

    private final ComposicaoPratoRepository repository;

    public ComposicaoPratoService(ComposicaoPratoRepository repository) {
        this.repository = repository;
    }

    public List<ComposicaoPrato> findAll() {
        return repository.findAll();
    }

    public ComposicaoPrato findById(Long idPrato, Long idIngrediente) {
        return repository.findById(new ComposicaoPratoId(idPrato, idIngrediente))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "ComposicaoPrato não encontrada: prato " + idPrato + ", ingrediente " + idIngrediente));
    }

    public ComposicaoPrato create(ComposicaoPrato composicaoPrato) {
        if (composicaoPrato.getPrato() == null || composicaoPrato.getPrato().getIdPrato() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prato é obrigatório");
        }
        if (composicaoPrato.getIngrediente() == null || composicaoPrato.getIngrediente().getIdIngrediente() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingrediente é obrigatório");
        }
        ComposicaoPratoId id = new ComposicaoPratoId(composicaoPrato.getPrato().getIdPrato(),
                composicaoPrato.getIngrediente().getIdIngrediente());
        if (repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ingrediente já faz parte da composição do prato: prato " + id.getPrato()
                            + ", ingrediente " + id.getIngrediente());
        }
        return repository.save(composicaoPrato);
    }

    public ComposicaoPrato update(Long idPrato, Long idIngrediente, ComposicaoPrato composicaoPrato) {
        ComposicaoPrato existente = findById(idPrato, idIngrediente);
        existente.setQuantidade(composicaoPrato.getQuantidade());
        return repository.save(existente);
    }

    public void delete(Long idPrato, Long idIngrediente) {
        repository.delete(findById(idPrato, idIngrediente));
    }
}
