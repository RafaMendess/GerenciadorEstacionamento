package models;

public enum TipoVeiculo {
    CARRO(12.0, 8.0),
    MOTO(8.0, 5.0);

    private final double valorHoraInicial;
    private final double valorHoraAdicional;

    TipoVeiculo(double valorHoraInicial, double valorHoraAdicional) {
        this.valorHoraInicial = valorHoraInicial;
        this.valorHoraAdicional = valorHoraAdicional;
    }

    /**
     * Calcula o valor total com base nas horas inteiras de permanência.
     * A primeira hora tem valor fixo; as seguintes usam o adicional.
     */
    public double calcularValor(long horas) {
        if (horas <= 0) {
            return 0.0;
        } else if (horas <= 1) {
            return valorHoraInicial;
        } else {
            return valorHoraInicial + (horas - 1) * valorHoraAdicional;
        }
    }

    public double getValorHoraInicial() {
        return valorHoraInicial;
    }

    public double getValorHoraAdicional() {
        return valorHoraAdicional;
    }
}
