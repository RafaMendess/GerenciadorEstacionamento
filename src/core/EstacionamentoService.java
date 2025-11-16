import java.time.LocalDateTime;

public class EstacionamentoService {
    private final Estacionamento estacionamento;
    private final Menu menu;

    public EstacionamentoService(Estacionamento estacionamento, Menu menu) {
        this.estacionamento = estacionamento;
        this.menu = menu;
    }

    public void iniciarServico() {
        int opcao;
        do {
            menu.exibirMenuPrincipal();
            try {
                opcao = Integer.parseInt(menu.scanner.nextLine());
                processarOpcao(opcao);
            } catch (NumberFormatException e) {
                menu.mostrarMensagem("Opção inválida! Digite um número.");
                opcao = -1;
            }
        } while (opcao != 0);

        menu.mostrarMensagem("Sistema encerrado. Obrigado!");
    }

    public void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> registrarEntrada();
            case 2 -> registrarSaida();
            case 3 -> mostrarVagasDisponiveis();
            case 4 -> menu.exibirSubmenuListarVeiculos(estacionamento);
            case 5 -> pesquisarVeiculo();
            case 6 -> menu.exibirSubmenuFaturamento(estacionamento.getFaturamento());
            case 7 -> mostrarFilaEspera();
            case 0 -> menu.mostrarMensagem("Encerrando sistema...");
            default -> menu.mostrarMensagem("Opção inválida!");
        }
        if (opcao != 0) {
            menu.pausar();
        }
    }

    private void registrarEntrada() {
        String placa = menu.solicitarPlaca();
        TipoVeiculo tipo = menu.solicitarTipoVeiculo();
        LocalDateTime horaEntrada = menu.solicitarDataManualOuAutomatica();

        Veiculo veiculo = new Veiculo(placa, tipo, horaEntrada);
        boolean sucesso = estacionamento.registrarEntrada(veiculo);

        if (sucesso) {
            menu.mostrarMensagem("Veículo estacionado com sucesso na vaga!");
        } else {
            if (estacionamento.getTamanhoFilaEspera() > 0) {
                menu.mostrarMensagem("Estacionamento cheio! Veículo adicionado na fila de espera. Posição: " + estacionamento.getTamanhoFilaEspera());
            } else {
                menu.mostrarMensagem("Erro: Veículo já está estacionado ou placa duplicada!");
            }
        }
    }

    private void registrarSaida() {
        String placa = menu.solicitarPlaca();
        double valor = estacionamento.registrarSaida(placa);

        if (valor >= 0) {
            menu.mostrarMensagem(String.format("Saída registrada! Valor a pagar: R$ %.2f", valor));
        } else {
            menu.mostrarMensagem("Veículo não encontrado no estacionamento!");
        }
    }

    private void mostrarVagasDisponiveis() {
        System.out.println("\n=== VAGAS DISPONÍVEIS ===");
        System.out.println("Vagas livres: " + estacionamento.getVagasDisponiveis());
        System.out.println("Vagas ocupadas: " + estacionamento.getVagasOcupadas());
        System.out.println("Capacidade total: " + estacionamento.getCapacidadeTotal());

        if (estacionamento.getTamanhoFilaEspera() > 0) {
            System.out.println("Veículos na fila de espera: " + estacionamento.getTamanhoFilaEspera());
        }
    }

    private void pesquisarVeiculo() {
        String placa = menu.solicitarPlaca();
        Veiculo veiculo = estacionamento.buscarPorPlaca(placa);

        if (veiculo != null) {
            System.out.println("\nVeículo encontrado:");
            System.out.println(veiculo);
        } else {
            menu.mostrarMensagem("Veículo não encontrado no estacionamento!");
        }
    }

    private void mostrarFilaEspera() {
        System.out.println("\n=== FILA DE ESPERA ===");
        var fila = estacionamento.getFilaDeEspera();
        if (fila.isEmpty()) {
            System.out.println("Nenhum veículo na fila de espera.");
        } else {
            int posicao = 1;
            for (Veiculo veiculo : fila) {
                System.out.println(posicao + ". " + veiculo);
                posicao++;
            }
        }
    }
}