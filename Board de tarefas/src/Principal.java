import controlador.ControladorQuadroTarefas;


public class Principal {
    public static void main(String[] args) {
        try {
            ControladorQuadroTarefas controlador = new ControladorQuadroTarefas();
            controlador.iniciar();
        } catch (Exception e) {
            System.err.println("Erro fatal na aplicação: " + e.getMessage());
            e.printStackTrace();
        }
    }
}