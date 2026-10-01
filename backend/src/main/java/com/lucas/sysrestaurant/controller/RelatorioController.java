package com.lucas.sysrestaurant.controller;

import com.lucas.sysrestaurant.dto.DiaDemandaResponse;
import com.lucas.sysrestaurant.dto.LucroResponse;
import com.lucas.sysrestaurant.dto.PedidosFuncionarioResponse;
import com.lucas.sysrestaurant.service.RelatorioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService service;

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    @GetMapping("/funcionarios/{matricula}/pedidos")
    public PedidosFuncionarioResponse pedidosDoFuncionario(
            @PathVariable Long matricula,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.pedidosDoFuncionario(matricula, inicio, fim);
    }

    @GetMapping("/lucro")
    public LucroResponse lucro(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.lucro(inicio, fim);
    }

    @GetMapping("/demanda")
    public List<DiaDemandaResponse> demanda(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.demanda(inicio, fim);
    }
}
