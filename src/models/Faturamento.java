package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Faturamento {
    private final String placa;
    private final double valorPago;
    private final LocalDateTime dataSaida;
    private final TipoVeiculo tipo;
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Faturamento(String placa, double valorPago, LocalDateTime dataSaida, TipoVeiculo tipo) {
        this.placa = placa;
        this.valorPago = valorPago;
        this.dataSaida = dataSaida;
        this.tipo = tipo;
    }

    public String getPlaca() { return placa; }

    public double getValorPago() { return valorPago; }

    public LocalDateTime getDataSaida() { return dataSaida; }

    public TipoVeiculo getTipo() { return tipo; }

    @Override
    public String toString() {
        return String.format("Placa: %s | Tipo: %s | Valor Pago: R$ %.2f | Data Saída: %s",
                placa, tipo.name(), valorPago, dataSaida != null ? dataSaida.format(fmt) : "—");
    }
}
