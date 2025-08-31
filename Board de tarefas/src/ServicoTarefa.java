package servico;

import modelo.Tarefa;
import repositorio.RepositorioTarefa;
import util.Constantes;

import java.time.LocalDate;
import java.util.ArrayList; 
import java.util.List;

public class ServicoTarefa {
    private RepositorioTarefa repositorio;

    public ServicoTarefa() {
        this.repositorio = new RepositorioTarefa();
    }


    
    public Tarefa criarTarefa(String titulo, String descricao, LocalDate dataVencimento, int prioridade) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título da tarefa não pode ser vazio");
        }
        if (prioridade < 1 || prioridade > 5) {
            throw new IllegalArgumentException("Prioridade deve estar entre 1 e 5");
        }

        Tarefa tarefa = new Tarefa(titulo, descricao, dataVencimento, prioridade);
        repositorio.adicionarTarefa(tarefa);
        return tarefa;
    }

    
    public boolean removerTarefa(String id) {
        return repositorio.removerTarefa(id);
    }

    
    public boolean atualizarTarefa(Tarefa tarefa) {
        return repositorio.atualizarTarefa(tarefa);
    }

   
    public boolean avancarStatus(String id) {
        Tarefa tarefa = repositorio.buscarPorId(id);
        if (tarefa != null) {
            String statusAtual = tarefa.getStatus();
            String novoStatus = obterProximoStatus(statusAtual);
            
            if (novoStatus != null) {
                tarefa.setStatus(novoStatus);
                return repositorio.atualizarTarefa(tarefa);
            }
        }
        return false;
    }

    
    public boolean retrocederStatus(String id) {
        Tarefa tarefa = repositorio.buscarPorId(id);
        if (tarefa != null) {
            String statusAtual = tarefa.getStatus();
            String novoStatus = obterStatusAnterior(statusAtual);
            
            if (novoStatus != null) {
                tarefa.setStatus(novoStatus);
                return repositorio.atualizarTarefa(tarefa);
            }
        }
        return false;
    }

    
    private String obterProximoStatus(String statusAtual) {
        return switch (statusAtual) {
            case Constantes.STATUS_A_FAZER -> Constantes.STATUS_FAZENDO;
            case Constantes.STATUS_FAZENDO -> Constantes.STATUS_FEITO;
            default -> null;
        };
    }

    
    private String obterStatusAnterior(String statusAtual) {
        return switch (statusAtual) {
            case Constantes.STATUS_FAZENDO -> Constantes.STATUS_A_FAZER;
            case Constantes.STATUS_FEITO -> Constantes.STATUS_FAZENDO;
            default -> null;
        };
    }

    
    public Tarefa buscarTarefaPorId(String id) {
        return repositorio.buscarPorId(id);
    }

    public List<Tarefa> listarTodasTarefas() {
        return repositorio.listarTodas();
    }

    
    public List<Tarefa> filtrarTarefasPorStatus(String status) {
        return repositorio.filtrarPorStatus(status);
    }

    
    public List<Tarefa> buscarTarefasPorTitulo(String titulo) {
        return repositorio.buscarPorTitulo(titulo);
    }

    
    public List<Tarefa> getTarefasAtrasadas() {
        List<Tarefa> todasTarefas = repositorio.listarTodas();
        List<Tarefa> atrasadas = new ArrayList<>();
        
        LocalDate hoje = LocalDate.now();
        for (Tarefa tarefa : todasTarefas) {
            if (!tarefa.getStatus().equals(Constantes.STATUS_FEITO) && 
                tarefa.getDataVencimento().isBefore(hoje)) {
                atrasadas.add(tarefa);
            }
        }
        return atrasadas;
    }
}