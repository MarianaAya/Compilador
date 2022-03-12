
package compilador.models;


public class Erro {
    private String mensagem;
    private int linha;
    private int pos;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public Erro(String mensagem, int linha, int pos) {
        this.mensagem = mensagem;
        this.linha = linha;
        this.pos = pos;
    }
    
    public Erro(String mensagem, int linha) {
        this.mensagem = mensagem;
        this.linha = linha;
        this.pos=0;
    }

    public Erro(String mensagem) {
        this.mensagem = mensagem;
        this.linha = 0;
        this.pos=0;
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
