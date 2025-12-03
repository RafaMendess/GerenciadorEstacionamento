package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa um registro de faturamento referente à saída de um veículo.
 * O registro contém a placa, valor pago, tipo de veículo e a data da saída.
 */
public class Faturamento {

    /** Placa do veículo que gerou o faturamento. */
    private final String placa;

    /** Valor total pago pelo cliente ao sair. */
    private final double valorPago;

    /** Momento exato em que o veículo realizou a saída. */
    private final LocalDateTime dataSaida;

    /** Tipo do veículo (carro, moto etc). */
    private final TipoVeiculo tipo;

    /** Formato padrão de data para exibição. */
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Cria um registro de faturamento.
     *
     * @param placa     Placa do veículo.
     * @param valorPago Valor total pago pelo estacionamento.
     * @param dataSaida Data e hora da saída do veículo.
     * @param tipo      Tipo do veículo.
     */
    public Faturamento(String placa, double valorPago, LocalDateTime dataSaida, TipoVeiculo tipo) {
        this.placa = placa;
        this.valorPago = valorPago;
        this.dataSaida = dataSaida;
        this.tipo = tipo;
    }

    /** @return Placa do veículo. */
    public String getPlaca() { return placa; }

    /** @return Valor pago pelo cliente. */
    public double getValorPago() { return valorPago; }

    /** @return Data e hora da saída. */
    public LocalDateTime getDataSaida() { return dataSaida; }

    /** @return Tipo do veículo que gerou o faturamento. */
    public TipoVeiculo getTipo() { return tipo; }

    /**
     * Retorna uma representação textual do registro.
     *
     * @return Texto contendo placa, tipo, valor pago e data da saída.
     */
    @Override
    public String toString() {
        return String.format(
                "Placa: %s | Tipo: %s | Valor Pago: R$ %.2f | Data Saída: %s",
                placa,
                tipo.name(),
                valorPago,
                dataSaida != null ? dataSaida.format(fmt) : "—"
        );
    }
}
