package models;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Veiculo {
    private final String placa;
    private final TipoVeiculo tipo;
    private Date dataEntrada;
    private Date dataSaida;

    public Veiculo(String placa, TipoVeiculo tipo) {
        this.placa = placa;
        this.tipo = tipo;
    }

    public String getPlaca() {
        return placa;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    /**
     * Calcula a duração da permanência em horas inteiras (arredondando pra cima).
     */
    public long calcularHorasEstacionadas() {
        if (dataSaida == null || dataEntrada == null) {
            return 0;
        }
        long diffMillis = dataSaida.getTime() - dataEntrada.getTime();
        return (long) Math.ceil((double) diffMillis / TimeUnit.HOURS.toMillis(1));
    }

    /**
     * Calcula o valor total a pagar com base no tipo do veículo e tempo.
     */
    public double calcularValorTotal() {
        long horas = calcularHorasEstacionadas();
        return tipo.calcularValor(horas);
    }

    @Override
    public String toString() {
        return String.format("Placa: %s | Tipo: %s | Entrada: %s | Saída: %s",
                placa,
                tipo.name(),
                dataEntrada,
                dataSaida != null ? dataSaida : "Em andamento");
    }
}
