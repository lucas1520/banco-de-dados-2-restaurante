package com.lucas.sysrestaurant.service;

import java.util.List;

final class DadosTesteCatalogo {

    record IngredienteModelo(String nome, String preco, int estoque) {
    }

    record ItemReceita(String ingrediente, String quantidade) {
    }

    record PratoModelo(String nome, String valor, int tempoPrep, boolean ativo, ItemReceita... receita) {
    }

    record FuncionarioModelo(String nome, String cargo, boolean ativo) {
    }

    private DadosTesteCatalogo() {
    }

    private static ItemReceita i(String ingrediente, String quantidade) {
        return new ItemReceita(ingrediente, quantidade);
    }

    static final int[] CADEIRAS_DAS_MESAS = {2, 2, 2, 4, 4, 4, 4, 6, 6, 6, 8, 10};

    static final List<FuncionarioModelo> FUNCIONARIOS = List.of(
            new FuncionarioModelo("Marcos Silva", "Garçom", true),
            new FuncionarioModelo("Paula Rocha", "Cozinheira", true),
            new FuncionarioModelo("Rita Alves", "Gerente", true),
            new FuncionarioModelo("Tiago Nunes", "Garçom", false),
            new FuncionarioModelo("Fernanda Costa", "Garçonete", true),
            new FuncionarioModelo("Lucas Pereira", "Garçom", true),
            new FuncionarioModelo("Juliana Mendes", "Caixa", true),
            new FuncionarioModelo("Carlos Eduardo Ramos", "Cozinheiro", true),
            new FuncionarioModelo("Beatriz Faria", "Garçonete", true),
            new FuncionarioModelo("Rafael Teixeira", "Maître", true),
            new FuncionarioModelo("Camila Duarte", "Auxiliar de cozinha", false),
            new FuncionarioModelo("Gustavo Barros", "Garçom", false));

    static final List<IngredienteModelo> INGREDIENTES = List.of(
            new IngredienteModelo("Macarrão", "6.50", 80),
            new IngredienteModelo("Carne moída", "32.00", 45),
            new IngredienteModelo("Alface", "3.20", 40),
            new IngredienteModelo("Arroz arbóreo", "18.00", 25),
            new IngredienteModelo("Cogumelos", "25.00", 2),
            new IngredienteModelo("Peito de frango", "19.90", 60),
            new IngredienteModelo("Filé mignon", "69.90", 4),
            new IngredienteModelo("Salmão", "74.90", 2),
            new IngredienteModelo("Camarão", "89.90", 0),
            new IngredienteModelo("Queijo parmesão", "58.00", 14),
            new IngredienteModelo("Queijo mussarela", "36.00", 70),
            new IngredienteModelo("Queijo gorgonzola", "62.00", 3),
            new IngredienteModelo("Molho de tomate", "9.50", 90),
            new IngredienteModelo("Tomate", "7.00", 50),
            new IngredienteModelo("Cebola", "4.50", 100),
            new IngredienteModelo("Alho", "28.00", 20),
            new IngredienteModelo("Batata", "5.50", 120),
            new IngredienteModelo("Arroz branco", "6.00", 150),
            new IngredienteModelo("Feijão", "8.50", 130),
            new IngredienteModelo("Farinha de trigo", "4.20", 90),
            new IngredienteModelo("Ovos", "0.90", 200),
            new IngredienteModelo("Creme de leite", "5.80", 35),
            new IngredienteModelo("Chocolate", "38.00", 12),
            new IngredienteModelo("Leite condensado", "7.50", 0),
            new IngredienteModelo("Azeite", "45.00", 9));

