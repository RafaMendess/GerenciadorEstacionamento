package models;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Representa um veículo no estacionamento.
 */
public class Veiculo {
    private final String placa;
    private final TipoVeiculo tipo;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;

    public Veiculo(String placa, TipoVeiculo tipo, LocalDateTime dataEntrada) {
        this.placa = placa;
        this.tipo = tipo;
        this.dataEntrada = dataEntrada!= null? dataEntrada: LocalDateTime.now();
    }

    // ===== Getters =====
    public String getPlaca() {
        return placa;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    // ===== Setters =====
    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }


    /**
     * Calcula o tempo total de permanência em horas inteiras, arredondando para cima.
     * @return número de horas estacionadas
     */
    public long calcularHorasEstacionadas() {
        if (dataEntrada == null || dataSaida == null) return 0;

        long minutos = Duration.between(dataEntrada, dataSaida).toMinutes();
        if (minutos <= 0) return 0;

        return (long) Math.ceil(minutos / 60.0);
    }

    /**
     * Calcula o valor total a pagar com base no tipo do veículo e tempo de permanência.
     * @return valor total a ser pago
     */
    public double calcularValorTotal() {
        long horas = calcularHorasEstacionadas();
        return tipo.calcularValor(horas);
    }

    @Override
    public String toString() {
        String entrada = dataEntrada != null ? dataEntrada.toString() : "—";
        String saida = dataSaida != null ? dataSaida.toString() : "Em andamento";

        return String.format(
                "Placa: %s | Tipo: %s | Entrada: %s | Saída: %s | Valor atual: R$ %.2f",
                placa, tipo.name(), entrada, saida, calcularValorTotal()
        );
    }
}
