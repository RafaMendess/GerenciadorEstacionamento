package models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Veiculo {
    private final String placa;
    private final TipoVeiculo tipo;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Veiculo(String placa, TipoVeiculo tipo, LocalDateTime dataEntrada) {
        if (placa == null || placa.isBlank()) throw new IllegalArgumentException("Placa inválida.");
        this.placa = placa;
        this.tipo = tipo;
        this.dataEntrada = dataEntrada != null ? dataEntrada : LocalDateTime.now();
    }

    public String getPlaca() { return placa; }
    public TipoVeiculo getTipo() { return tipo; }
    public LocalDateTime getDataEntrada() { return dataEntrada; }
    public LocalDateTime getDataSaida() { return dataSaida; }

    public void setDataEntrada(LocalDateTime dataEntrada) { this.dataEntrada = dataEntrada; }
    public void setDataSaida(LocalDateTime dataSaida) { this.dataSaida = dataSaida; }

    public long calcularHorasEstacionadas() {
        if (dataEntrada == null || dataSaida == null) return 0;
        long minutos = Duration.between(dataEntrada, dataSaida).toMinutes();
        if (minutos <= 0) return 0;
        return (long) Math.ceil(minutos / 60.0);
    }

    public double calcularValorTotal() {
        long horas = calcularHorasEstacionadas();
        return tipo.calcularValor(horas);
    }

    @Override
    public String toString() {
        String entrada = dataEntrada != null ? dataEntrada.format(fmt) : "—";
        String saida = dataSaida != null ? dataSaida.format(fmt) : "Em andamento";
        return String.format("Placa: %s | Tipo: %s | Entrada: %s | Saída: %s | Valor atual: R$ %.2f",
                placa, tipo.name(), entrada, saida, calcularValorTotal());
    }
}
