package view;

import models.TipoVeiculo;
import models.Vaga;
import controllers.FaturamentoController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Classe responsável por toda a interface de menus do sistema.
 * Centraliza a leitura de entradas do usuário, exibição de opções
 * e coleta de dados usados pelos controladores.
 */
public class Menu {

    /** Scanner usado para leitura das entradas do usuário. */
    private final Scanner scanner = new Scanner(System.in);

    /** Formato padrão para data e hora completa (entrada manual). */
    private final DateTimeFormatter dataHoraFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /** Formato padrão para datas (relatórios). */
    private final DateTimeFormatter dataFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Exibe o menu principal do sistema na tela.
     * Não processa a escolha — apenas mostra as opções.
     */
    public void exibirMenuPrincipal() {
        System.out.println("\n=== SISTEMA DE ESTACIONAMENTO ===");
        System.out.println("1. Registrar entrada de veículo");
        System.out.println("2. Registrar saída de veículo");
        System.out.println("3. Vagas disponíveis");
        System.out.println("4. Listar veículos presentes");
        System.out.println("5. Pesquisar veículo por placa");
        System.out.println("6. Relatório de faturamento");
        System.out.println("7. Fila de espera");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Lê a opção digitada no menu de forma segura.
     *
     * @return número escolhido pelo usuário ou -1 caso não seja um inteiro válido.
     */
    public int lerOpcao() {
        String line = scanner.nextLine();
        try {
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return -1; // Retorna valor inválido sem quebrar o programa
        }
    }

    /**
     * Exibe uma lista de todos os veículos atualmente estacionados.
     *
     * @param vagas lista de vagas do estacionamento
     */
    public void exibirSubmenuListarVeiculos(List<Vaga> vagas) {
        System.out.println("\n=== VEÍCULOS ESTACIONADOS ===");

        boolean encontrou = false;

        // Percorre vagas exibindo apenas as ocupadas
        for (Vaga vaga : vagas) {
            if (vaga.isOcupada()) {
                System.out.println("Vaga " + vaga.getId() + ": " + vaga.getVeiculo());
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum veículo estacionado no momento.");
        }
    }

    /**
     * Exibe o submenu de relatórios de faturamento e processa a escolha do usuário.
     *
     * @param faturamento controlador responsável pelos cálculos de faturamento
     * @param diaAtual data atual usada para relatórios diários
     */
    public void exibirSubmenuFaturamento(FaturamentoController faturamento, LocalDate diaAtual) {
        System.out.println("\n=== RELATÓRIO DE FATURAMENTO ===");
        System.out.println("1. Ver faturamento do dia atual");
        System.out.println("2. Pesquisar faturamento por data");
        System.out.println("3. Ver total geral");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        int opcao = lerOpcao();

        switch (opcao) {
            case 1 -> {
                // Mostra relatório do dia atual
                System.out.println(faturamento.gerarRelatorio(diaAtual));
            }
            case 2 -> pesquisarFaturamentoPorData(faturamento);
            case 3 -> System.out.println("Total geral acumulado: R$ " + faturamento.getTotalGeral());
            case 0 -> {} // Voltar ao menu anterior
            default -> System.out.println("Opção inválida!");
        }
    }

    /**
     * Pergunta ao usuário uma data e mostra o relatório correspondente.
     *
     * @param faturamento controlador responsável pelos relatórios
     */
    private void pesquisarFaturamentoPorData(FaturamentoController faturamento) {
        System.out.print("Digite a data (dd/MM/yyyy): ");
        String texto = scanner.nextLine();

        try {
            LocalDate data = LocalDate.parse(texto, dataFmt);
            System.out.println(faturamento.gerarRelatorio(data));
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida.");
        }
    }

    /**
     * Solicita ao usuário se deseja usar a data/hora atual ou digitar manualmente.
     *
     * @return objeto LocalDateTime correspondente.
     */
    public LocalDateTime solicitarDataManualOuAutomatica() {
        System.out.print("Usar hora atual? (s/n): ");
        String resposta = scanner.nextLine();

        if (resposta.equalsIgnoreCase("s")) {
            return LocalDateTime.now();
        } else {
            System.out.print("Digite data e hora (dd/MM/yyyy HH:mm): ");
            String dataHoraStr = scanner.nextLine();

            try {
                return LocalDateTime.parse(dataHoraStr, dataHoraFmt);
            } catch (DateTimeParseException e) {
                System.out.println("Data/hora inválida! Usando hora atual.");
                return LocalDateTime.now();
            }
        }
    }

    /**
     * Exibe uma mensagem de confirmação para o usuário.
     *
     * @param mensagem texto adicional exibido antes da confirmação
     * @return true se o usuário responder "s", false caso contrário
     */
    public boolean confirmarAcao(String mensagem) {
        if (mensagem != null && !mensagem.isBlank()) {
            System.out.print(mensagem + " (s/n): ");
        } else {
            System.out.print("(s/n): ");
        }

        String resposta = scanner.nextLine().trim();
        return resposta.equalsIgnoreCase("s");
    }

    /**
     * Solicita uma placa ao usuário e faz limpeza básica de caracteres.
     *
     * @return placa formatada em maiúsculo e sem caracteres inválidos
     */
    public String solicitarPlaca() {
        System.out.print("Digite a placa do veículo: ");
        String raw = scanner.nextLine().trim().toUpperCase();
        return raw.replaceAll("[^A-Z0-9]", ""); // Remove caracteres inválidos
    }

    /**
     * Solicita o tipo de veículo (carro ou moto) repetindo até o usuário fornecer uma opção válida.
     *
     * @return tipo de veículo escolhido pelo usuário
     */
    public TipoVeiculo solicitarTipoVeiculo() {
        while (true) {
            System.out.println("Tipo de veículo:");
            System.out.println("1. Carro");
            System.out.println("2. Moto");
            System.out.print("Escolha: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1": return TipoVeiculo.CARRO;
                case "2": return TipoVeiculo.MOTO;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    /**
     * Exibe uma mensagem simples ao usuário.
     *
     * @param mensagem conteúdo a ser exibido
     */
    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    /**
     * Pausa a execução até o usuário pressionar Enter.
     */
    public void pausar() {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    /**
     * Lê um número inteiro do usuário, repetindo enquanto o valor for inválido.
     *
     * @return inteiro fornecido pelo usuário
     */
    public int lerInteiro() {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Número inválido. Tente novamente: ");
            }
        }
    }
}
