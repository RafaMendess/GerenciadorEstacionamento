package models;

/**
 * Representa os possíveis resultados ao tentar registrar a entrada
 * de um veículo no estacionamento.
 *
 * <p>Usado para indicar ao chamador qual foi a ação tomada
 * pelo sistema ao processar a entrada.</p>
 *
 * <p>Possíveis valores:</p>
 * <ul>
 *     <li>{@link #ESTACIONADO} – veículo estacionado diretamente em uma vaga livre.</li>
 *     <li>{@link #FILA} – não havia vagas, então o veículo foi colocado na fila de espera.</li>
 *     <li>{@link #JA_EXISTE} – já existe um veículo com a mesma placa no estacionamento ou na fila.</li>
 * </ul>
 */
public enum EntradaStatus {

    /** Veículo estacionado com sucesso em uma vaga disponível. */
    ESTACIONADO,

    /** Veículo enviado para a fila de espera por falta de vagas. */
    FILA,

    /** Veículo com a mesma placa já se encontra no estacionamento ou na fila. */
    JA_EXISTE
}