    static final List<PratoModelo> PRATOS = List.of(
            new PratoModelo("Spaghetti à Bolonhesa", "42.90", 25, true,
                    i("Macarrão", "0.20"), i("Carne moída", "0.15"), i("Molho de tomate", "0.20"), i("Cebola", "0.05")),
            new PratoModelo("Salada Caesar", "28.50", 10, true,
                    i("Alface", "0.30"), i("Peito de frango", "0.15"), i("Queijo parmesão", "0.03"), i("Ovos", "1.00")),
            new PratoModelo("Risoto de Cogumelos", "54.00", 35, true,
                    i("Arroz arbóreo", "0.18"), i("Cogumelos", "0.12"), i("Queijo parmesão", "0.04"),
                    i("Cebola", "0.05"), i("Azeite", "0.02")),
            new PratoModelo("Lasanha Quatro Queijos", "47.00", 40, false,
                    i("Farinha de trigo", "0.20"), i("Queijo mussarela", "0.15"), i("Queijo gorgonzola", "0.05"),
                    i("Queijo parmesão", "0.05"), i("Molho de tomate", "0.20")),
            new PratoModelo("Filé à Parmegiana", "62.00", 30, true,
                    i("Filé mignon", "0.25"), i("Queijo mussarela", "0.10"), i("Molho de tomate", "0.15"),
                    i("Ovos", "1.00"), i("Batata", "0.20")),
            new PratoModelo("Salmão Grelhado", "78.00", 28, true,
                    i("Salmão", "0.30"), i("Batata", "0.20"), i("Azeite", "0.03"), i("Alho", "0.02")),
            new PratoModelo("Moqueca de Camarão", "84.00", 45, true,
                    i("Camarão", "0.30"), i("Tomate", "0.15"), i("Cebola", "0.10"), i("Arroz branco", "0.15"),
                    i("Azeite", "0.04")),
            new PratoModelo("Frango Grelhado com Batatas", "38.00", 22, true,
                    i("Peito de frango", "0.30"), i("Batata", "0.25"), i("Azeite", "0.02"), i("Alho", "0.01")),
            new PratoModelo("Feijoada Completa", "58.00", 50, true,
                    i("Feijão", "0.30"), i("Carne moída", "0.20"), i("Arroz branco", "0.15"), i("Cebola", "0.05")),
            new PratoModelo("Pizza Margherita", "44.00", 20, true,
                    i("Farinha de trigo", "0.25"), i("Queijo mussarela", "0.20"), i("Molho de tomate", "0.15"),
                    i("Tomate", "0.10")),
            new PratoModelo("Pizza Quatro Queijos", "52.00", 22, true,
                    i("Farinha de trigo", "0.25"), i("Queijo mussarela", "0.15"), i("Queijo gorgonzola", "0.05"),
                    i("Queijo parmesão", "0.05")),
            new PratoModelo("Strogonoff de Frango", "39.90", 25, true,
                    i("Peito de frango", "0.25"), i("Creme de leite", "0.20"), i("Cebola", "0.05"),
                    i("Arroz branco", "0.15"), i("Tomate", "0.10")),
            new PratoModelo("Omelete de Queijo", "24.00", 10, true,
                    i("Ovos", "3.00"), i("Queijo mussarela", "0.08")),
            new PratoModelo("Batata Frita", "22.00", 15, true,
                    i("Batata", "0.40"), i("Azeite", "0.02")),
            new PratoModelo("Bife Acebolado", "49.00", 25, true,
                    i("Filé mignon", "0.22"), i("Cebola", "0.15"), i("Arroz branco", "0.15"), i("Feijão", "0.10")),
            new PratoModelo("Brigadeiro Gourmet", "14.00", 8, true,
                    i("Chocolate", "0.05"), i("Leite condensado", "0.10"), i("Creme de leite", "0.02")),
            new PratoModelo("Pudim de Leite", "16.50", 5, true,
                    i("Leite condensado", "0.20"), i("Ovos", "2.00"), i("Creme de leite", "0.10")),
            new PratoModelo("Mousse de Chocolate", "18.00", 10, false,
                    i("Chocolate", "0.10"), i("Creme de leite", "0.15"), i("Ovos", "2.00")),
            new PratoModelo("Panqueca de Frango", "33.00", 20, true,
                    i("Farinha de trigo", "0.15"), i("Ovos", "1.00"), i("Peito de frango", "0.20"),
                    i("Molho de tomate", "0.15"), i("Queijo mussarela", "0.08")),
            new PratoModelo("Couvert da Casa", "12.00", 5, true));

    static final List<String> NOMES = List.of(
            "Ana", "Bruno", "Carla", "Diego", "Eduarda", "Felipe", "Gabriela", "Henrique", "Isabela", "João",
            "Karina", "Leonardo", "Mariana", "Nicolas", "Olívia", "Pedro", "Quésia", "Rodrigo", "Sabrina", "Thiago",
            "Úrsula", "Vinícius", "Wesley", "Ximena", "Yasmin", "Zeca", "Alice", "Bernardo", "Cecília", "Davi",
            "Elisa", "Fábio", "Giovana", "Heitor", "Ingrid", "Jéssica", "Kauê", "Larissa", "Matheus", "Natália");

    static final List<String> SOBRENOMES = List.of(
            "Souza", "Lima", "Dias", "Martins", "Oliveira", "Santos", "Pereira", "Costa", "Rodrigues", "Almeida",
            "Nascimento", "Carvalho", "Araújo", "Ribeiro", "Gomes", "Barbosa", "Moreira", "Cardoso", "Ferreira",
            "Machado", "Rocha", "Correia", "Teixeira", "Moraes", "Freitas", "Campos", "Nunes", "Pinto", "Monteiro",
            "Vieira", "Cunha", "Azevedo", "Mendes", "Ramos", "Farias", "Batista", "Duarte", "Castro", "Lopes",
            "Fernandes");
}
