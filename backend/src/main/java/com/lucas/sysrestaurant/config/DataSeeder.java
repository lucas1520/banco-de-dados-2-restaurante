package com.lucas.sysrestaurant.config;

import com.lucas.sysrestaurant.repository.MesaRepository;
import com.lucas.sysrestaurant.service.DadosTesteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MesaRepository mesas;
    private final DadosTesteService dadosTeste;

    public DataSeeder(MesaRepository mesas, DadosTesteService dadosTeste) {
        this.mesas = mesas;
        this.dadosTeste = dadosTeste;
    }

    @Override
    public void run(String... args) {
        if (mesas.count() > 0) {
            return;
        }
        dadosTeste.inserir();
    }
}
