package com.lucas.sysrestaurant.service;

import com.lucas.sysrestaurant.dto.AdicionarItemRequest;
import com.lucas.sysrestaurant.dto.AlterarItemRequest;
import com.lucas.sysrestaurant.model.ComposicaoPrato;
import com.lucas.sysrestaurant.model.Ingrediente;
import com.lucas.sysrestaurant.model.ItemPedido;
import com.lucas.sysrestaurant.model.ItemPedidoId;
import com.lucas.sysrestaurant.model.Pedido;
import com.lucas.sysrestaurant.model.Prato;
import com.lucas.sysrestaurant.repository.ComposicaoPratoRepository;
import com.lucas.sysrestaurant.repository.IngredienteRepository;
import com.lucas.sysrestaurant.repository.ItemPedidoRepository;
import com.lucas.sysrestaurant.repository.PedidoRepository;
import com.lucas.sysrestaurant.repository.PratoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ItemPedidoService {

    private final ItemPedidoRepository repository;
    private final PedidoRepository pedidoRepository;
    private final PratoRepository pratoRepository;
    private final ComposicaoPratoRepository composicaoPratoRepository;
    private final IngredienteRepository ingredienteRepository;

    public ItemPedidoService(ItemPedidoRepository repository, PedidoRepository pedidoRepository,
                              PratoRepository pratoRepository, ComposicaoPratoRepository composicaoPratoRepository,
                              IngredienteRepository ingredienteRepository) {
        this.repository = repository;
        this.pedidoRepository = pedidoRepository;
        this.pratoRepository = pratoRepository;
        this.composicaoPratoRepository = composicaoPratoRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    @Transactional
    public ItemPedido adicionarItem(Long idPedido, AdicionarItemRequest request) {
        Pedido pedido = buscarPedidoEditavel(idPedido);
        validarQuantidade(request.quantidade());
        Prato prato = pratoRepository.findById(request.idPrato())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prato não encontrado: " + request.idPrato()));
        if (Boolean.FALSE.equals(prato.getAtivo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Prato inativo, não pode ser adicionado ao pedido: " + prato.getNome());
        }

        ItemPedido existente = repository.findById(new ItemPedidoId(idPedido, prato.getIdPrato())).orElse(null);
        if (existente != null) {
            int quantidadeNova = existente.getQuantidade() + request.quantidade();
            ajustarEstoque(prato, existente.getQuantidade(), quantidadeNova);
            existente.setQuantidade(quantidadeNova);
            return repository.save(existente);
        }

        ajustarEstoque(prato, 0, request.quantidade());

        return repository.save(new ItemPedido(pedido, prato, request.quantidade(), prato.getValor()));
    }

    @Transactional
    public ItemPedido alterarItem(Long idPedido, Long idPrato, AlterarItemRequest request) {
        buscarPedidoEditavel(idPedido);
        validarQuantidade(request.quantidade());
        ItemPedido item = buscarItemDoPedido(idPedido, idPrato);

        ajustarEstoque(item.getPrato(), item.getQuantidade(), request.quantidade());

        item.setQuantidade(request.quantidade());
        return repository.save(item);
    }

    @Transactional
    public void removerItem(Long idPedido, Long idPrato) {
        buscarPedidoEditavel(idPedido);
        ItemPedido item = buscarItemDoPedido(idPedido, idPrato);

        ajustarEstoque(item.getPrato(), item.getQuantidade(), 0);

        repository.delete(item);
    }

    @Transactional
    public void removerItensDoPedido(Long idPedido) {
        for (ItemPedido item : repository.findByPedido_IdPedido(idPedido)) {
            ajustarEstoque(item.getPrato(), item.getQuantidade(), 0);
            repository.delete(item);
        }
    }

    private Pedido buscarPedidoEditavel(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado: " + idPedido));
        if (Boolean.TRUE.equals(pedido.getEntregue())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Pedido já foi entregue, não é possível alterar itens: " + idPedido);
        }
        return pedido;
    }

    private ItemPedido buscarItemDoPedido(Long idPedido, Long idPrato) {
        return repository.findById(new ItemPedidoId(idPedido, idPrato))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Prato " + idPrato + " não está no pedido " + idPedido));
    }

    private void validarQuantidade(Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade deve ser maior que zero");
        }
    }

    private void ajustarEstoque(Prato prato, int quantidadeAntiga, int quantidadeNova) {
        List<ComposicaoPrato> composicoes = composicaoPratoRepository.findByPrato_IdPrato(prato.getIdPrato());

        if (quantidadeNova > quantidadeAntiga) {
            int aumento = quantidadeNova - quantidadeAntiga;
            for (ComposicaoPrato composicao : composicoes) {
                BigDecimal necessario = composicao.getQuantidade().multiply(BigDecimal.valueOf(aumento));
                Ingrediente ingrediente = composicao.getIngrediente();
                if (BigDecimal.valueOf(ingrediente.getEstoque()).compareTo(necessario) < 0) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Estoque insuficiente de " + ingrediente.getNome());
                }
            }
        }

        for (ComposicaoPrato composicao : composicoes) {
            int baixaNova = composicao.getQuantidade().multiply(BigDecimal.valueOf(quantidadeNova)).intValue();
            int baixaAntiga = composicao.getQuantidade().multiply(BigDecimal.valueOf(quantidadeAntiga)).intValue();
            Ingrediente ingrediente = composicao.getIngrediente();
            ingrediente.setEstoque(ingrediente.getEstoque() - (baixaNova - baixaAntiga));
            ingredienteRepository.save(ingrediente);
        }
    }
}
