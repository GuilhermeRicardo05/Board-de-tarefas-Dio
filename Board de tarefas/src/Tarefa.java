package modelo;

import java.time.LocalDate;
import java.util.UUID;

public class Tarefa {
    private String id;
    private String titulo;
    private String descricao;
    private String status;
    private LocalDate dataVencimento;
    private int prioridade;
    private LocalDate dataCriacao;


    public Tarefa(String titulo, String descricao, LocalDate dataVencimento, int prioridade) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = "A_FAZER";
        this.dataVencimento = dataVencimento;
        this.prioridade = prioridade;
        this.dataCriacao = LocalDate.now();
    }


    public Tarefa(String id, String titulo, String descricao, String status, 
                 LocalDate dataVencimento, int prioridade, LocalDate dataCriacao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.dataVencimento = dataVencimento;
        this.prioridade = prioridade;
        this.dataCriacao = dataCriacao;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public int getPrioridade() { return prioridade; }
    public void setPrioridade(int prioridade) { this.prioridade = prioridade; }
    public LocalDate getDataCriacao() { return dataCriacao; }

    @Override
    public String toString() {
        return "Tarefa [ID: " + id + ", Título: " + titulo + ", Status: " + status + 
               ", Vencimento: " + dataVencimento + ", Prioridade: " + prioridade + "]";
    }
}