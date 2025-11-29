package services;

import models.*;
import view.Menu;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EstacionamentoController {
    private final Estacionamento estacionamento;
    private final Menu menu;

    public EstacionamentoController(Estacionamento estacionamento, Menu menu) {
        this.estacionamento = estacionamento;
        this.menu = menu;
    }

    public void iniciarServico() {
        int opcao;
        do {
            menu.exibirMenuPrincipal();
            opcao = menu.lerOpcao();
            processarOpcao(opcao);
        } while (opcao != 0);

        // Ao encerrar, mostra faturamento do dia de início (referência)
        menu.mostrarMensagem("Sistema encerrado. Obrigado!");
        var inicio = estacionamento.getInicioDia();
        if (inicio != null) {
            String rel = estacionamento.getFaturamento().gerarRelatorio(inicio.toLocalDate());
            System.out.println("\nRelatório do dia de trabalho (início: " + inicio.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "):");
            System.out.println(rel);
        }
    }

    public void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> registrarEntrada();
            case 2 -> registrarSaida();
            case 3 -> mostrarVagasDisponiveis();
            case 4 -> menu.exibirSubmenuListarVeiculos(estacionamento.listarVagas());
            case 5 -> pesquisarVeiculo();
            case 6 -> menu.exibirSubmenuFaturamento(estacionamento.getFaturamento());
            case 7 -> mostrarFilaEspera();
            case 0 -> menu.mostrarMensagem("Encerrando sistema...");
            default -> menu.mostrarMensagem("Opção inválida!");
        }
        if (opcao != 0) menu.pausar();
    }

    private void registrarEntrada() {
        String placa = menu.solicitarPlaca();
        TipoVeiculo tipo = menu.solicitarTipoVeiculo();
        LocalDateTime horaEntrada = menu.solicitarDataManualOuAutomatica();

        Veiculo veiculo = new Veiculo(placa, tipo, horaEntrada);
        EntradaStatus status = estacionamento.registrarEntrada(veiculo);

        switch (status) {
            case ESTACIONADO -> menu.mostrarMensagem("Veículo estacionado com sucesso na vaga!");
            case FILA -> menu.mostrarMensagem("Estacionamento cheio! Veículo adicionado na fila de espera. Posição: " + estacionamento.getTamanhoFilaEspera());
            case JA_EXISTE -> menu.mostrarMensagem("Já existe veículo com essa placa no estacionamento ou na fila!");
        }
    }

    private void registrarSaida() {
        String placa = menu.solicitarPlaca();
        Veiculo veiculo = estacionamento.buscarPorPlaca(placa);

        if (veiculo != null) {
            veiculo.setDataSaida(LocalDateTime.now());
            double valor = veiculo.calcularValorTotal();
            boolean confirmar = menu.confirmarAcao("Valor a pagar: R$ " + String.format("%.2f", valor) + ". Confirmar saída?");
            if (confirmar) {
                double recebido = estacionamento.registrarSaida(placa);
                if (recebido >= 0) {
                    menu.mostrarMensagem("Saída registrada com sucesso! Valor cobrado: R$ " + String.format("%.2f", recebido));
                } else {
                    menu.mostrarMensagem("Erro ao registrar saída.");
                }
            } else {
                menu.mostrarMensagem("Saída cancelada pelo usuário.");
            }
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
