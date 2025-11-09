package models;

/**
 * Representa uma vaga de estacionamento, que pode estar ocupada ou livre.
 * Cada vaga possui um identificador único e pode conter um veículo associado.
 */
public class Vaga {

    private final int id;

    private boolean ocupada;
    private Veiculo veiculo;

    /**
     * Constrói uma vaga com o ID informado.
     *
     * @param id Identificador da vaga (deve ser positivo).
     * @throws IllegalArgumentException se o ID for menor ou igual a zero.
     */
    public Vaga(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID da vaga deve ser positivo.");
        }
        this.id = id;
        this.ocupada = false;
        this.veiculo = null;
    }

    /**
     * Retorna o identificador único da vaga.
     *
     * @return ID da vaga.
     */

    public int getId() {
        return id;
    }

    /**
     * Verifica se a vaga está ocupada.
     *
     * @return {@code true} se a vaga estiver ocupada; {@code false} caso contrário.
     */
    public boolean isOcupada() {
        return ocupada;
    }

    /**
     * Retorna o veículo atualmente estacionado na vaga.
     *
     * @return Veículo da vaga, ou {@code null} se estiver livre.
     */
    public Veiculo getVeiculo() {
        return veiculo;
    }

    /**
     * Ocupa a vaga com um veículo específico.
     *
     * @param veiculo Veículo a ser estacionado.
     * @throws IllegalStateException se a vaga já estiver ocupada.
     */
    public void ocupar(Veiculo veiculo) {
        if (ocupada) {
            throw new IllegalStateException("A vaga " + id + " já está ocupada!");
        }
        this.veiculo = veiculo;
        this.ocupada = true;
    }

    /**
     * Libera a vaga, removendo o veículo associado.
     *
     * @throws IllegalStateException se a vaga já estiver livre.
     */
    public void liberar() {
        if (!ocupada) {
            throw new IllegalStateException("A vaga " + id + " já está livre!");
        }
        this.veiculo = null;
        this.ocupada = false;
    }

    @Override
    public String toString() {
        return String.format("Vaga %d - %s",
                id,
                ocupada ? "Ocupada por " + veiculo.getPlaca() : "Livre");
    }
}
