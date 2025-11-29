import models.Estacionamento;
import controllers.EstacionamentoController;
import view.Menu;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();

        System.out.println("Quantas vagas o estacionamento terá hoje?");
        int capacidade = menu.lerInteiro();

        System.out.print("Deseja usar data/hora atual como referência de início do dia? ");
        boolean usarAgora = menu.confirmarAcao("");

        LocalDateTime inicioDia;
        if (usarAgora) {
            inicioDia = LocalDateTime.now();
        } else {
            System.out.print("Digite data e hora de início (dd/MM/yyyy HH:mm): ");
            inicioDia = menu.solicitarDataManualOuAutomatica();
        }

        Estacionamento estacionamento = new Estacionamento(capacidade, inicioDia);
        EstacionamentoController service = new EstacionamentoController(estacionamento, menu);

        service.iniciarServico();
    }
}
