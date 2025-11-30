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

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dataHoraFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DateTimeFormatter dataFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

    /** Lê opção do menu de forma segura (retorna -1 se inválido) */
    public int lerOpcao() {
        String line = scanner.nextLine();
        try {
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void exibirSubmenuListarVeiculos(List<Vaga> vagas) {
        System.out.println("\n=== VEÍCULOS ESTACIONADOS ===");
        boolean encontrou = false;
        for (Vaga vaga : vagas) {
            if (vaga.isOcupada()) {
                System.out.println("Vaga " + vaga.getId() + ": " + vaga.getVeiculo());
                encontrou = true;
            }
        }
        if (!encontrou) System.out.println("Nenhum veículo estacionado no momento.");
    }
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
                // usa o dia atual do turno (passado pelo EstacionamentoController)
                System.out.println(faturamento.gerarRelatorio(diaAtual));
            }
            case 2 -> pesquisarFaturamentoPorData(faturamento);
            case 3 -> System.out.println("Total geral acumulado: R$ " + faturamento.getTotalGeral());
            case 0 -> {}
            default -> System.out.println("Opção inválida!");
        }
    }

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

    public boolean confirmarAcao(String mensagem) {
        if (mensagem != null && !mensagem.isBlank()) System.out.print(mensagem + " (s/n): ");
        else System.out.print("(s/n): ");
        String resposta = scanner.nextLine();
        return resposta.equalsIgnoreCase("s");
    }

    public String solicitarPlaca() {
        System.out.print("Digite a placa do veículo: ");
        String raw = scanner.nextLine().toUpperCase().replaceAll("[^A-Z0-9]", "");
        return raw;
    }

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
                default: System.out.println("Opção inválida!");
            }
        }
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void pausar() {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

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
