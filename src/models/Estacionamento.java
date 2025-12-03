package models;

import controllers.FaturamentoController;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Representa um estacionamento que controla vagas, fila de espera e faturamento.
 * Permite registrar entrada e saída de veículos, além de emitir relatórios via
 * {@link FaturamentoController}.
 *
 * <p>Principais responsabilidades:</p>
 * <ul>
 *     <li>Gerenciar ocupação das vagas</li>
 *     <li>Manter fila de espera quando não há vagas disponíveis</li>
 *     <li>Registrar faturamento ao liberar uma vaga</li>
 *     <li>Controlar horário de início do dia</li>
 * </ul>
 */
public class Estacionamento {

    /** Lista de vagas do estacionamento. */
    private final List<Vaga> vagas;

    /** Fila de espera para quando não houver vagas disponíveis. */
    private final Queue<Veiculo> filaDeEspera;

    /** Controlador responsável por registrar faturamento. */
    private final FaturamentoController faturamentoController;

    /** Registro do início do dia para relatórios. */
    private final LocalDateTime inicioDia;

    /**
     * Construtor do estacionamento.
     *
     * @param capacidadeMaxima número total de vagas
     * @param inicioDia horário de início do dia
     * @param faturamentoController controlador para registro de faturamento
     * @throws IllegalArgumentException se capacidade for inválida
     */
    public Estacionamento(int capacidadeMaxima, LocalDateTime inicioDia, FaturamentoController faturamentoController) {
        if (capacidadeMaxima <= 0) throw new IllegalArgumentException("Capacidade deve ser positiva.");

        this.vagas = new ArrayList<>();
        for (int i = 1; i <= capacidadeMaxima; i++) {
            vagas.add(new Vaga(i));
        }

        this.filaDeEspera = new LinkedList<>();
        this.faturamentoController = faturamentoController;
        this.inicioDia = inicioDia;
    }

    /**
     * @return horário de início do dia.
     */
    public LocalDateTime getInicioDia() {
        return inicioDia;
    }

    /**
     * Registra a entrada de um veículo no estacionamento.
     *
     * <p>Fluxo:</p>
     * <ul>
     *     <li>Valida a placa</li>
     *     <li>Verifica se já existe veículo igual (vaga ou fila)</li>
     *     <li>Se houver vaga, estaciona</li>
     *     <li>Se não houver vaga, entra na fila</li>
     * </ul>
     *
     * @param veiculo veículo a ser registrado
     * @return status da tentativa de entrada
     * @throws IllegalArgumentException se o veículo ou placa forem inválidos
     */
    public EntradaStatus registrarEntrada(Veiculo veiculo) {
        if (veiculo == null || veiculo.getPlaca() == null || veiculo.getPlaca().isBlank()) {
            throw new IllegalArgumentException("Veículo ou placa inválida.");
        }

        // Evita duplicidade
        if (buscarPorPlaca(veiculo.getPlaca()) != null) return EntradaStatus.JA_EXISTE;
        boolean naFila = filaDeEspera.stream().anyMatch(v -> v.getPlaca().equalsIgnoreCase(veiculo.getPlaca()));
        if (naFila) return EntradaStatus.JA_EXISTE;

        // Procura vaga livre
        Optional<Vaga> vagaLivre = vagas.stream()
                .filter(v -> !v.isOcupada())
                .findFirst();

        if (vagaLivre.isPresent()) {
            vagaLivre.get().ocupar(veiculo);
            return EntradaStatus.ESTACIONADO;
        }

        // Sem vagas → vai para fila
        filaDeEspera.offer(veiculo);
        return EntradaStatus.FILA;
    }

    /**
     * Registra a saída de um veículo do estacionamento.
     *
     * <p>Fluxo:</p>
     * <ul>
     *     <li>Localiza o veículo pela placa</li>
     *     <li>Calcula o valor total</li>
     *     <li>Registra faturamento</li>
     *     <li>Libera a vaga</li>
     *     <li>Se houver fila, chama o próximo para a vaga</li>
     * </ul>
     *
     * @param placa placa do veículo a remover
     * @return valor cobrado, ou -1 se o veículo não foi encontrado
     */
    public double registrarSaida(String placa) {
        Optional<Vaga> vagaOcupada = vagas.stream()
                .filter(v -> v.isOcupada() && v.getVeiculo().getPlaca().equalsIgnoreCase(placa))
                .findFirst();

        if (vagaOcupada.isPresent()) {
            Vaga vaga = vagaOcupada.get();
            Veiculo veiculo = vaga.getVeiculo();

            // Garante que exista um horário de saída
            LocalDateTime horaSaida = veiculo.getDataSaida();
            if (horaSaida == null) {
                horaSaida = LocalDateTime.now();
                veiculo.setDataSaida(horaSaida);
            }

            double valor = veiculo.calcularValorTotal();

            // Registra faturamento
            faturamentoController.registrarSaida(horaSaida, veiculo, valor);

            // Libera a vaga
            vaga.liberar();

            // Chama próximo da fila automaticamente
            if (!filaDeEspera.isEmpty()) {
                Veiculo proximo = filaDeEspera.poll();
                proximo.setDataEntrada(LocalDateTime.now());
                vaga.ocupar(proximo);
            }

            return valor;
        }
        return -1;
    }

    /**
     * Lista todas as vagas de forma imutável.
     *
     * @return lista de vagas
     */
    public List<Vaga> listarVagas() {
        return Collections.unmodifiableList(vagas);
    }

    /**
     * Busca um veículo pela placa nas vagas ocupadas.
     *
     * @param placa placa do veículo
     * @return veículo encontrado ou null
     */
    public Veiculo buscarPorPlaca(String placa) {
        return vagas.stream()
                .filter(Vaga::isOcupada)
                .map(Vaga::getVeiculo)
                .filter(v -> v.getPlaca().equalsIgnoreCase(placa))
                .findFirst()
                .orElse(null);
    }

    /**
     * @return quantidade de vagas disponíveis.
     */
    public long getVagasDisponiveis() {
        return vagas.stream().filter(v -> !v.isOcupada()).count();
    }

    /**
     * @return quantidade de vagas ocupadas.
     */
    public long getVagasOcupadas() {
        return vagas.stream().filter(Vaga::isOcupada).count();
    }

    /**
     * @return total de vagas do estacionamento.
     */
    public int getCapacidadeTotal() {
        return vagas.size();
    }

    /**
     * @return controlador de faturamento.
     */
    public FaturamentoController getFaturamento() {
        return faturamentoController;
    }

    /**
     * Retorna uma lista imutável contendo a fila de espera.
     *
     * @return lista da fila de espera
     */
    public List<Veiculo> getFilaDeEspera() {
        return Collections.unmodifiableList(new ArrayList<>(filaDeEspera));
    }

    /**
     * @return quantidade de veículos aguardando na fila.
     */
    public int getTamanhoFilaEspera() {
        return filaDeEspera.size();
    }
}
