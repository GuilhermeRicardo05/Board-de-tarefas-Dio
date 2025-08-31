package controlador;

import modelo.Tarefa;
import servico.ServicoTarefa;
import util.Constantes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


public class ControladorQuadroTarefas {
    private ServicoTarefa servicoTarefa;
    private Scanner scanner;
    private DateTimeFormatter formatadorData;

   
    public ControladorQuadroTarefas() {
        this.servicoTarefa = new ServicoTarefa();
        this.scanner = new Scanner(System.in);
        this.formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

   
    public void iniciar() {
        System.out.println("=== QUADRO DE TAREFAS ===");
        
        while (true) {
            exibirMenuPrincipal();
            int opcao = lerInteiro("Escolha uma opção: ");
            
            switch (opcao) {
                case 1 -> exibirQuadroCompleto();
                case 2 -> criarTarefa();
                case 3 -> moverTarefa();
                case 4 -> editarTarefa();
                case 5 -> excluirTarefa();
                case 6 -> buscarTarefas();
                case 7 -> exibirTarefasAtrasadas();
                case 0 -> {
                    System.out.println("Saindo do sistema...");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
            
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }

    
    private void exibirMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Visualizar Quadro Completo");
        System.out.println("2. Criar Nova Tarefa");
        System.out.println("3. Mover Tarefa");
        System.out.println("4. Editar Tarefa");
        System.out.println("5. Excluir Tarefa");
        System.out.println("6. Buscar Tarefas");
        System.out.println("7. Tarefas Atrasadas");
        System.out.println("0. Sair");
        System.out.println("======================");
    }

   
    private void exibirQuadroCompleto() {
        System.out.println("\n=== QUADRO COMPLETO ===");
        
        exibirColuna(Constantes.STATUS_A_FAZER, "A FAZER");
        exibirColuna(Constantes.STATUS_FAZENDO, "FAZENDO");
        exibirColuna(Constantes.STATUS_FEITO, "FEITO");
    }

   
    private void exibirColuna(String status, String nomeColuna) {
        List<Tarefa> tarefas = servicoTarefa.filtrarTarefasPorStatus(status);
        
        System.out.println("\n--- " + nomeColuna + " (" + tarefas.size() + ") ---");
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa");
        } else {
            for (int i = 0; i < tarefas.size(); i++) {
                Tarefa tarefa = tarefas.get(i);
                System.out.printf("%d. %s (Prioridade: %d, Vencimento: %s)%n",
                    i + 1,
                    tarefa.getTitulo(),
                    tarefa.getPrioridade(),
                    formatadorData.format(tarefa.getDataVencimento()));
            }
        }
    }

   
    private void criarTarefa() {
        System.out.println("\n=== NOVA TAREFA ===");
        
        String titulo = lerString("Título: ");
        String descricao = lerString("Descrição: ");
        LocalDate dataVencimento = lerData("Data de vencimento (dd/MM/yyyy): ");
        int prioridade = lerInteiro("Prioridade (1-5): ");
        
        try {
            Tarefa tarefa = servicoTarefa.criarTarefa(titulo, descricao, dataVencimento, prioridade);
            System.out.println("Tarefa criada com sucesso! ID: " + tarefa.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    
    private void moverTarefa() {
        System.out.println("\n=== MOVER TAREFA ===");
        exibirQuadroCompleto();
        
        String id = lerString("ID da tarefa a mover: ");
        Tarefa tarefa = servicoTarefa.buscarTarefaPorId(id);
        
        if (tarefa == null) {
            System.out.println("Tarefa não encontrada!");
            return;
        }
        
        System.out.println("Tarefa: " + tarefa.getTitulo());
        System.out.println("Status atual: " + traduzirStatus(tarefa.getStatus()));
        
        System.out.println("\n1. Avançar status");
        System.out.println("2. Retroceder status");
        int opcao = lerInteiro("Escolha: ");
        
        boolean sucesso = false;
        if (opcao == 1) {
            sucesso = servicoTarefa.avancarStatus(id);
        } else if (opcao == 2) {
            sucesso = servicoTarefa.retrocederStatus(id);
        } else {
            System.out.println("Opção inválida!");
            return;
        }
        
        if (sucesso) {
            System.out.println("Tarefa movida com sucesso!");
        } else {
            System.out.println("Não foi possível mover a tarefa!");
        }
    }


    private void editarTarefa() {
        System.out.println("\n=== EDITAR TAREFA ===");
        String id = lerString("ID da tarefa a editar: ");
        
        Tarefa tarefa = servicoTarefa.buscarTarefaPorId(id);
        if (tarefa == null) {
            System.out.println("Tarefa não encontrada!");
            return;
        }
        
        System.out.println("Tarefa atual: " + tarefa);
        System.out.println("\nDeixe em branco para manter o valor atual:");
        
        String novoTitulo = lerStringOpcional("Novo título: ");
        String novaDescricao = lerStringOpcional("Nova descrição: ");
        String novaDataStr = lerStringOpcional("Nova data de vencimento (dd/MM/yyyy): ");
        String novaPrioridadeStr = lerStringOpcional("Nova prioridade (1-5): ");
        
        if (!novoTitulo.isEmpty()) tarefa.setTitulo(novoTitulo);
        if (!novaDescricao.isEmpty()) tarefa.setDescricao(novaDescricao);
        if (!novaDataStr.isEmpty()) {
            try {
                LocalDate novaData = LocalDate.parse(novaDataStr, formatadorData);
                tarefa.setDataVencimento(novaData);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida! Mantendo data original.");
            }
        }
        if (!novaPrioridadeStr.isEmpty()) {
            try {
                int novaPrioridade = Integer.parseInt(novaPrioridadeStr);
                if (novaPrioridade >= 1 && novaPrioridade <= 5) {
                    tarefa.setPrioridade(novaPrioridade);
                } else {
                    System.out.println("Prioridade inválida! Mantendo prioridade original.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Prioridade inválida! Mantendo prioridade original.");
            }
        }
        
        if (servicoTarefa.atualizarTarefa(tarefa)) {
            System.out.println("Tarefa atualizada com sucesso!");
        } else {
            System.out.println("Erro ao atualizar tarefa!");
        }
    }

   
    private void excluirTarefa() {
        System.out.println("\n=== EXCLUIR TAREFA ===");
        String id = lerString("ID da tarefa a excluir: ");
        
        Tarefa tarefa = servicoTarefa.buscarTarefaPorId(id);
        if (tarefa == null) {
            System.out.println("Tarefa não encontrada!");
            return;
        }
        
        System.out.println("Tarefa: " + tarefa.getTitulo());
        String confirmacao = lerString("Tem certeza que deseja excluir? (s/N): ");
        
        if (confirmacao.equalsIgnoreCase("s")) {
            if (servicoTarefa.removerTarefa(id)) {
                System.out.println("Tarefa excluída com sucesso!");
            } else {
                System.out.println("Erro ao excluir tarefa!");
            }
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    
    private void buscarTarefas() {
        System.out.println("\n=== BUSCAR TAREFAS ===");
        String termo = lerString("Termo de busca: ");
        
        List<Tarefa> resultados = servicoTarefa.buscarTarefasPorTitulo(termo);
        
        System.out.println("\nResultados da busca (" + resultados.size() + "):");
        if (resultados.isEmpty()) {
            System.out.println("Nenhuma tarefa encontrada.");
        } else {
            for (Tarefa tarefa : resultados) {
                System.out.printf("- %s [%s] (Prioridade: %d, Vencimento: %s)%n",
                    tarefa.getTitulo(),
                    traduzirStatus(tarefa.getStatus()),
                    tarefa.getPrioridade(),
                    formatadorData.format(tarefa.getDataVencimento()));
            }
        }
    }

  
    private void exibirTarefasAtrasadas() {
        System.out.println("\n=== TAREFAS ATRASADAS ===");
        List<Tarefa> atrasadas = servicoTarefa.getTarefasAtrasadas();
        
        if (atrasadas.isEmpty()) {
            System.out.println("Nenhuma tarefa atrasada! 👍");
        } else {
            System.out.println("Tarefas atrasadas (" + atrasadas.size() + "):");
            for (Tarefa tarefa : atrasadas) {
                long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(
                    tarefa.getDataVencimento(), LocalDate.now());
                
                System.out.printf("- %s [%s dias atrasado] (Prioridade: %d)%n",
                    tarefa.getTitulo(),
                    diasAtraso,
                    tarefa.getPrioridade());
            }
        }
    }

  
    private String traduzirStatus(String status) {
        return switch (status) {
            case Constantes.STATUS_A_FAZER -> "A Fazer";
            case Constantes.STATUS_FAZENDO -> "Fazendo";
            case Constantes.STATUS_FEITO -> "Feito";
            default -> status;
        };
    }

  private String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private String lerStringOpcional(String mensagem) {
        System.out.print(mensagem);
        String input = scanner.nextLine().trim();
        return input;
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido.");
            }
        }
    }

    private LocalDate lerData(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                String dataStr = scanner.nextLine().trim();
                return LocalDate.parse(dataStr, formatadorData);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido! Use dd/MM/yyyy.");
            }
        }
    }
}