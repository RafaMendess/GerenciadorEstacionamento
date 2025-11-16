import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner;

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

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

    public void exibirSubmenuListarVeiculos(Estacionamento estacionamento) {
        System.out.println("\n=== VEÍCULOS ESTACIONADOS ===");
        List<Vaga> vagas = estacionamento.listarVagas();
        boolean encontrouVeiculos = false;

        for (Vaga vaga : vagas) {
            if (!vaga.estaLivre()) {
                System.out.println("Vaga " + vaga.getId() + ": " + vaga.getVeiculo());
                encontrouVeiculos = true;
            }
        }

        if (!encontrouVeiculos) {
            System.out.println("Nenhum veículo estacionado no momento.");
        }

        System.out.println("Total: " + estacionamento.getVagasOcupadas() + " veículo(s)");
    }

    public void exibirSubmenuFaturamento(Faturamento faturamento) {
        System.out.println("\n=== RELATÓRIO DE FATURAMENTO ===");
        System.out.printf("Total geral arrecadado: R$ %.2f%n", faturamento.getTotalGeral());

        System.out.println("\nFaturamento por dia:");
        faturamento.getFaturamentoPorDia().forEach((data, valor) -> {
            System.out.printf("  %s: R$ %.2f%n", data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), valor);
        });

        System.out.print("\nDeseja ver detalhes de um dia específico? (s/n): ");
        String resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("s")) {
            System.out.print("Digite a data (dd/MM/yyyy): ");
            String dataStr = scanner.nextLine();
            try {
                LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                List<RegistroFaturamento> registros = faturamento.getFaturamentoDetalhado(data);
                if (registros.isEmpty()) {
                    System.out.println("Nenhum registro para esta data.");
                } else {
                    System.out.println("\nDetalhes do dia " + dataStr + ":");
                    registros.forEach(System.out::println);
                }
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida!");
            }
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
                return LocalDateTime.parse(dataHoraStr,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            } catch (DateTimeParseException e) {
                System.out.println("Data/hora inválida! Usando hora atual.");
                return LocalDateTime.now();
            }
        }
    }

    public boolean confirmarAcao(String mensagem) {
        System.out.print(mensagem + " (s/n): ");
        String resposta = scanner.nextLine();
        return resposta.equalsIgnoreCase("s");
    }

    public String solicitarPlaca() {
        System.out.print("Digite a placa do veículo: ");
        return scanner.nextLine().toUpperCase().replaceAll("[^A-Z0-9]", "");
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
}