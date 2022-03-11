
package compilador.models;


public class Token {
    private String token;
    private String cadeia;
    private int linha;
    private int pos;
    public Token(String token, String cadeia) {
        this.token = token;
        this.cadeia = cadeia;
        this.linha=0;
        this.pos=0;
    }
    
    public Token(String token, String cadeia, int linha, int pos) {
        this.token = token;
        this.cadeia = cadeia;
        this.linha = linha;
        this.pos=0;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCadeia() {
        return cadeia;
    }

    public void setCadeia(String cadeia) {
        this.cadeia = cadeia;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }
    
    
    
    
    
}
