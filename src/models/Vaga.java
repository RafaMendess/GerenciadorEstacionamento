package models;

public class Vaga {
    private final int id;
    private boolean ocupada;
    private Veiculo veiculo;

    public Vaga(int id) {
        if (id <= 0) throw new IllegalArgumentException("O ID da vaga deve ser positivo.");
        this.id = id;
        this.ocupada = false;
        this.veiculo = null;
    }

    public int getId() { return id; }

    public boolean isOcupada() { return ocupada; }

    public Veiculo getVeiculo() { return veiculo; }

    public void ocupar(Veiculo veiculo) {
        if (ocupada) throw new IllegalStateException("A vaga " + id + " já está ocupada!");
        this.veiculo = veiculo;
        this.ocupada = true;
    }

    public void liberar() {
        if (!ocupada) throw new IllegalStateException("A vaga " + id + " já está livre!");
        this.veiculo = null;
        this.ocupada = false;
    }

    @Override
    public String toString() {
        return String.format("Vaga %d - %s", id, ocupada ? "Ocupada por " + veiculo.getPlaca() : "Livre");
    }
}
