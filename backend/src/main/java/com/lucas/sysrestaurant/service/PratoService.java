package com.lucas.sysrestaurant.service;

import com.lucas.sysrestaurant.dto.PratoPossivelResponse;
import com.lucas.sysrestaurant.model.ComposicaoPrato;
import com.lucas.sysrestaurant.model.Prato;
import com.lucas.sysrestaurant.repository.ComposicaoPratoRepository;
import com.lucas.sysrestaurant.repository.PratoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PratoService {

    private final PratoRepository repository;
    private final ComposicaoPratoRepository composicaoPratoRepository;

    public PratoService(PratoRepository repository, ComposicaoPratoRepository composicaoPratoRepository) {
        this.repository = repository;
        this.composicaoPratoRepository = composicaoPratoRepository;
    }

    public List<Prato> findAll() {
        return repository.findAll();
    }

    public Prato findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado: " + id));
    }

    public Prato create(Prato prato) {
        prato.setIdPrato(null);
        return repository.save(prato);
    }

    public Prato update(Long id, Prato prato) {
        findById(id);
        prato.setIdPrato(id);
        return repository.save(prato);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    public List<PratoPossivelResponse> possiveis() {
        Map<Long, List<ComposicaoPrato>> composicoes = composicaoPratoRepository.findAll().stream()
                .collect(Collectors.groupingBy(c -> c.getPrato().getIdPrato()));
        return repository.findByAtivoTrueOrderByNome().stream()
                .map(p -> new PratoPossivelResponse(p.getIdPrato(), p.getNome(),
                        quantidadePossivel(composicoes.getOrDefault(p.getIdPrato(), List.of()))))
                .toList();
    }

    private Integer quantidadePossivel(List<ComposicaoPrato> composicao) {
        Integer menor = null;
        for (ComposicaoPrato c : composicao) {
            if (c.getQuantidade() == null || c.getQuantidade().signum() <= 0) {
                continue;
            }
            int n = BigDecimal.valueOf(c.getIngrediente().getEstoque())
                    .divide(c.getQuantidade(), 0, RoundingMode.DOWN)
                    .intValue();
            menor = menor == null ? Math.max(n, 0) : Math.min(menor, Math.max(n, 0));
        }
        return menor;
    }
}
