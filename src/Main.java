import controllers.FaturamentoController;
import models.Estacionamento;
import controllers.EstacionamentoController;
import view.Menu;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        FaturamentoController faturamentoController= new FaturamentoController();
        while (true) {
            System.out.println("Iniciando um novo dia de trabalho...");

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

            Estacionamento estacionamento = new Estacionamento(capacidade, inicioDia, faturamentoController);
            EstacionamentoController service = new EstacionamentoController(estacionamento, menu);


            service.iniciarServico();

            // Após sair do dia:
            System.out.println("Deseja iniciar mais um dia de trabalho?");
            boolean continuar = menu.confirmarAcao("");

            if (!continuar) {
                System.out.println("Encerrando o programa...");
                break;
            }
        }
    }
}
