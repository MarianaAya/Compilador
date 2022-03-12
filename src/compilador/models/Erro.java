
package compilador.models;


public class Erro {
    private String mensagem;
    private int linha;

    public Erro(String mensagem, int linha) {
        this.mensagem = mensagem;
        this.linha = linha;
    }

    public Erro(String mensagem) {
        this.mensagem = mensagem;
        this.linha = 0;
    }
    
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }
    
    
}
