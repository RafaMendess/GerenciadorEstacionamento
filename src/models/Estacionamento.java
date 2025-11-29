package models;

import services.FaturamentoController;

import java.time.LocalDateTime;
import java.util.*;

public class Estacionamento {
    private final List<Vaga> vagas;
    private final Queue<Veiculo> filaDeEspera;
    private final FaturamentoController faturamentoController;
    private final LocalDateTime inicioDia;

    public Estacionamento(int capacidadeMaxima, LocalDateTime inicioDia) {
        if (capacidadeMaxima <= 0) throw new IllegalArgumentException("Capacidade deve ser positiva.");
        this.vagas = new ArrayList<>();
        for (int i = 1; i <= capacidadeMaxima; i++) {
            vagas.add(new Vaga(i));
        }
        this.filaDeEspera = new LinkedList<>();
        this.faturamentoController = new FaturamentoController();
        this.inicioDia = inicioDia;
    }

    public LocalDateTime getInicioDia() {
        return inicioDia;
    }

    /**
     * Registra entrada -- retorna ESTACIONADO se entrou em vaga,
     * FILA se foi para fila de espera, JA_EXISTE se já havia veículo com mesma placa (na vaga ou na fila).
     */
    public EntradaStatus registrarEntrada(Veiculo veiculo) {
        if (veiculo == null || veiculo.getPlaca() == null || veiculo.getPlaca().isBlank()) {
            throw new IllegalArgumentException("Veículo ou placa inválida.");
        }

        // verificar duplicidade tanto nas vagas quanto na fila
        if (buscarPorPlaca(veiculo.getPlaca()) != null) return EntradaStatus.JA_EXISTE;
        boolean naFila = filaDeEspera.stream().anyMatch(v -> v.getPlaca().equalsIgnoreCase(veiculo.getPlaca()));
        if (naFila) return EntradaStatus.JA_EXISTE;

        Optional<Vaga> vagaLivre = vagas.stream().filter(v -> !v.isOcupada()).findFirst();

        if (vagaLivre.isPresent()) {
            vagaLivre.get().ocupar(veiculo);
            return EntradaStatus.ESTACIONADO;
        } else {
            filaDeEspera.offer(veiculo);
            return EntradaStatus.FILA;
        }
    }

    /**
     * Registra saída — procura a vaga ocupada pela placa, calcula valor, registra faturamento,
     * libera a vaga e, se houver fila, chama o próximo veículo automaticamente para essa vaga
     * (com dataEntrada atual).
     * Retorna valor cobrado ou -1 se não encontrou veículo.
     */
    public double registrarSaida(String placa) {
        Optional<Vaga> vagaOcupada = vagas.stream()
                .filter(v -> v.isOcupada() && v.getVeiculo().getPlaca().equalsIgnoreCase(placa))
                .findFirst();

        if (vagaOcupada.isPresent()) {
            Vaga vaga = vagaOcupada.get();
            Veiculo veiculo = vaga.getVeiculo();

            LocalDateTime horaSaida = LocalDateTime.now();
            veiculo.setDataSaida(horaSaida);
            double valor = veiculo.calcularValorTotal();

            faturamentoController.registrarSaida(horaSaida, veiculo, valor);

            vaga.liberar();

            // Atende fila: puxa próximo veículo para a vaga recém-liberada e atualiza dataEntrada para agora
            if (!filaDeEspera.isEmpty()) {
                Veiculo proximo = filaDeEspera.poll();
                proximo.setDataEntrada(LocalDateTime.now());
                vaga.ocupar(proximo);
            }

            return valor;
        }
        return -1;
    }

    public List<Vaga> listarVagas() {
        return Collections.unmodifiableList(vagas);
    }

    public Veiculo buscarPorPlaca(String placa) {
        return vagas.stream()
                .filter(Vaga::isOcupada)
                .map(Vaga::getVeiculo)
                .filter(v -> v.getPlaca().equalsIgnoreCase(placa))
                .findFirst()
                .orElse(null);
    }

    public long getVagasDisponiveis() {
        return vagas.stream().filter(v -> !v.isOcupada()).count();
    }

    public long getVagasOcupadas() {
        return vagas.stream().filter(Vaga::isOcupada).count();
    }

    public int getCapacidadeTotal() {
        return vagas.size();
    }

    public FaturamentoController getFaturamento() {
        return faturamentoController;
    }

    /** Retorna cópia imutável da fila de espera (ordem preservada). */
    public List<Veiculo> getFilaDeEspera() {
        return Collections.unmodifiableList(new ArrayList<>(filaDeEspera));
    }

    public int getTamanhoFilaEspera() {
        return filaDeEspera.size();
    }
}
