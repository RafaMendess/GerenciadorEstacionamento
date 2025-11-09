package models;

/**
 * Enum que representa os tipos de veículos e suas tarifas correspondentes.
 * Cada tipo define o valor da primeira hora e o valor das horas adicionais.
 */
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
     *
     * @param horas Tempo total em horas inteiras (arredondado pra cima)
     * @return Valor total a ser pago
     */
    public double calcularValor(long horas) {
        if (horas <= 0) return 0.0;
        if (horas == 1) return valorHoraInicial;
        return valorHoraInicial + (horas - 1) * valorHoraAdicional;
    }

    public double getValorHoraInicial() {
        return valorHoraInicial;
    }

    public double getValorHoraAdicional() {
        return valorHoraAdicional;
    }

    /**
     * Retorna uma descrição legível do tipo de veículo e suas tarifas.
     * Útil para menus e relatórios.
     */
    public String getDescricao() {
        return String.format(
                "%s - Primeira hora: R$ %.2f | Hora adicional: R$ %.2f",
                name(), valorHoraInicial, valorHoraAdicional
        );
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
