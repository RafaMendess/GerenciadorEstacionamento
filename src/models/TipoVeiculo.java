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

    public double calcularValor(long horas) {
        if (horas <= 0) return 0.0;
        if (horas == 1) return valorHoraInicial;
        return valorHoraInicial + (horas - 1) * valorHoraAdicional;
    }

    public double getValorHoraInicial() { return valorHoraInicial; }

    public double getValorHoraAdicional() { return valorHoraAdicional; }

    public String getDescricao() {
        return String.format("%s - Primeira hora: R$ %.2f | Hora adicional: R$ %.2f",
                name(), valorHoraInicial, valorHoraAdicional);
    }

    @Override
    public String toString() { return name().toLowerCase(); }
}
