package com.lucas.sysrestaurant.controller;

import com.lucas.sysrestaurant.dto.ContagemTabelasResponse;
import com.lucas.sysrestaurant.service.DadosTesteService;
import com.lucas.sysrestaurant.service.LimpezaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@ConditionalOnProperty(name = "app.dev-tools.enabled", havingValue = "true")
public class AdminController {

    private final LimpezaService limpezaService;
    private final DadosTesteService dadosTesteService;

    public AdminController(LimpezaService limpezaService, DadosTesteService dadosTesteService) {
        this.limpezaService = limpezaService;
        this.dadosTesteService = dadosTesteService;
    }

    @PostMapping("/limpar-dados")
    public ContagemTabelasResponse limparDados() {
        return limpezaService.limpar();
    }

    @PostMapping("/dados-teste")
    public ContagemTabelasResponse dadosTeste() {
        return dadosTesteService.inserir();
    }
}
