package repositorio;

import modelo.Tarefa;
import util.Constantes;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTarefa {
    private List<Tarefa> tarefas;
    private String arquivoDados;

    public RepositorioTarefa() {
        this.tarefas = new ArrayList<>();
        this.arquivoDados = Constantes.ARQUIVO_DADOS;
        carregarTarefas();
    }

   
    public void adicionarTarefa(Tarefa tarefa) {
        tarefas.add(tarefa);
        salvarTarefas();
    }

    
    public boolean removerTarefa(String id) {
        Tarefa tarefa = buscarPorId(id);
        if (tarefa != null) {
            tarefas.remove(tarefa);
            salvarTarefas();
            return true;
        }
        return false;
    }

   
    public boolean atualizarTarefa(Tarefa tarefaAtualizada) {
        for (int i = 0; i < tarefas.size(); i++) {
            if (tarefas.get(i).getId().equals(tarefaAtualizada.getId())) {
                tarefas.set(i, tarefaAtualizada);
                salvarTarefas();
                return true;
            }
        }
        return false;
    }

   
    public Tarefa buscarPorId(String id) {
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getId().equals(id)) {
                return tarefa;
            }
        }
        return null;
    }

    
    public List<Tarefa> listarTodas() {
        return new ArrayList<>(tarefas);
    }

    
    public List<Tarefa> filtrarPorStatus(String status) {
        List<Tarefa> resultado = new ArrayList<>();
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getStatus().equals(status)) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }

    
    public List<Tarefa> buscarPorTitulo(String titulo) {
        List<Tarefa> resultado = new ArrayList<>();
        for (Tarefa tarefa : tarefas) {
            if (tarefa.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                resultado.add(tarefa);
            }
        }
        return resultado;
    }

   
        private void carregarTarefas() {
        File diretorio = new File(Constantes.DIRETORIO_DADOS);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        File arquivo = new File(arquivoDados);
        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivoDados))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                Tarefa tarefa = parseTarefa(linha);
                if (tarefa != null) {
                    tarefas.add(tarefa);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar tarefas: " + e.getMessage());
        }
    }

    private Tarefa parseTarefa(String linha) {
        try {
            String[] partes = linha.split("\\|");
            if (partes.length == 7) {
                return new Tarefa(
                    partes[0], // id
                    partes[1], // titulo
                    partes[2], // descricao
                    partes[3], // status
                    LocalDate.parse(partes[4]), // dataVencimento
                    Integer.parseInt(partes[5]), // prioridade
                    LocalDate.parse(partes[6]) // dataCriacao
                );
            }
        } catch (Exception e) {
            System.err.println("Erro ao parsear tarefa: " + e.getMessage());
        }
        return null;
    }

    private void salvarTarefas() {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivoDados))) {
            for (Tarefa tarefa : tarefas) {
                escritor.println(serializarTarefa(tarefa));
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar tarefas: " + e.getMessage());
        }
    }

    private String serializarTarefa(Tarefa tarefa) {
        return tarefa.getId() + "|" +
               tarefa.getTitulo() + "|" +
               tarefa.getDescricao() + "|" +
               tarefa.getStatus() + "|" +
               tarefa.getDataVencimento() + "|" +
               tarefa.getPrioridade() + "|" +
               tarefa.getDataCriacao();
    }
}