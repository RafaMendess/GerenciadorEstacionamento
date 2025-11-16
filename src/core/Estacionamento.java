import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Estacionamento {
    private final List<Vaga> vagas;
    private final Queue<Veiculo> filaDeEspera;
    private final Faturamento faturamento;

    public Estacionamento(int capacidadeMaxima) {
        this.vagas = new ArrayList<>();
        for (int i = 1; i <= capacidadeMaxima; i++) {
            vagas.add(new Vaga(i));
        }
        this.filaDeEspera = new LinkedList<>();
        this.faturamento = new Faturamento();
    }

    public boolean registrarEntrada(Veiculo veiculo) {
        // Verificar se já existe veículo com mesma placa
        if (buscarPorPlaca(veiculo.getPlaca()) != null) {
            return false;
        }

        Optional<Vaga> vagaLivre = vagas.stream().filter(Vaga::estaLivre).findFirst();

        if (vagaLivre.isPresent()) {
            vagaLivre.get().ocupar(veiculo);
            return true;
        } else {
            filaDeEspera.offer(veiculo);
            return false;
        }
    }

    public double registrarSaida(String placa) {
        Optional<Vaga> vagaOcupada = vagas.stream()
                .filter(v -> !v.estaLivre() && v.getVeiculo().getPlaca().equalsIgnoreCase(placa))
                .findFirst();

        if (vagaOcupada.isPresent()) {
            Vaga vaga = vagaOcupada.get();
            Veiculo veiculo = vaga.getVeiculo();

            // Calcular valor
            LocalDateTime horaSaida = LocalDateTime.now();
            veiculo.setHoraSaida(horaSaida);
            long horas = veiculo.calcularTempoEmHoras();
            double valor = veiculo.getTipo().calcularValor(horas);

            // Registrar faturamento
            faturamento.registrarSaida(horaSaida, veiculo, valor);

            // Liberar vaga
            vaga.liberar();

            // Atender fila de espera se houver
            atenderFilaDeEspera();

            return valor;
        }
        return -1; // Indica que veículo não foi encontrado
    }

    private void atenderFilaDeEspera() {
        if (!filaDeEspera.isEmpty()) {
            Veiculo proximoVeiculo = filaDeEspera.poll();
            Optional<Vaga> vagaLivre = vagas.stream().filter(Vaga::estaLivre).findFirst();
            vagaLivre.ifPresent(vaga -> vaga.ocupar(proximoVeiculo));
        }
    }

    public List<Vaga> listarVagas() {
        return new ArrayList<>(vagas);
    }

    public Veiculo buscarPorPlaca(String placa) {
        return vagas.stream()
                .filter(v -> !v.estaLivre())
                .map(Vaga::getVeiculo)
                .filter(v -> v.getPlaca().equalsIgnoreCase(placa))
                .findFirst()
                .orElse(null);
    }

    public long getVagasDisponiveis() {
        return vagas.stream().filter(Vaga::estaLivre).count();
    }

    public long getVagasOcupadas() {
        return vagas.stream().filter(v -> !v.estaLivre()).count();
    }

    public int getCapacidadeTotal() {
        return vagas.size();
    }

    public Faturamento getFaturamento() {
        return faturamento;
    }

    public Queue<Veiculo> getFilaDeEspera() {
        return new LinkedList<>(filaDeEspera);
    }

    public int getTamanhoFilaEspera() {
        return filaDeEspera.size();
    }
