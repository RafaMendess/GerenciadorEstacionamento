package services;

import models.Faturamento;
import models.Veiculo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Controla o faturamento diário e total do estacionamento.
 * Utiliza LocalDate como chave para agrupar o faturamento do dia.
 */
public class FaturamentoController {
    private double totalGeral;
    private final Map<LocalDate, Double> faturamentoDia;
    private final Map<LocalDate, List<Faturamento>> faturamentoDetalhado;

    public FaturamentoController() {
        this.totalGeral = 0.0;
        this.faturamentoDia = new HashMap<>();
        this.faturamentoDetalhado = new HashMap<>();
    }

    public Map<LocalDate, Double> getFaturamentoPorDia() {
        return Collections.unmodifiableMap(faturamentoDia);
    }

    public void registrarSaida(LocalDateTime dataSaida, Veiculo veiculo, double valor) {
        LocalDate dia = dataSaida.toLocalDate();
        totalGeral += valor;

        faturamentoDia.merge(dia, valor, Double::sum);

        faturamentoDetalhado
                .computeIfAbsent(dia, k -> new ArrayList<>())
                .add(new Faturamento(
                        veiculo.getPlaca(),
                        valor,
                        dataSaida,
                        veiculo.getTipo()
                ));
    }

    public double getFaturamentoDia(LocalDate dia) {
        return faturamentoDia.getOrDefault(dia, 0.0);
    }

    public List<Faturamento> getFaturamentoDetalhado(LocalDate dia) {
        return faturamentoDetalhado.getOrDefault(dia, Collections.emptyList());
    }

    public double getTotalGeral() {
        return totalGeral;
    }

    public String gerarRelatorio(LocalDate dia) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Relatório de Faturamento - ").append(dia).append(" ===\n");
        sb.append(String.format("Total do dia: R$ %.2f\n\n", getFaturamentoDia(dia)));

        List<Faturamento> registros = getFaturamentoDetalhado(dia);

        if (registros.isEmpty()) {
            sb.append("Nenhum veículo registrado neste dia.\n");
        } else {
            registros.forEach(r -> sb.append(r).append("\n"));
        }
        return sb.toString();
    }
}
