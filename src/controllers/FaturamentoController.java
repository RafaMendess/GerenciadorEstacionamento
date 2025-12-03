package controllers;

import models.Faturamento;
import models.Veiculo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controla o faturamento diário e geral do estacionamento.
 *
 * <p>O faturamento é agrupado por dia utilizando {@link LocalDate} como chave.
 * Este controller mantém tanto o total do dia quanto um registro detalhado
 * contendo todos os veículos que realizaram pagamento.</p>
 */
public class FaturamentoController {

    /** Soma de todos os valores arrecadados desde o início do sistema. */
    private double totalGeral;

    /** Tabela contendo o valor total arrecadado por dia. */
    private final Map<LocalDate, Double> faturamentoDia;

    /** Lista detalhada das saídas ocorridas em cada dia. */
    private final Map<LocalDate, List<Faturamento>> faturamentoDetalhado;

    /**
     * Construtor padrão — inicializa containers de armazenamento.
     */
    public FaturamentoController() {
        this.totalGeral = 0.0;
        this.faturamentoDia = new HashMap<>();
        this.faturamentoDetalhado = new HashMap<>();
    }

    /**
     * Registra a saída de um veículo e atualiza os valores de faturamento.
     *
     * @param dataSaida Momento em que o veículo deixou o estacionamento.
     * @param veiculo   Objeto Veiculo que saiu.
     * @param valor     Valor pago pelo cliente.
     */
    public void registrarSaida(LocalDateTime dataSaida, Veiculo veiculo, double valor) {
        LocalDate dia = dataSaida.toLocalDate();

        // Atualiza o total geral
        totalGeral += valor;

        // Atualiza o total do dia atual
        faturamentoDia.merge(dia, valor, Double::sum);

        // Armazena registro detalhado
        faturamentoDetalhado
                .computeIfAbsent(dia, k -> new ArrayList<>())
                .add(new Faturamento(
                        veiculo.getPlaca(),
                        valor,
                        dataSaida,
                        veiculo.getTipo()
                ));
    }

    /**
     * Obtém o faturamento total de um dia específico.
     *
     * @param dia Dia desejado.
     * @return Valor total arrecadado naquele dia ou 0.0 caso não haja registros.
     */
    public double getFaturamentoDia(LocalDate dia) {
        return faturamentoDia.getOrDefault(dia, 0.0);
    }

    /**
     * Retorna a lista detalhada de registros de faturamento de um dia.
     *
     * @param dia Dia desejado.
     * @return Lista imutável dos registros (pode estar vazia).
     */
    public List<Faturamento> getFaturamentoDetalhado(LocalDate dia) {
        return faturamentoDetalhado.getOrDefault(dia, Collections.emptyList());
    }

    /**
     * @return Valor total arrecadado desde o início do sistema.
     */
    public double getTotalGeral() {
        return totalGeral;
    }

    /**
     * Gera um relatório textual contendo o total do dia e a lista de registros.
     *
     * @param dia Dia desejado para o relatório.
     * @return String formatada com os dados do faturamento.
     */
    public String gerarRelatorio(LocalDate dia) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== Relatório de Faturamento - ")
                .append(dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .append(" ===\n");

        sb.append(String.format("Total do dia: R$ %.2f\n\n", getFaturamentoDia(dia)));

        List<Faturamento> registros = getFaturamentoDetalhado(dia);

        // Caso nenhum veículo tenha saído neste dia
        if (registros.isEmpty()) {
            sb.append("Nenhum veículo registrado neste dia.\n");
        } else {
            // Imprime cada registro em uma linha
            registros.forEach(r -> sb.append(r).append("\n"));
        }

        return sb.toString();
    }
}
