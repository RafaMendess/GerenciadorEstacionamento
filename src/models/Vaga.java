package models;

/**
 * Representa uma vaga de estacionamento. Cada vaga possui um ID único,
 * pode estar ocupada ou livre, e opcionalmente possui um veículo associado.
 */
public class Vaga {

    /** Identificador único da vaga. */
    private final int id;

    /** Indica se a vaga está ocupada. */
    private boolean ocupada;

    /** Veículo atualmente estacionado na vaga (ou null se estiver livre). */
    private Veiculo veiculo;

    /**
     * Constrói uma vaga com um identificador numérico.
     *
     * @param id Identificador da vaga (deve ser positivo).
     * @throws IllegalArgumentException se o ID for zero ou negativo.
     */
    public Vaga(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID da vaga deve ser positivo.");
        }

        this.id = id;
        this.ocupada = false;
        this.veiculo = null;
    }


    /** @return ID da vaga. */
    public int getId() { return id; }

    /** @return true se a vaga está ocupada. */
    public boolean isOcupada() { return ocupada; }

    /** @return Veículo associado à vaga, caso exista. */
    public Veiculo getVeiculo() { return veiculo; }


    /**
     * Ocupa a vaga com o veículo informado.
     *
     * @param veiculo Veículo que será estacionado na vaga.
     * @throws IllegalStateException se a vaga já estiver ocupada.
     */
    public void ocupar(Veiculo veiculo) {
        if (ocupada) {
            throw new IllegalStateException("A vaga " + id + " já está ocupada!");
        }

        // Associa o veículo e marca a vaga como ocupada
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

        // Remove o veículo e marca a vaga como livre
        this.veiculo = null;
        this.ocupada = false;
    }

    /**
     * Retorna uma descrição curta da vaga.
     *
     * @return Texto indicando ID da vaga e seu estado atual.
     */
    @Override
    public String toString() {
        return String.format(
                "Vaga %d - %s",
                id,
                ocupada ? "Ocupada por " + veiculo.getPlaca() : "Livre"
        );
    }
}
