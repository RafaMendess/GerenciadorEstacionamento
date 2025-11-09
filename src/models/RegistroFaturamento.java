package models;

import java.time.LocalDate;

/**
 * Representa um registro individual de faturamento,
 * contendo informações sobre o veículo, valor pago, data e tipo.
 */
public class  RegistroFaturamento {
    private final String placa;
    private final double valorPago;
    private final LocalDate dataSaida;
    private final TipoVeiculo tipo;

    public RegistroFaturamento(String placa, double valorPago, LocalDate dataSaida, TipoVeiculo tipo) {
        this.placa = placa;
        this.valorPago = valorPago;
        this.dataSaida = dataSaida;
        this.tipo = tipo;
    }

    public String getPlaca() {
        return placa;
    }

    public double getValorPago() {
        return valorPago;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return String.format(
                "Placa: %s | Tipo: %s | Valor Pago: R$ %.2f | Data Saída: %s",
                placa, tipo.name(), valorPago, dataSaida
        );
    }
}
