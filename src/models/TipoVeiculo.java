package models;

/**
 * Enumeração que representa os tipos de veículos aceitos pelo sistema,
 * contendo suas regras de cobrança para a primeira hora e horas adicionais.
 */
public enum TipoVeiculo {

    /** Veículo do tipo carro: primeira hora mais cara, adicionais mais altos. */
    CARRO(12.0, 8.0),

    /** Veículo do tipo moto: valores reduzidos. */
    MOTO(8.0, 5.0);

    /** Valor cobrado pela primeira hora de estacionamento. */
    private final double valorHoraInicial;

    /** Valor cobrado por cada hora adicional após a primeira. */
    private final double valorHoraAdicional;

    /**
     * Construtor interno do enum.
     *
     * @param valorHoraInicial   Valor da primeira hora.
     * @param valorHoraAdicional Valor de cada hora adicional.
     */
    TipoVeiculo(double valorHoraInicial, double valorHoraAdicional) {
        this.valorHoraInicial = valorHoraInicial;
        this.valorHoraAdicional = valorHoraAdicional;
    }

    /**
     * Calcula o valor total com base no número de horas estacionadas.
     * - Se horas <= 0, retorna 0.
     * - Se horas == 1, cobra apenas o valor da primeira hora.
     * - Se > 1, soma primeira hora + horas adicionais.
     *
     * @param horas Quantidade de horas estacionadas (arredondadas previamente).
     * @return Valor total a pagar pelo tipo de veículo.
     */
    public double calcularValor(long horas) {
        if (horas <= 0) return 0.0;

        // Se ficou somente 1 hora, cobra apenas a hora inicial
        if (horas == 1) return valorHoraInicial;

        // Horas adicionais após a primeira
        return valorHoraInicial + (horas - 1) * valorHoraAdicional;
    }

    /** @return Valor da primeira hora. */
    public double getValorHoraInicial() { return valorHoraInicial; }

    /** @return Valor de cada hora adicional. */
    public double getValorHoraAdicional() { return valorHoraAdicional; }

    /**
     * Retorna uma descrição amigável contendo valores formatados.
     *
     * @return Texto com o nome do tipo e suas tarifas.
     */
    public String getDescricao() {
        return String.format(
                "%s - Primeira hora: R$ %.2f | Hora adicional: R$ %.2f",
                name(), valorHoraInicial, valorHoraAdicional
        );
    }

    /**
     * Sobrescreve toString para retornar o nome em minúsculas.
     *
     * @return Nome do tipo em letras minúsculas.
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
