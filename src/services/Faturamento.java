package services;

import models.RegistroFaturamento;
import models.Veiculo;

import java.time.LocalDate;
import java.util.*;

/**
 * Responsável por registrar e gerenciar o faturamento do estacionamento.
 * Mantém controle diário e total dos valores arrecadados,
 * além dos registros detalhados de cada veículo.
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
     * Registra a saída de um veículo no sistema de faturamento.
     * Atualiza o total do dia e o total geral, além de salvar um registro detalhado.
     *
     * @param dataSaida Data em que o veículo saiu.
     * @param veiculo Veículo que está saindo.
     * @param valor Valor pago pelo cliente.
     */
    public void registrarSaida(LocalDate dataSaida, Veiculo veiculo, double valor) {
        totalGeral += valor;

        faturamentoDia.merge(dataSaida, valor, Double::sum);

        faturamentoDetalhado
                .computeIfAbsent(dataSaida, k -> new ArrayList<>())
                .add(new RegistroFaturamento(veiculo.getPlaca(), valor, dataSaida, veiculo.getTipo()));
    }

    /**
     * Retorna o valor total arrecadado desde o início do dia atual.
     *
     * @param data Data desejada.
     * @return Valor total do dia; 0.0 se não houver registros.
     */
    public double getFaturamentoDia(LocalDate data) {
        return faturamentoDia.getOrDefault(data, 0.0);
    }

    /**
     * Retorna todos os registros de faturamento detalhados de uma data específica.
     *
     * @param data Data desejada.
     * @return Lista de registros de faturamento, ou lista vazia se não houver.
     */
    public List<RegistroFaturamento> getFaturamentoDetalhado(LocalDate data) {
        return faturamentoDetalhado.getOrDefault(data, Collections.emptyList());
    }

    /**
     * Retorna o total geral acumulado de todas as datas.
     *
     * @return Valor total arrecadado.
     */
    public double getTotalGeral() {
        return totalGeral;
    }

    /**
     *
     * @param data Data escolhida para gerar relatorio
     * @return Retorna o relatorio da data escolhida
     */
    public String gerarRelatorio(LocalDate data) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Relatório de Faturamento - ").append(data).append(" ===\n");
        sb.append(String.format("Total do dia: R$ %.2f\n\n", getFaturamentoDia(data)));

        List<RegistroFaturamento> registros = getFaturamentoDetalhado(data);
        if (registros.isEmpty()) {
            sb.append("Nenhum veículo registrado neste dia.\n");
        } else {
            registros.forEach(r -> sb.append(r).append("\n"));
        }
        return sb.toString();
    }

}
