package services;

import models.RegistroFaturamento;
import models.Veiculo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Controla o faturamento diário e total do estacionamento.
 * Agora utilizando LocalDate como chave para agrupar o faturamento do dia.
 */
public class Faturamento {
    private double totalGeral;
    private final Map<LocalDate, Double> faturamentoDia;
    private final Map<LocalDate, List<RegistroFaturamento>> faturamentoDetalhado;

    public Faturamento() {
        this.totalGeral = 0.0;
        this.faturamentoDia = new HashMap<>();
        this.faturamentoDetalhado = new HashMap<>();
    }

    /**
     * Registra a saída de um veículo.
     * Agrupa os valores pelo dia da saída .
     */
    public void registrarSaida(LocalDateTime dataSaida, Veiculo veiculo, double valor) {
        LocalDate dia = dataSaida.toLocalDate();

        totalGeral += valor;

        faturamentoDia.merge(dia, valor, Double::sum);

        faturamentoDetalhado
                .computeIfAbsent(dia, k -> new ArrayList<>())
                .add(new RegistroFaturamento(
                        veiculo.getPlaca(),
                        valor,
                        dataSaida,
                        veiculo.getTipo()
                ));
    }

    /**
     * Retorna o faturamento total de um dia específico.
     */
    public double getFaturamentoDia(LocalDate dia) {
        return faturamentoDia.getOrDefault(dia, 0.0);
    }

    /**
     * Retorna os registros detalhados de um dia específico.
     */
    public List<RegistroFaturamento> getFaturamentoDetalhado(LocalDate dia) {
        return faturamentoDetalhado.getOrDefault(dia, Collections.emptyList());
    }

    /**
     * Retorna o valor total arrecadado desde o início.
     */
    public double getTotalGeral() {
        return totalGeral;
    }

    /**
     * Gera relatório do dia com lista de veículos e valores.
     */
    public String gerarRelatorio(LocalDate dia) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Relatório de Faturamento - ").append(dia).append(" ===\n");
        sb.append(String.format("Total do dia: R$ %.2f\n\n", getFaturamentoDia(dia)));

        List<RegistroFaturamento> registros = getFaturamentoDetalhado(dia);

        if (registros.isEmpty()) {
            sb.append("Nenhum veículo registrado neste dia.\n");
        } else {
            registros.forEach(r -> sb.append(r).append("\n"));
        }
        return sb.toString();
    }
}
