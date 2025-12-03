package controllers;

import models.*;
import view.Menu;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controla o fluxo principal do sistema de estacionamento.
 *
 * <p>Este controller é responsável por interpretar as ações do usuário,
 * utilizando o {@link Menu} para entrada e saída de informações, e delegando
 * operações para o modelo {@link Estacionamento}.</p>
 *
 * <p>Coordena processos como registrar entrada, registrar saída, visualizar vagas,
 * consultar fila de espera e exibir relatórios de faturamento.</p>
 */
public class EstacionamentoController {

    /** Modelo principal que contém vagas, fila e faturamento. */
    private final Estacionamento estacionamento;

    /** Camada de interface com o usuário (menus e entradas). */
    private final Menu menu;

    /**
     * Construtor padrão.
     *
     * @param estacionamento Instância principal do modelo de estacionamento.
     * @param menu Interface de entrada e saída para interação com o usuário.
     */
    public EstacionamentoController(Estacionamento estacionamento, Menu menu) {
        this.estacionamento = estacionamento;
        this.menu = menu;
    }

    /**
     * Inicia o ciclo principal do sistema, exibindo o menu e processando opções.
     * Ao finalizar, exibe o relatório de faturamento do dia.
     */
    public void iniciarServico() {
        int opcao;
        do {
            menu.exibirMenuPrincipal();
            opcao = menu.lerOpcao();
            processarOpcao(opcao);
        } while (opcao != 0);

        menu.mostrarMensagem("Sistema encerrado. Obrigado!");

        // Exibe relatório do dia referente ao horário de início
        var inicio = estacionamento.getInicioDia();
        if (inicio != null) {
            String rel = estacionamento.getFaturamento().gerarRelatorio(inicio.toLocalDate());
            System.out.println("\nRelatório do dia de trabalho (início: " +
                    inicio.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "):");
            System.out.println(rel);
        }
    }

    /**
     * Processa a escolha do usuário no menu principal.
     *
     * @param opcao Número da opção selecionada.
     */
    public void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> registrarEntrada();
            case 2 -> registrarSaida();
            case 3 -> mostrarVagasDisponiveis();
            case 4 -> menu.exibirSubmenuListarVeiculos(estacionamento.listarVagas());
            case 5 -> pesquisarVeiculo();
            case 6 -> menu.exibirSubmenuFaturamento(
                    estacionamento.getFaturamento(),
                    estacionamento.getInicioDia().toLocalDate()
            );
            case 7 -> mostrarFilaEspera();
            case 0 -> menu.mostrarMensagem("Encerrando sistema...");
            default -> menu.mostrarMensagem("Opção inválida!");
        }

        if (opcao != 0) menu.pausar();
    }

    /**
     * Realiza o fluxo de registro de entrada de um veículo:
     * solicita placa, tipo e horário, e registra no estacionamento.
     */
    private void registrarEntrada() {
        String placa = menu.solicitarPlaca();
        TipoVeiculo tipo = menu.solicitarTipoVeiculo();
        LocalDateTime horaEntrada = menu.solicitarDataManualOuAutomatica();

        Veiculo veiculo = new Veiculo(placa, tipo, horaEntrada);
        EntradaStatus status = estacionamento.registrarEntrada(veiculo);

        switch (status) {
            case ESTACIONADO ->
                    menu.mostrarMensagem("Veículo estacionado com sucesso na vaga!");
            case FILA ->
                    menu.mostrarMensagem("Estacionamento cheio! Veículo adicionado na fila de espera. Posição: "
                            + estacionamento.getTamanhoFilaEspera());
            case JA_EXISTE ->
                    menu.mostrarMensagem("Já existe veículo com essa placa no estacionamento ou na fila!");
        }
    }

    /**
     * Realiza o fluxo de saída: localiza o veículo, aplica cálculo de valor,
     * solicita confirmação e finaliza a operação.
     */
    private void registrarSaida() {
        String placa = menu.solicitarPlaca();
        Veiculo veiculo = estacionamento.buscarPorPlaca(placa);

        if (veiculo != null) {
            // Define data de saída (manual ou automática)
            veiculo.setDataSaida(menu.solicitarDataManualOuAutomatica());

            double valor = veiculo.calcularValorTotal();

            boolean confirmar = menu.confirmarAcao(
                    "Valor a pagar: R$ " + String.format("%.2f", valor) + ". Confirmar saída?"
            );

            if (confirmar) {
                double recebido = estacionamento.registrarSaida(placa);

                if (recebido >= 0) {
                    menu.mostrarMensagem("Saída registrada com sucesso! Valor cobrado: R$ "
                            + String.format("%.2f", recebido));
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

    /**
     * Exibe a quantidade de vagas livres/ocupadas e informações da fila.
     */
    private void mostrarVagasDisponiveis() {
        System.out.println("\n=== VAGAS DISPONÍVEIS ===");
        System.out.println("Vagas livres: " + estacionamento.getVagasDisponiveis());
        System.out.println("Vagas ocupadas: " + estacionamento.getVagasOcupadas());
        System.out.println("Capacidade total: " + estacionamento.getCapacidadeTotal());

        if (estacionamento.getTamanhoFilaEspera() > 0) {
            System.out.println("Veículos na fila de espera: " + estacionamento.getTamanhoFilaEspera());
        }
    }

    /**
     * Permite pesquisar um veículo pela placa e exibe suas informações.
     */
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

    /**
     * Exibe todos os veículos atualmente na fila de espera.
     */
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
