package models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa um veículo estacionado, contendo placa, tipo e informações de entrada/saída.
 * A classe também calcula tempo de permanência e valor total com base no tipo do veículo.
 */
public class Veiculo {

    /** Placa do veículo (imutável). */
    private final String placa;

    /** Tipo do veículo (carro, moto, etc). */
    private final TipoVeiculo tipo;

    /** Data e hora de entrada no estacionamento. */
    private LocalDateTime dataEntrada;

    /** Data e hora de saída do estacionamento. */
    private LocalDateTime dataSaida;

    /** Formatação padrão para datas exibidas. */
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Constrói um veículo com placa, tipo e data de entrada.
     *
     * @param placa        Placa do veículo (não pode ser nula ou vazia).
     * @param tipo         Tipo do veículo (carro/moto).
     * @param dataEntrada  Momento da entrada. Caso seja null, utiliza o horário atual.
     *
     * @throws IllegalArgumentException se a placa for inválida.
     */
    public Veiculo(String placa, TipoVeiculo tipo, LocalDateTime dataEntrada) {
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("Placa inválida.");
        }

        this.placa = placa;
        this.tipo = tipo;
        this.dataEntrada = dataEntrada != null ? dataEntrada : LocalDateTime.now();
    }


    public String getPlaca() { return placa; }

    public TipoVeiculo getTipo() { return tipo; }

    public LocalDateTime getDataEntrada() { return dataEntrada; }

    public LocalDateTime getDataSaida() { return dataSaida; }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    /**
     * Calcula quantas horas o veículo permaneceu estacionado.
     * O cálculo usa arredondamento para cima (mínimo de 1 hora se tiver passado 1 minuto).
     *
     * @return Número de horas estacionadas. Retorna 0 se entrada ou saída forem nulas.
     */
    public long calcularHorasEstacionadas() {
        if (dataEntrada == null || dataSaida == null) return 0;

        // Calcula a diferença total em minutos
        long minutos = Duration.between(dataEntrada, dataSaida).toMinutes();

        // Evita resultado negativo caso datas estejam incorretas
        if (minutos <= 0) return 0;

        // Converte minutos para horas, sempre arredondando para cima
        return (long) Math.ceil(minutos / 60.0);
    }

    /**
     * Calcula o valor total a pagar com base nas horas estacionadas
     * e na regra de preço definida pelo tipo do veículo.
     *
     * @return Valor total baseado no tipo do veículo.
     */
    public double calcularValorTotal() {
        long horas = calcularHorasEstacionadas();
        return tipo.calcularValor(horas);
    }

    /**
     * Retorna uma string formatada contendo informações completas do veículo.
     *
     * @return Representação textual com placa, tipo, entrada, saída e valor acumulado.
     */
    @Override
    public String toString() {
        String entrada = (dataEntrada != null) ? dataEntrada.format(fmt) : "—";
        String saida = (dataSaida != null) ? dataSaida.format(fmt) : "Em andamento";

        return String.format(
                "Placa: %s | Tipo: %s | Entrada: %s | Saída: %s | Valor atual: R$ %.2f",
                placa, tipo.name(), entrada, saida, calcularValorTotal()
        );
    }
}
